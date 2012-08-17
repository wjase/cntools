package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.Color;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class FillColorChooserAction extends ChooseColourAction
{
	public static final String ACTION_NAME_KEY = "FillColour";

	public FillColorChooserAction( SVGEditor editor )
	{
		super( editor );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	@Override
	public void setColor( Color c )
	{
		getEditor().getDrawing()
			.setFillColour(
				c );
	}

	@Override
	public Color getCurrent()
	{
		return getEditor().getDrawing()
			.getFillColour();
	}

}
