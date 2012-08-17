package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Draws an effect and restores the state afterwards
 * @author jasonw
 */
public class AtomicDraw
{
	public static void draw( ShapeEffect se, Graphics2D g, Shape s )
	{
		StateSaver saver = StateSaver.wrap( g );
		try
		{
			se.draw(
				saver,
				s );
		}
		finally
		{
			saver.restore();
		}
	}
}
