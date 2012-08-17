package com.cybernostics.lib.gui.border;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.Border;

public class GradientShadowBorder implements Border
{

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

	public GradientShadowBorder()
	{
		myInsets = new Insets( 10, 10, 10, 10 );
	}

	private void createRegions( Component c, int x, int y, int width, int height )
	{
		RoundRectangle2D roundRect = new RoundRectangle2D.Float( x,
			y,
			width,
			height,
			myInsets.left,
			myInsets.top );
		Rectangle2D innerRect = new Rectangle2D.Float( x + myInsets.left, y
			+ myInsets.top, width - myInsets.left
			- myInsets.right, height - myInsets.top - myInsets.bottom );
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
		setupTopSide(
			innerRect,
			x,
			y,
			width,
			height );
		setupLeftSide(
			innerRect,
			x,
			y,
			width,
			height );
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
		g2.fill( borderArea );
		g2.setStroke( new BasicStroke( 0 ) );
		g2.setPaint( paint );
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER ) );
		g2.fill( borderArea );
	}

	@Override
	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		if (( currentSize == null ) || ( !currentSize.equals( c.getSize() ) ))
		{
			createRegions(
				c,
				x,
				y,
				width,
				height );
		}

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON );

		Paint savedPaint = g2.getPaint();
		Composite savedComposite = g2.getComposite();

		paint(
			g2,
			rightBorder,
			rightPaint );
		paint(
			g2,
			leftBorder,
			leftPaint );
		paint(
			g2,
			topBorder,
			topPaint );
		paint(
			g2,
			bottomBorder,
			bottomPaint );

		g2.setComposite( savedComposite );
		g2.setPaint( savedPaint );

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
			componentRect.getMinX(),
			componentRect.getMaxY() );

		p.lineTo(
			x,
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
			componentRect.getY() );

		p.lineTo(
			x + width,
			y );
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
