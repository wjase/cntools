package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.gui.action.ButtonStyler;
import javax.swing.AbstractButton;

/**
 *
 * @author jasonw
 */
public class TransparentButtonStyler implements ButtonStyler
{

	private static TransparentButtonStyler styler = null;

	public static TransparentButtonStyler get()
	{
		if (styler == null)
		{
			styler = new TransparentButtonStyler();
		}
		return styler;
	}

	private TransparentButtonStyler()
	{
	}

	@Override
	public void apply( AbstractButton ab )
	{
		ab.setOpaque( false );
	}
}
