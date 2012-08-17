package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class SVGSaveAction extends SVGEditorAction
{
	public static final String ACTION_NAME_KEY = "Save";

	public SVGSaveAction( SVGEditor e )
	{
		super( e );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	@Override
	public void onActionPerformed( ActionEvent e )
	{
		getEditor().getDrawing()
			.write(
				new PrintWriter( System.out ) );
	}
}
