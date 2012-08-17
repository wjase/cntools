package com.cybernostics.lib.gui.border;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.cybernostics.lib.gui.graphics.StateSaver;

public class GradientDropShadowBorder implements Border
{

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "Drop Shadow Test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JPanel jp = new JPanel( new FlowLayout() );
		jf.getContentPane()
			.add(
				jp );
		jf.getContentPane()
			.setBackground(
				Color.white );

		JPanel jpShadow = new JPanel();
		jf.getContentPane()
			.setLayout(
				new FlowLayout() );
		//jpShadow.setForeground( Color.blue );
		JLabel jl = new JLabel( "Hello" );
		jl.setAlignmentY( 0.5f );
		jl.setOpaque( true );
		jpShadow.add( jl );
		jl.setBackground( Color.gray.brighter()
			.brighter() );
		Dimension dPanelSize = new Dimension( 200, 200 );
		jp.setMinimumSize( dPanelSize );
		jp.setPreferredSize( dPanelSize );
		jl.setSize(
			100,
			100 );
		// jp.setBorder( BorderFactory.createLineBorder( Color.black, 20 ) );
		jp.setBorder( BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(
				30,
				30,
				30,
				30 ),
			new GradientDropShadowBorder() ) );
		jp.setOpaque( true );

		jp.add( jpShadow );
		jf.pack();
		jp.validate();

		jf.setSize(
			400,
			400 );
		jf.setVisible( true );

	}

	Insets myInsets;

	// used to track if we need to change the borders
	Dimension currentSize;

	Area leftBorder;

	GradientPaint leftPaint;

	Area rightBorder;

	GradientPaint rightPaint;

	Area topBorder;

	GradientPaint topPaint;

	Area bottomBorder;

	GradientPaint bottomPaint;

	Color shadowStart;

	Color shadowEnd;

	Area wholeBorder;

	Rectangle2D innerRect;

	Stroke nullStroke = new BasicStroke( 0 );

	public GradientDropShadowBorder()
	{
		myInsets = new Insets( 3, 3, 3, 3 );
	}

	private void createRegions( Component c, int x, int y, int width, int height )
	{
		innerRect = new Rectangle2D.Float( x + myInsets.left,
			y + myInsets.top,
			width - myInsets.left - myInsets.right,
			height - myInsets.top - myInsets.bottom );
		RoundRectangle2D roundRect = new RoundRectangle2D.Float( x,
			y,
			width,
			height,
			myInsets.left,
			myInsets.top );

		wholeBorder = new Area( roundRect );
		wholeBorder.subtract( new Area( innerRect ) );

		shadowEnd = new Color( 255, 255, 255, 0 );
		// getSolidParentBackGround();
		shadowStart = shadowEnd.darker()
			.darker()
			.darker()
			.darker();

		setupBottomSide(
			innerRect,
			x,
			y,
			width,
			height );
		// setupTopSide( innerRect, x, y, width, height );
		// setupLeftSide( innerRect, x, y, width, height );
		setupRightSide(
			innerRect,
			x,
			y,
			width,
			height );
	}

	@Override
	public Insets getBorderInsets( Component c )
	{
		return myInsets;
	}

	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}

	private void paint( Graphics2D g2, Area borderArea, GradientPaint paint )
	{
		// setup the background color first
		g2.setPaint( shadowEnd );

		g2.setStroke( nullStroke );
		g2.setPaint( paint );
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER ) );
		g2.fill( borderArea );
	}

	Stroke topLine = new BasicStroke( 2 );

	@Override
	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		StateSaver g2 = StateSaver.wrap( g );

		try
		{
			if (( currentSize == null )
				|| ( !currentSize.equals( c.getSize() ) ))
			{
				createRegions(
					c,
					x,
					y,
					width,
					height );
			}
			Color cSaved = g2.getColor();
			g2.setColor( shadowStart );
			g2.setStroke( topLine );
			//saver.g().draw( new Rectangle2D.Double( x, y, width, height ) );

			g2.draw( innerRect );

			// g2.draw(wholeBorder);
			g2.setColor( cSaved );

			Shape currentClip = g2.getClip();
			wholeBorder.intersect( new Area( currentClip ) );

			g2.setClip( wholeBorder );

			g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );

			//
			//
			paint(
				g2,
				rightBorder,
				rightPaint );
			paint(
				g2,
				bottomBorder,
				bottomPaint );
			//

		}
		finally
		{
			g2.restore();
		}

	}

	public void setupBottomSide( Rectangle2D componentRect,
		int x,
		int y,
		int width,
		int height )
	{
		// Create the clip region for the right-side
		Path2D.Float p = new Path2D.Float();

		p.moveTo(
			componentRect.getMinX() + 2,
			componentRect.getMaxY() );

		p.lineTo(
			componentRect.getMinX() + 2,
			y + height );
		p.lineTo(
			x + width,
			y + height );
		p.lineTo(
			componentRect.getMaxX(),
			componentRect.getMaxY() );
		p.closePath();

		bottomBorder = (Area) wholeBorder.clone();
		bottomBorder.intersect( new Area( p ) );

		bottomPaint = new GradientPaint( (int) componentRect.getCenterX(),
			(int) componentRect.getMaxY(),
			shadowStart,
			(int) componentRect.getCenterX(),
			y + height,
			shadowEnd );
	}

	public void setupLeftSide( Rectangle2D componentRect,
		int x,
		int y,
		int width,
		int height )
	{
		// Create the clip region for the right-side
		Path2D.Float p = new Path2D.Float();

		p.moveTo(
			componentRect.getMinX(),
			componentRect.getY() );

		p.lineTo(
			x,
			y );
		p.lineTo(
			x,
			y + height );
		p.lineTo(
			componentRect.getMinX(),
			componentRect.getMaxY() );
		p.closePath();

		leftBorder = (Area) wholeBorder.clone();
		leftBorder.intersect( new Area( p ) );

		leftPaint = new GradientPaint( (int) componentRect.getMinX(),
			(int) componentRect.getCenterY(),
			shadowStart,
			x,
			(int) componentRect.getCenterY(),
			shadowEnd );
	}

	public void setupRightSide( Rectangle2D componentRect,
		int x,
		int y,
		int width,
		int height )
	{
		// Create the clip region for the right-side
		Path2D.Float p = new Path2D.Float();

		p.moveTo(
			componentRect.getMaxX(),
			componentRect.getY() + 2 );

		p.lineTo(
			x + width,
			componentRect.getY() + 2 );
		p.lineTo(
			x + width,
			y + height );
		p.lineTo(
			componentRect.getMaxX(),
			componentRect.getMaxY() );
		p.closePath();

		rightBorder = (Area) wholeBorder.clone();
		rightBorder.intersect( new Area( p ) );

		rightPaint = new GradientPaint( (int) componentRect.getMaxX(),
			(int) componentRect.getCenterY(),
			shadowStart,
			x
				+ width,
			(int) componentRect.getCenterY(),
			shadowEnd );

	}

	public void setupTopSide( Rectangle2D componentRect,
		int x,
		int y,
		int width,
		int height )
	{
		// Create the clip region for the right-side
		Path2D.Float p = new Path2D.Float();

		p.moveTo(
			componentRect.getMinX(),
			componentRect.getY() );

		p.lineTo(
			x,
			y );
		p.lineTo(
			x + width,
			y );
		p.lineTo(
			componentRect.getMaxX(),
			componentRect.getY() );
		p.closePath();

		topBorder = (Area) wholeBorder.clone();
		topBorder.intersect( new Area( p ) );

		topPaint = new GradientPaint( (int) componentRect.getCenterX(),
			(int) componentRect.getMinY(),
			shadowStart,
			(int) componentRect.getCenterX(),
			y,
			shadowEnd );

	}

}
