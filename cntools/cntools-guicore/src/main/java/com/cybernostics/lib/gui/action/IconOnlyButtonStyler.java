package com.cybernostics.lib.gui.action;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class IconOnlyButtonStyler implements ButtonStyler
{

	private static IconOnlyButtonStyler styler = null;

	public static IconOnlyButtonStyler get()
	{
		if (styler == null)
		{
			styler = new IconOnlyButtonStyler();
		}
		return styler;
	}

	private IconOnlyButtonStyler()
	{
	}

	@Override
	public void apply( AbstractButton ab )
	{
		ab.setOpaque( false );
		ab.setContentAreaFilled( false );
		ab.setBorderPainted( false );
		ab.setBorder( null );
		ab.setIconTextGap( 0 );
		ab.setMargin( null );
		ab.setText( null );

		Action a = ab.getAction();
		ab.setIcon( (Icon) a.getValue( Action.LARGE_ICON_KEY ) );
		ab.repaint();

	}
}
