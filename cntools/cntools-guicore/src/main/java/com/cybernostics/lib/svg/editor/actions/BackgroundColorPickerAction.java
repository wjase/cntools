package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.Color;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class BackgroundColorPickerAction extends ChooseColourAction
{
	public static final String ACTION_NAME_KEY = "Paper Colour";

	public BackgroundColorPickerAction( SVGEditor e )
	{
		super( e );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	@Override
	public void setColor( Color c )
	{
		getEditor().getDrawing()
			.setPaperColor(
				c );
	}

	@Override
	public Color getCurrent()
	{
		return getEditor().getDrawing()
			.getPaperColor();
	}
}
