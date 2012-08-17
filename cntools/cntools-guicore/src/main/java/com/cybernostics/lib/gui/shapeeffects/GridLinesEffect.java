package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shape.ShapeUtils2D;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * Draws a grid across the nominated draw shape bounds Note: Does not enforce
 * shape clipping region
 *
 * @author jasonw
 */
public class GridLinesEffect implements ShapeEffect
{

	private int numTicks = 10;

	private Color lineColor = Color.red;

	public GridLinesEffect( int numTicks )
	{
		this.numTicks = numTicks;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		StateSaver g3 = StateSaver.wrap( g2 );

		try
		{
			g3.setClip( s );
			g3.setColor( lineColor );
			Rectangle2D bounds = ShapeUtils2D.getBounds( s );
			double xInc = bounds.getWidth() / numTicks;
			double yInc = bounds.getHeight() / numTicks;
			double width = bounds.getWidth();
			double height = bounds.getHeight();

			for (double gridTick = 0.0; gridTick <= width; gridTick += xInc)
			{
				g3.draw( new Line2D.Double( gridTick, 0, gridTick, height ) );
			}
			for (double gridTick = 0.0; gridTick <= height; gridTick += yInc)
			{
				g3.draw( new Line2D.Double( 0f, gridTick, width, gridTick ) );
			}

		}
		finally
		{
			g3.restore();
		}
	}

	public void setColor( Color theColor )
	{
		this.lineColor = theColor;
	}

}
