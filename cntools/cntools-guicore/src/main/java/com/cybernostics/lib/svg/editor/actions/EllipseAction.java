package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.EllipseTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class EllipseAction extends ControlSelectAction
{
	public static final String ACTION_NAME_KEY = "Ellipse";

	public EllipseAction( SVGEditor svge )
	{
		super( svge, new EllipseTool() );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

}
