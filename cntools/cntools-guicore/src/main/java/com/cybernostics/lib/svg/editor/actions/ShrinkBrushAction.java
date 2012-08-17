package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.event.ActionEvent;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class ShrinkBrushAction extends SVGEditorAction
{
	public static final String ACTION_NAME_KEY = "Smaller";

	public ShrinkBrushAction( SVGEditor editor )
	{
		super( editor );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	@Override
	public void onActionPerformed( ActionEvent e )
	{
		getEditor().getStrokeWidthAdjuster()
			.decreaseStroke();

	}
}
