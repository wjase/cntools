package com.cybernostics.lib.svg.editor.actions;

import java.awt.Color;
import java.awt.Component;

/**
 *
 * @author jasonw
 */
public interface ColorChooser
{
	public Color chooseColor( Component chooserParent, Color current );
}
