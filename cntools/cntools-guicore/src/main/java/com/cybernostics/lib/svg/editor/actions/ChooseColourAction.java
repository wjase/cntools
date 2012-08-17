package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 *
 * @author jasonw
 */
public abstract class ChooseColourAction extends SVGEditorAction
{

	public ChooseColourAction( SVGEditor editor )
	{
		super( editor );
	}

	@Override
	public void onActionPerformed( ActionEvent e )
	{

		ColorChooser chooser = ColorPicker.getPicker();
		Color chosen = chooser.chooseColor(
			getEditor(),
			getCurrent() );
		if (chosen != null)
		{
			setColor( chosen );
		}
	}

	public abstract void setColor( Color c );

	public abstract Color getCurrent();
}
