package com.cybernostics.lib.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.cybernostics.lib.concurrent.GUIEventThread;

public abstract class ColumnArrayTableModel extends AbstractTableModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8508557850590663206L;
	private ArrayList< TableColumnCellDataAdapter > columnAdapters = new ArrayList< TableColumnCellDataAdapter >();

	public void addColumn( TableColumnCellDataAdapter newColumn )
	{
		columnAdapters.add( newColumn );
	}

	public int getColumnIndex( String nameToFind )
	{
		for (int index = 0; index < columnAdapters.size(); ++index)
		{
			if (columnAdapters.get(
				index )
				.getName()
				.equals(
					nameToFind ))
			{
				return index;
			}
		}
		return -1;
	}

	@Override
	public String getColumnName( int col )
	{
		return columnAdapters.get(
			col )
			.getName();
	}

	public int getColumnCount()
	{
		return columnAdapters.size();
	}

	public Object getValueAt( int row, int col )
	{
		return columnAdapters.get(
			col )
			.getValue(
				getRowData( row ) );
	}

	@Override
	public boolean isCellEditable( int row, int col )
	{
		return true;
	}

	@Override
	public Class< ? > getColumnClass( int col )
	{
		return columnAdapters.get(
			col )
			.getColumnClass();
	}

	@Override
	public void setValueAt( Object value, final int row, final int col )
	{
		columnAdapters.get(
			col )
			.setValue(
				getRowData( row ),
				value );
		GUIEventThread.run( new Runnable()
		{

			@Override
			public void run()
			{
				fireTableCellUpdated(
					row,
					col );
			}
		} );

	}

	abstract public Object getRowData( int row );

}
