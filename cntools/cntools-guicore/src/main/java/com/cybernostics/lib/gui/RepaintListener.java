package com.cybernostics.lib.gui;

import java.awt.Rectangle;

/**
 * 
 * @author jasonw
 */
public interface RepaintListener
{

	/**
	 * 
	 */
	public void repaint();

	/**
	 * 
	 * @param r
	 */
	public void repaint( Rectangle r );
}
