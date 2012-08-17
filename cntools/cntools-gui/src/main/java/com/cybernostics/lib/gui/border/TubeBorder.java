package com.cybernostics.lib.gui.border;

import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.shapeeffects.ShapedBorder;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shapeeffects.ColorFill;
import com.cybernostics.lib.gui.shapeeffects.ImageStrokedEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Filename : TubeBorder.java Created By : jasonw Description: see class description below
 * 
 * Copyright Cybernostics Australia 2009
 */
/**
 * @author jasonw
 *
 */
public class TubeBorder extends AbstractBorder
	implements
	RendersBackgroundBorder,
	ShapedBorder
{

	/**
	 *
	 */
	private static final long serialVersionUID = -5402137527179588918L;

	Insets myInsets = new Insets( 15, 15, 15, 15 );

	Stroke thickLine = new BasicStroke( myInsets.left / 2 );

	RoundRectangle2D basicRect = new RoundRectangle2D.Double();

	ShapeEffect tubeEffect = null;

	@Override
	public Insets getBorderInsets( Component c )
	{
		return myInsets;
	}

	public TubeBorder( int radius )
	{
		this( radius, new ColorFill( Color.green ) );
	}

	public TubeBorder( int radius, Color tubeColour )
	{
		this( radius, new ColorFill( tubeColour ) );
	}

	public TubeBorder( int radius, ShapeEffect tubePainter )
	{
		basicRect.setRoundRect(
			basicRect.getMinX(),
			basicRect.getMinY(),
			basicRect.getWidth(),
			basicRect.getHeight(),
			radius,
			radius );
		this.tubeEffect = tubePainter;
		setInsets(
			radius,
			radius,
			radius,
			radius );

	}

	public void setInsets( int top, int left, int right, int bottom )
	{
		myInsets.top = top;
		myInsets.left = left;
		myInsets.bottom = bottom;
		myInsets.right = right;
		thickLine = new BasicStroke( myInsets.left / 2 );
	}

	public void setInsets( Insets other )
	{
		myInsets.top = other.top;
		myInsets.left = other.left;
		myInsets.bottom = other.bottom;
		myInsets.right = other.right;
		thickLine = new BasicStroke( myInsets.left / 2 );

	}

	@Override
	public Shape getBorderClipShape( Component c )
	{
		int linewidth = (int) ( (BasicStroke) thickLine ).getLineWidth();
		basicRect.setFrame(
			linewidth / 2,
			linewidth / 2,
			c.getWidth() - linewidth - 3,
			c.getHeight() - linewidth - 3 );
		return thickLine.createStrokedShape( basicRect );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component,
	 * java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		StateSaver g2 = StateSaver.wrap( g );
		float currentAlpha = 1f;
		try
		{
			Composite comp = g2.getComposite();
			if (comp instanceof AlphaComposite)
			{
				AlphaComposite ac = (AlphaComposite) comp;
				currentAlpha = ac.getAlpha();
			}
			Shape clipper = getBorderClipShape( c );
			g2.setClip( clipper );

			Shape darkBit = AffineTransform.getTranslateInstance(
				2,
				2 )
				.createTransformedShape(
					basicRect );
			g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );

			Stroke thinStroke = g2.getStroke();
			g2.setStroke( thickLine );

			g2.setColor( Color.black );
			g2.draw( darkBit );

			g2.setColor( Color.white );
			g2.draw( this.basicRect );

			g2.setColor( Color.darkGray );
			g2.setStroke( thinStroke );
			g2.draw( AffineTransform.getTranslateInstance(
				1,
				1 )
				.createTransformedShape(
					thickLine.createStrokedShape( this.basicRect ) ) );

			g2.setStroke( thickLine );

			g2.setComposite( AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER,
				currentAlpha * 0.9f ) );

			tubeEffect.draw(
				g2,
				thickLine.createStrokedShape( darkBit ) );

		}
		finally
		{
			g2.restore();

		}
		// clipArea.intersect( new Area( g2.getClip() ) );
		// g2.setClip( clipArea );

	}

	@SuppressWarnings("serial")
	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "BorderTest" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setSize(
			400,
			400 );
		jf.getContentPane()
			.setLayout(
				new FlowLayout() );
		jf.getContentPane()
			.setBackground(
				Color.red );

		final BufferedImage bi = new BufferedImage( 100,
			100,
			BufferedImage.TYPE_4BYTE_ABGR );
		Graphics2D g = bi.createGraphics();

		final ImageStrokedEffect iff = new ImageStrokedEffect( bi );
		g.setColor( Color.black );
		g.fillOval(
			0,
			0,
			bi.getWidth(),
			bi.getHeight() );
		ShapedPanel jp = new ShapedPanel()
		{

			//            @Override
			//            public void paintComponent( Graphics g )
			//            {
			//                super.paintComponent( g );
			//                iff.draw( (Graphics2D)g, new Rectangle2D.Double( 0, 0, getWidth(), getHeight()) );
			//                
			//                //g.drawImage( bi, 0, 0, this);
			//                
			//            }

			{
				//setOpaque( false );
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see javax.swing.JComponent#paint(java.awt.Graphics)
			 */
			// @Override
			//			public void paint( Graphics g )
			//			{
			//				Border b = getBorder();
			//				if ( b instanceof RendersBackgroundBorder )
			//				{
			//					( ( RendersBackgroundBorder ) b ).paintBackground( this, g, 0, 0, getWidth(), getHeight() );
			//				}
			//				super.paint( g );
			//			}
		};
		//jp.setBackground( Color.blue );

		jp.setPreferredSize( new Dimension( 500, 500 ) );
		TubeBorder tb = new TubeBorder( 50, iff );
		jp.setBorder( tb );
		jp.setBackgroundPainter( new ColorFill( Color.pink ) );
		jf.getContentPane()
			.add(
				jp );
		jf.setVisible( true );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.lib.gui.border.RendersBackgroundBorder#paintBackground
	 * (java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	@Override
	public void paintBackground( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		Graphics2D g2 = (Graphics2D) g;
		Color fg = g2.getColor();
		g2.setColor( c.getBackground() );

		g2.fill( this.basicRect );
		g2.setColor( fg );

	}

	@Override
	public Shape getBackgroundClipShape( Component c )
	{
		return basicRect;

	}

}
