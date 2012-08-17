package com.cybernostics.lib.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The Relative Dimension calculates a fixed dimension based on a proportion of
 * the current screen size.
 */
public class ScreenRelativeDimension extends Dimension
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7346693841268826822L;

	//DimensionFloat relativeSize = new DimensionFloat();
	/**
	 * Relative size compared to screen size
	 * 
	 * @param width
	 *            - value between 1.0 and 0
	 * @param height
	 *            - value between 1.0 and 0
	 */
	public ScreenRelativeDimension( float width, float height )
	{
		Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

		this.width = (int) ( screenSize.width * width );
		this.height = (int) ( screenSize.height * height );
	}

	public ScreenRelativeDimension( double width, double height )
	{
		Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

		this.width = (int) ( screenSize.width * width );
		this.height = (int) ( screenSize.height * height );
	}
}
