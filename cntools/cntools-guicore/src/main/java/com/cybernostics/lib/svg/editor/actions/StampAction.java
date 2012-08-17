package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.ImageStampTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class StampAction extends ControlSelectAction
{
	public static final String ACTION_NAME_KEY = "Stamp";

	public StampAction( SVGEditor svge )
	{
		super( svge, new ImageStampTool() );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}
}
