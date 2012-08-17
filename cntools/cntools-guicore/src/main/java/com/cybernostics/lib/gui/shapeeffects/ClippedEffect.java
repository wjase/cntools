package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class ClippedEffect implements ShapeEffect
{

	ShapeEffect toDraw;

	public ClippedEffect( ShapeEffect toDraw )
	{
		this.toDraw = toDraw;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		// save current clip
		Shape clip = g2.getClip();
		try
		{
			g2.setClip( s );
			toDraw.draw(
				g2,
				s );
		}
		finally
		{
			// restore
			g2.setClip( clip );

		}
	}

}
