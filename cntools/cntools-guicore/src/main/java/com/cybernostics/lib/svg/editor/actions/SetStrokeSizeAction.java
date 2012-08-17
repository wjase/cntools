package com.cybernostics.lib.svg.editor.actions;

import com.cybernostics.lib.svg.editor.SVGEditor;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author jasonw
 */
public class SetStrokeSizeAction extends SVGEditorAction
{

	public static final String ACTION_NAME_KEY = "StrokeSize";

	public SetStrokeSizeAction( SVGEditor editor )
	{
		super( editor );
		putValue(
			Action.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
		addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				StrokeSizeAdjuster adjuster = getEditor().getStrokeWidthAdjuster();
				adjuster.setStrokeSize( chooseStrokeSize(
					(JComponent) e.getSource(),
					adjuster.getStrokeSize() ) );
			}

		} );
	}

	/**
	 * Override this to set new stroke size
	 *
	 * @param current
	 * @return
	 */
	public int chooseStrokeSize( JComponent source, int current )
	{
		Component c = SwingUtilities.getRoot( source );
		String result = JOptionPane.showInputDialog(
			c,
			String.format(
				"%d",
				current ) );
		return Integer.parseInt( result );
	}

}
