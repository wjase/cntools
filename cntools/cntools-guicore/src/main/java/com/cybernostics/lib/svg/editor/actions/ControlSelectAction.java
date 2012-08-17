package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.EditController;
import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.event.ActionEvent;

/**
 *
 * @author jasonw
 */
public class ControlSelectAction extends SVGEditorAction
{

	protected EditController modeController = null;

	public EditController getModeController()
	{
		return modeController;
	}

	public ControlSelectAction( SVGEditor editor, EditController mode )
	{
		super( editor );
		this.modeController = mode;
	}

	@Override
	protected void onActionPerformed( ActionEvent e )
	{
		getEditor().setCurrentController(
			modeController );
	}

}
