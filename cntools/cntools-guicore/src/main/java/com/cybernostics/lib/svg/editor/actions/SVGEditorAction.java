package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.gui.action.ChainableAction;
import com.cybernostics.lib.gui.action.IconOnlyButtonStyler;
import com.cybernostics.lib.svg.editor.SVGEditor;

/**
 *
 * @author jasonw
 */
abstract public class SVGEditorAction extends ChainableAction
{

	public static final String STYLER_KEY = "STYLER";
	private SVGEditor theEditor = null;

	public SVGEditor getEditor()
	{
		return theEditor;
	}

	public void setEditor( SVGEditor editor )
	{
		this.theEditor = editor;
	}

	public SVGEditorAction( SVGEditor editor )
	{
		theEditor = editor;
		putValue(
			STYLER_KEY,
			IconOnlyButtonStyler.get() );
	}

}
