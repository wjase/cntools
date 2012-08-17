package com.cybernostics.lib.gui.icon;

import com.cybernostics.lib.media.icon.PreferredSizeListener;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.icon.NoImageIcon;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.image.BitmapMaker;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

/**
 * The Stamp Icon implements a postage-stamp-like effect around an image.
 * 
 * @author jasonw
 * 
 */
public class PerforatedStampIcon implements ScalableIcon
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main( String[] args )
	{
		// TODO Auto-generated method stub
		JFrame myApp = new JFrame( "Stamp Test" );

		myApp.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		myApp.setBackground( Color.black );

		// ImageIcon test = new
		// ImageIcon("file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg");
		ScalableIcon test = null;
		test = NoImageIcon.get();
		ScalableIcon generated = new ScalableIcon()
		{

			@Override
			public void setSize( Dimension2D d )
			{
				this.d.width = (int) d.getWidth();
				this.d.height = (int) d.getHeight();
			}

			Dimension d = new Dimension( 100, 100 );

			@Override
			public Dimension getPreferredSize()
			{
				return d;
			}

			@Override
			public void setMinimumSize( Dimension d )
			{

			}

			@Override
			public ScalableIcon copy()
			{
				return null;
			}

			@Override
			public void addPreferredSizeListener( PreferredSizeListener listener )
			{

			}

			@Override
			public BufferedImage getImage()
			{
				return null;
			}

			@Override
			public void addPropertyChangeListener( PropertyChangeListener listener )
			{

			}

			@Override
			public void paintIcon( Component c, Graphics g, int x, int y )
			{
				g.setColor( Color.red );
				g.fillRect(
					x,
					y,
					d.width,
					d.height );
			}

			@Override
			public int getIconWidth()
			{
				return d.width;
			}

			@Override
			public int getIconHeight()
			{
				return d.height;
			}
		};
		PerforatedStampIcon stamp = new PerforatedStampIcon( test );
		JLabel jb = new JLabel( new ImageIcon( stamp.getImage() ) );
		jb.setBackground( Color.gray );
		// jb.setContentAreaFilled(false);
		// jb.setBorderPainted(false);
		jb.setOpaque( true );
		myApp.add( jb );

		myApp.pack();
		myApp.setVisible( true );
	}

	ScalableIcon embeddedIcon;

	Dimension iconSize = new Dimension( 0, 0 );

	int newHeight = -1;

	int newWidth = -1;

	int PerforationWidth = 10;

	Rectangle2D stampRectangle = null;

	/**
	 * 
	 */
	Area theShadow;

	Area theShape = null;

	/**
	 * @param i
	 */
	public PerforatedStampIcon( ScalableIcon i )
	{
		embeddedIcon = i;

		setSize(
			(int) i.getIconWidth(),
			(int) i.getIconHeight() );
	}

	public PerforatedStampIcon( ScalableIcon i, Dimension iconSize )
	{
		embeddedIcon = i;

		setSize(
			(int) iconSize.getWidth(),
			(int) iconSize.getHeight() );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight()
	{
		if (newHeight == -1)
		{
			newHeight = iconSize.height + ( PerforationWidth * 2 );
			newHeight = newHeight
				+ ( PerforationWidth - ( newHeight % PerforationWidth ) + ( PerforationWidth / 2 ) );
		}

		return newHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth()
	{
		if (newWidth == -1)
		{
			newWidth = iconSize.width + ( PerforationWidth * 3 );
			newWidth = newWidth
				+ ( PerforationWidth - ( newWidth % PerforationWidth ) );
		}
		return newWidth;
	}

	/**
	 * @return
	 */
	Area getStampShadow()
	{
		if (theShadow == null)
		{
			AffineTransform shiftDownABit = new AffineTransform();
			shiftDownABit.translate(
				2,
				2 );

			theShadow = new Area( shiftDownABit.createTransformedShape( getStampShape() ) );
		}

		return theShadow;
	}

	/**
	 * @return
	 */
	public Area getStampShape()
	{

		if (theShape == null)
		{
			// First create a rectangle then chop out the horizontal
			// perforations
			stampRectangle = new Rectangle2D.Float( PerforationWidth,
				PerforationWidth / 2,
				iconSize.width
					+ ( PerforationWidth * 2 ),
				iconSize.height + ( PerforationWidth * 2 ) );
			Shape littleTopCircle = new Ellipse2D.Float( PerforationWidth / 2,
				0,
				PerforationWidth,
				PerforationWidth );
			Shape littleBottomCircle = new Ellipse2D.Float( PerforationWidth / 2,
				iconSize.height
					+ ( PerforationWidth * 2 ),
				PerforationWidth,
				PerforationWidth );

			theShape = new Area( stampRectangle );
			AffineTransform tx = new AffineTransform();
			tx.translate(
				10.0,
				0 );
			for (int current = (int) stampRectangle.getWidth(); current > 0; current = current
				- PerforationWidth)
			{
				theShape.subtract( new Area( littleTopCircle ) );
				theShape.subtract( new Area( littleBottomCircle ) );
				// shift the circles to the right
				littleTopCircle = tx.createTransformedShape( littleTopCircle );
				littleBottomCircle = tx.createTransformedShape( littleBottomCircle );
			}
			theShape.subtract( new Area( littleTopCircle ) );
			theShape.subtract( new Area( littleBottomCircle ) );

			// Next chop out the vertical perforations
			Shape littleLeftCircle = new Ellipse2D.Float( (float) ( stampRectangle.getMinX() - PerforationWidth / 2 ),
				(float) ( stampRectangle.getMinY() - PerforationWidth / 2 ),
				PerforationWidth,
				PerforationWidth );
			Shape littleRightCircle = new Ellipse2D.Float( (float) ( stampRectangle.getMaxX() - PerforationWidth / 2 ),
				(float) ( stampRectangle.getMinY() - PerforationWidth / 2 ),
				PerforationWidth,
				PerforationWidth );

			tx = new AffineTransform();
			tx.translate(
				0,
				10 );
			for (int current = (int) stampRectangle.getHeight(); current > 0; current = current
				- PerforationWidth)
			{
				theShape.subtract( new Area( littleLeftCircle ) );
				theShape.subtract( new Area( littleRightCircle ) );
				// shift the circles to the right
				littleLeftCircle = tx.createTransformedShape( littleLeftCircle );
				littleRightCircle = tx.createTransformedShape( littleRightCircle );
			}
			theShape.subtract( new Area( littleLeftCircle ) );
			theShape.subtract( new Area( littleRightCircle ) );

		}
		return theShape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		StateSaver g2 = StateSaver.wrap( g );

		g2.translate(
			x,
			y );
		try
		{
			g2.setPaint( Color.black );
			g2.fill( getStampShadow() );
			g2.setPaint( Color.white );
			g2.fill( getStampShape() );

			// TODO Auto-generated method stub
			// g.fillRect(0, 0, getIconWidth(), getIconHeight());

			int imageX = (int) ( stampRectangle.getMinX() + ( stampRectangle.getWidth() - iconSize.width ) / 2 );
			int imageY = (int) ( stampRectangle.getMinY() + ( stampRectangle.getHeight() - iconSize.height ) / 2 );
			// g2.drawImage( embeddedIcon, imageX, imageY, iconSize.width,
			// iconSize.height, null );
			g.translate(
				imageX,
				imageY );
			embeddedIcon.paintIcon(
				c,
				g,
				0,
				0 );
			g.translate(
				-imageX,
				-imageY );
			//			g2.drawImage( embeddedIcon, imageX, imageY, ( int ) stampRectangle.getWidth(), ( int ) stampRectangle
			//					.getHeight() - 3, 0, 0, embeddedIcon.getIconWidth(), embeddedIcon.getIconHeight(), null );
			// embeddedIcon.paintIcon( c, g, imageX, imageY );
			Rectangle2D border = new Rectangle2D.Float( imageX,
				imageY,
				iconSize.width,
				iconSize.height );
			BasicStroke bs = new BasicStroke( 3 );
			g2.setStroke( bs );
			g2.setPaint( new Color( 255, 255, 255, 255 ) );
			g2.setColor( Color.black );
			g2.draw( border );
			// g.setColor(Color.black);
			// g.drawRect(5, 5, embeddedIcon.getIconWidth(),
			// embeddedIcon.getIconHeight());

		}
		finally
		{
			g2.restore();
		}
	}

	private void setSize( int width, int height )
	{
		iconSize.height = height;
		iconSize.width = width;
		fireSizeChanged();
	}

	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	@Override
	public void setSize( Dimension2D d )
	{
		if (( dMin != null )
			&& ( ( d.getWidth() < dMin.width ) || ( d.getHeight() < dMin.height ) ))
		{
			return;
		}
		iconSize.setSize( d );
		fireSizeChanged();
	}

	@Override
	public ScalableIcon copy()
	{
		return this;
	}

	private List< PreferredSizeListener > listeners = new ArrayList< PreferredSizeListener >();

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		listeners.add( listener );
	}

	private void fireSizeChanged()
	{
		for (PreferredSizeListener eachListener : listeners)
		{
			eachListener.preferredSizeChanged( iconSize );
		}
	}

	@Override
	public BufferedImage getImage()
	{
		BufferedImage bi = BitmapMaker.createFastImage(
			getIconWidth(),
			getIconHeight(),
			Transparency.TRANSLUCENT );
		Graphics g = bi.getGraphics();
		paintIcon(
			null,
			g,
			0,
			0 );
		g.dispose();
		return bi;

	}

	@Override
	public Dimension getPreferredSize()
	{
		return iconSize;
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
	}

}
