package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.RectangleTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class RectangleAction extends ControlSelectAction
{
	public static final String ACTION_NAME_KEY = "Rectangle";

	public RectangleAction( SVGEditor svge )
	{
		super( svge, new RectangleTool() );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

}
