package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.BrushTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class BrushSelectAction1 extends ControlSelectAction
{

	public BrushSelectAction1( SVGEditor editor )
	{
		super( editor, new BrushTool() );

		putValue(
			Action.SHORT_DESCRIPTION,
			"Brush" );
	}
}
