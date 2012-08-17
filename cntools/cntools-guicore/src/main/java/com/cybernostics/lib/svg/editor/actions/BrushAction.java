package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.BrushTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class BrushAction extends ControlSelectAction
{
	public static final String ACTION_NAME_KEY = "Brush";

	public BrushAction( SVGEditor svge )
	{
		super( svge, new BrushTool() );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

}
