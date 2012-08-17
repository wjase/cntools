package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Component;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public interface ShapedBorder
{

	/**
	 * Return the region within which the border is painted
	 * 
	 * @param c - the component for which a Shape is requested
	 * @return a shape to be passed to setClip before rendering a border
	 */
	Shape getBorderClipShape( Component c );

	/**
	 * Returns the inner Shape which contains the background for a the component
	 * boarded by this border.
	 * @param c - the component for which a Shape is requested
	 * @return 
	 */
	Shape getBackgroundClipShape( Component c );

}
