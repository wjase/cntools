package com.cybernostics.lib.media.icon;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author jasonw
 */
public class ScreenRelativeIconSizer
{

	private ScreenRelativeIconSizer()
	{
	}

	private static Dimension iconDim = null;

	public static Dimension getDefaultDimension()
	{
		if (iconDim == null)
		{
			iconDim = new Dimension( iconHeight, iconHeight );
		}
		return iconDim;
	}

	public static void setSize( ScalableIcon toSize )
	{
		double currentHeight = toSize.getIconHeight();
		double scaleY = getIconHeight() / currentHeight;

		Dimension toSet = new Dimension( (int) ( scaleY * toSize.getIconWidth() ),
			getIconHeight() );
		toSize.setSize( toSet );
		toSize.setMinimumSize( toSet );

	}

	static double fraction = 0.08;

	public static void setIconHeight( double fraction )
	{
		ScreenRelativeIconSizer.fraction = fraction;
	}

	static int iconHeight = 0;

	private static int getIconHeight()
	{
		if (iconHeight == 0)
		{
			int screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice()
				.getDisplayMode()
				.getHeight();
			iconHeight = (int) ( screenHeight * fraction );
		}

		return iconHeight;
	}
}
