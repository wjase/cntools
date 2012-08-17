package com.cybernostics.lib.gui.border;

import java.awt.Component;
import java.awt.Graphics;

/**
 * @author jasonw
 *
 */
public interface RendersBackgroundBorder
{
	/**
	 * For irregular and rounded borders, make the parent transparent and let the border render the component area
	 * 
	 * overload paint to fo this
	 * 
	 * 		public void paint( Graphics g )
			{
				((RendersBackgroundBorder)getBorder()).paintBackground( this, g, 0, 0, getWidth(), getHeight() );
				super.paint( g );
			}
	 * @param c - component to render
	 * @param g - graphics
	 * @param x - topleft x
	 * @param y - topleft y
	 * @param width - width of region
	 * @param height - height of region
	 */
	public void paintBackground( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height );

}
