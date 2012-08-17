package com.cybernostics.lib.gui.border;

import com.cybernostics.lib.media.image.BitmapMaker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import com.cybernostics.lib.resourcefinder.ResourceFinder;

public class TiledImageBorder extends AbstractBorder
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2202091024258710640L;

	public static void main( String args[] )
	{
		JFrame frame = new JFrame( "My Border" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		ImageIcon myIcon = new ImageIcon( "C:/data/eclipsework/Test/bin/leaves.gif" );
		Image anImage = myIcon.getImage();
		TiledImageBorder border = new TiledImageBorder( anImage );
		JPanel helloButton = new JPanel();
		helloButton.add( new JLabel( new ImageIcon( border.getBottomImage() ) ) );
		helloButton.setBorder( border );
		JLabel braveButton = new JLabel( "Brave New" );
		braveButton.setBorder( border );
		braveButton.setEnabled( false );
		JLabel worldButton = new JLabel( "World" );
		worldButton.setBorder( border );
		Container contentPane = frame.getContentPane();
		contentPane.add(
			helloButton,
			BorderLayout.NORTH );
		contentPane.add(
			braveButton,
			BorderLayout.CENTER );
		contentPane.add(
			worldButton,
			BorderLayout.SOUTH );
		frame.setSize(
			300,
			100 );
		frame.setVisible( true );
	}

	Image theImage;

	BufferedImage rightImage;
	BufferedImage topImage;
	BufferedImage bottomImage;
	ArrayList< Double > offsets = new ArrayList< Double >();

	public TiledImageBorder( Image suppliedImage )
	{
		theImage = suppliedImage;

		// Create a BufferedImage big enough to hold the Image loaded
		// in the constructor. Then copy that image into the new
		// BufferedImage object so that we can process it.
		BufferedImage bimage = BitmapMaker.createFastImage(
			theImage.getWidth( null ),
			theImage.getHeight( null ),
			Transparency.BITMASK );

		Graphics2D ig = bimage.createGraphics();
		ig.drawImage(
			theImage,
			0,
			0,
			null ); // copy the image

		// This AffineTransform is used by one of the image filters below
		AffineTransform mirrorTransform = AffineTransform.getTranslateInstance(
			bimage.getWidth(),
			0 );
		mirrorTransform.scale(
			-1.0,
			1.0 ); // flip horizontally

		BufferedImageOp mirror = new AffineTransformOp( mirrorTransform,
			AffineTransformOp.TYPE_BILINEAR );
		rightImage = mirror.filter(
			bimage,
			null );

		AffineTransform rotateRight = new AffineTransform();
		rotateRight.rotate( Math.PI / 2 );
		rotateRight.translate(
			bimage.getHeight(),
			bimage.getWidth() * -1 );

		BufferedImageOp rotateRightOp = new AffineTransformOp( rotateRight,
			AffineTransformOp.TYPE_BILINEAR );
		topImage = rotateRightOp.filter(
			bimage,
			null );

		AffineTransform mirrorVertical = new AffineTransform();
		mirrorVertical.translate(
			0,
			topImage.getHeight() );
		mirrorVertical.scale(
			1.0,
			-1.0 );

		BufferedImageOp flipVerticalOp = new AffineTransformOp( mirrorVertical,
			AffineTransformOp.TYPE_BILINEAR );
		bottomImage = flipVerticalOp.filter(
			topImage,
			null );

	}

	@Override
	public Insets getBorderInsets( Component c )
	{
		return new Insets( 30, 30, 30, 30 );
	}

	public BufferedImage getBottomImage()
	{
		return bottomImage;
	}

	public BufferedImage getRightImage()
	{
		return rightImage;
	}

	public BufferedImage getTopImage()
	{
		return topImage;
	}

	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}

	@Override
	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		Insets insets = getBorderInsets( c );
		// Color horizontalColor;

		// horizontalColor = Color.green;

		// Top
		// g.setColor(Color.green);
		// g.fillRect(0, 0, width, insets.bottom);
		tileImagesVertical(
			new Rectangle2D.Float( 0, 0, width, insets.bottom ),
			(Graphics2D) g,
			c,
			topImage,
			true );

		// Left
		g.setColor( Color.red );
		// g.fillRect(0, 0, insets.left, height);

		tileImagesHorizontal(
			(Rectangle2D) new Rectangle2D.Float( 0, 0, insets.left, height ),
			(Graphics2D) g,
			c,
			theImage,
			true );
		// Right
		g.setColor( Color.blue );
		// g.fillRect(width-insets.right, 0, width-insets.right, height);
		tileImagesHorizontal(
			(Rectangle2D) new Rectangle2D.Float( width - insets.right, 0, width
				- insets.right,
				height ),
			(Graphics2D) g,
			c,
			rightImage,
			false );
		// Bottom
		// g.setColor(Color.magenta);

		tileImagesVertical(
			new Rectangle2D.Float( 0, height - insets.bottom, width, height ),
			(Graphics2D) g,
			c,
			bottomImage,
			false );
		// g.fillRect(0, height-insets.bottom, width, height);
		// g.drawImage(topImage, 0, 0, null);

	}

	public void setBottomImage( BufferedImage bottomImage )
	{
		this.bottomImage = bottomImage;
	}

	public void setRightImage( BufferedImage rightImage )
	{
		this.rightImage = rightImage;
	}

	public void setTopImage( BufferedImage topImage )
	{
		this.topImage = topImage;
	}

	public void tileImagesHorizontal( Rectangle2D rect,
		Graphics2D g2,
		Component c,
		Image toPaint,
		boolean isLeft )
	{
		int imageheight = theImage.getHeight( null );
		int index = 0;

		for (int i = (int) rect.getMinY(); i < (int) rect.getMaxY();)
		{
			if (index >= offsets.size() - 1)
			{
				offsets.add( new Double( Math.random() ) );
			}
			float dRand = offsets.get(
				index )
				.floatValue();

			int iLeftPos = (int) rect.getMinX();
			if (isLeft)
			{
				iLeftPos -= ( toPaint.getWidth( null ) - ( rect.getWidth() ) );
			}
			else
			{
				// iLeftPos += (rect.getWidth());
			}
			// Position it to the left or right depending on which side
			g2.drawImage(
				toPaint,
				iLeftPos,
				i,
				null );
			i += ( imageheight ) * dRand;
			++index;
		}

	}

	public void tileImagesVertical( Rectangle2D rect,
		Graphics2D g2,
		Component c,
		Image toPaint,
		boolean istop )
	{
		int imageWidth = theImage.getWidth( null );
		int index = 0;

		for (int i = (int) rect.getMinX(); i < (int) rect.getMaxX();)
		{
			if (index >= offsets.size() - 1)
			{
				offsets.add( new Double( Math.random() ) );
			}
			double dRand = offsets.get(
				index )
				.floatValue();

			int iYPos = (int) rect.getMinY();
			if (istop)
			{
				iYPos -= ( toPaint.getHeight( null ) - ( rect.getHeight() ) );
			}
			else
			{
				// iLeftPos += (rect.getWidth());
			}
			// Position it to the left or right depending on which side
			g2.drawImage(
				toPaint,
				i,
				iYPos,
				null );
			i += ( imageWidth ) * dRand;
			++index;
		}

	}
}