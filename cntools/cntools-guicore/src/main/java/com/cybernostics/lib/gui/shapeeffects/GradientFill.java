package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class GradientFill implements ShapeEffect
{

	GradientPaint gp;

	public GradientFill(
		final Point2D start,
		final Color clrFrom,
		final Color clrTo,
		final Point2D end )
	{
		gp = new GradientPaint( start, clrFrom, end, clrTo );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		StateSaver saver = StateSaver.wrap( g2 );
		try
		{
			saver.setPaint( gp );
			saver.fill( s );
		}
		finally
		{
			saver.restore();
		}
	}
}
