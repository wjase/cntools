package com.cybernostics.lib.table;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellEditor;

public class WrapTextEditor extends AbstractCellEditor
	implements
	TableCellEditor,
	KeyListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 107243000804272438L;

	JTextArea myControl;

	public WrapTextEditor()
	{
		myControl = new JTextArea();
		myControl.setLineWrap( true );
		myControl.addKeyListener( this );
		// myControl.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
		// null);
		// myControl.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
		// null);

		AWTKeyStroke tab = AWTKeyStroke.getAWTKeyStroke(
			KeyEvent.VK_TAB,
			0 );
		AWTKeyStroke shiftTab = AWTKeyStroke.getAWTKeyStroke(
			KeyEvent.VK_TAB,
			InputEvent.SHIFT_DOWN_MASK );
		AWTKeyStroke ctrlTab = AWTKeyStroke.getAWTKeyStroke(
			KeyEvent.VK_TAB,
			InputEvent.CTRL_DOWN_MASK );
		AWTKeyStroke ctrlShiftTab = AWTKeyStroke.getAWTKeyStroke(
			KeyEvent.VK_TAB,
			InputEvent.CTRL_DOWN_MASK
				| InputEvent.SHIFT_DOWN_MASK );

		// create a java.util.Set for forwards and backwards focus traversal
		// keys. The actual concrete Set you use probably doesn't matter.
		// I've used a java.util.HashSet
		HashSet< AWTKeyStroke > forwardKeys = new HashSet< AWTKeyStroke >();
		HashSet< AWTKeyStroke > backwardKeys = new HashSet< AWTKeyStroke >();

		// add the appropriate keys to the appropriate sets
		forwardKeys.add( tab );
		forwardKeys.add( ctrlTab );
		backwardKeys.add( shiftTab );
		backwardKeys.add( ctrlShiftTab );

		// set the keys for the this traversal policy.
		myControl.setFocusTraversalKeys(
			KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
			forwardKeys );
		myControl.setFocusTraversalKeys(
			KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
			backwardKeys );
		myControl.getCaret()
			.setVisible(
				true );
		myControl.requestFocus();
	}

	// Implement the one CellEditor method that AbstractCellEditor doesn't.
	public Object getCellEditorValue()
	{
		return myControl.getText();
	}

	// Implement the one method defined by TableCellEditor.
	public Component getTableCellEditorComponent( JTable table,
		Object value,
		boolean isSelected,
		int row,
		int column )
	{
		myControl.setText( (String) value );
		myControl.requestFocus();
		myControl.grabFocus();
		myControl.getCaret()
			.setVisible(
				true );
		return myControl;
	}

	@Override
	public void keyPressed( KeyEvent e )
	{
	}

	@Override
	public void keyReleased( KeyEvent e )
	{
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			fireEditingStopped();
			e.consume();
		}
	}

	@Override
	public void keyTyped( KeyEvent e )
	{
	}

}
