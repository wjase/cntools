package com.cybernostics.lib.gui.control;

import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class DefaultNextLastRenderer extends JEditorPane
	implements
	ListCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7480814197525258643L;

	public DefaultNextLastRenderer()
	{
		setEditable( false );
		setOpaque( false );
		putClientProperty(
			JEditorPane.HONOR_DISPLAY_PROPERTIES,
			Boolean.TRUE );

	}

	@Override
	public Component getListCellRendererComponent( JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus )
	{
		setText( (String) value );

		return this;
	}

}