package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * general interface for applying effects to a shape, like
 * outlines halos etc
 * @author jasonw
 */
public interface ShapeEffect
{
	public void draw( Graphics2D g2, Shape s );
}
