package com.cybernostics.lib.table;

/*
 * ColorEditor.java (compiles with releases 1.3 and 1.4) is used by TableDialogEditDemo.java.
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.cybernostics.lib.gui.dialogs.JDateChooser;

public class CalendarEditor extends AbstractCellEditor
	implements
	TableCellEditor,
	ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6813094095532961022L;
	Calendar currentDate = Calendar.getInstance();
	JButton button;
	JDateChooser datePicker = null;

	protected static final String EDIT = "editDate";

	public CalendarEditor()
	{
		// Set up the editor (from the table's point of view),
		// which is a button.
		// This button brings up the color chooser dialog,
		// which is the editor from the user's point of view.
		button = new JButton();
		button.setActionCommand( EDIT );
		button.addActionListener( this );
		button.setBorderPainted( false );

		// Set up the dialog that the button brings up.
		datePicker = new JDateChooser( currentDate );
	}

	/**
	 * Handles events from the editor button and from the dialog's OK button.
	 */
	public void actionPerformed( ActionEvent e )
	{
		if (EDIT.equals( e.getActionCommand() ))
		{
			// The user has clicked the cell, so
			// bring up the dialog.
			// button.setBackground(currentColor);
			datePicker.showDialog(
				null,
				"Choose Date" );

			currentDate = datePicker.getSelectedDate();

			// Make the renderer reappear.
			fireEditingStopped();
		}
	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue()
	{
		return currentDate;
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent( JTable table,
		Object value,
		boolean isSelected,
		int row,
		int column )
	{
		currentDate = (Calendar) value;
		return button;
	}
}
