package com.cybernostics.lib.svg.editor.actions;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JColorChooser;

/**
 *
 * @author jasonw
 */
public abstract class ColorPicker implements ColorChooser
{

	private static ColorChooser picker;

	public static ColorChooser getPicker()
	{
		if (picker == null)
		{
			return getDefault();
		}
		return picker;
	}

	public static void setPicker( ColorChooser cp )
	{
		picker = cp;
	}

	@Override
	abstract public Color chooseColor( Component chooserParent, Color current );

	private static ColorChooser defaultPicker = null;

	private static ColorChooser getDefault()
	{
		if (defaultPicker == null)
		{
			defaultPicker = new ColorChooser()
			{

				@Override
				public Color chooseColor( Component chooserComponent,
					Color current )
				{
					Color color = JColorChooser.showDialog(
						chooserComponent,
						"Pick a color",
						current );
					return color;
				}
			};
		}
		return defaultPicker;
	}
}
