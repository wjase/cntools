package com.cybernostics.lib.gui.action;

import javax.swing.Action;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
abstract public class CNAppAction extends ChainableAction
{

	public static String CLICK_SOUND = "CLICK_SOUND";
	public static String ACT_SOUND = "ACT_SOUND";
	public static String STYLER = "STYLER";

	public CNAppAction( String name, String id, Icon icon )
	{
		this( name, id, icon, IconOnlyButtonStyler.get() );
	}

	public CNAppAction( String name, String id, Icon icon, ButtonStyler styler )
	{
		super( name, id, icon );
		putValue(
			"STYLER",
			styler );
		putValue(
			Action.LARGE_ICON_KEY,
			icon );
		putValue(
			Action.SMALL_ICON,
			icon );

	}

	public Icon getIcon()
	{
		return (Icon) getValue( Action.LARGE_ICON_KEY );
	}

	public ButtonStyler getStyler()
	{
		ButtonStyler bs = (ButtonStyler) getValue( STYLER );
		return bs != null ? bs : IconOnlyButtonStyler.get();
	}

	public String getDescription()
	{
		return (String) getValue( Action.SHORT_DESCRIPTION );
	}
}
