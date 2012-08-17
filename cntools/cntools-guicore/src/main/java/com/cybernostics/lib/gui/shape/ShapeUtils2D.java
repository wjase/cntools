package com.cybernostics.lib.gui.shape;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jasonw
 */
public class ShapeUtils2D
{

	/**
	 * Saves creating another Rectangle if it is already a rectangle Beware -
	 * don't modify the rect - call getBounds2D if you want to do that as it
	 * always creates a new rect
	 *
	 * @param toGet
	 * @return
	 */
	public static Rectangle2D getBounds( Shape toGet )
	{
		if (toGet instanceof Rectangle2D)
		{
			return (Rectangle2D) toGet;
		}
		return toGet.getBounds2D();
	}

}
