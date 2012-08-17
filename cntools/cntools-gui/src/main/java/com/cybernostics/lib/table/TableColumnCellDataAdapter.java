package com.cybernostics.lib.table;

/**
 * Class to translate fields into columns in a JTable
 * 
 * @author jasonw
 * 
 */
public class TableColumnCellDataAdapter
{

	private String columnName;
	private Class< ? > columnType;
	private ColumnDataAdapter columnAdapter;

	public TableColumnCellDataAdapter( String columnName, Class< ? > columnType )
	{
		this.columnName = columnName;
		this.columnType = columnType;
	}

	public void setDataAdapter( ColumnDataAdapter adapter )
	{
		columnAdapter = adapter;
	}

	/**
	 * The name of the table column
	 * 
	 * @return
	 */
	public String getName()
	{
		return columnName;
	}

	/**
	 * @param rowItem
	 *            - object representing a row of Data
	 * @return
	 */
	public Object getValue( Object rowItem )
	{
		return columnAdapter.getValue( rowItem );
	}

	/**
	 * @param rowItem
	 *            - object representing a row of Data
	 * @param value
	 */
	public void setValue( Object rowItem, Object value )
	{
		columnAdapter.setValue(
			rowItem,
			value );
	}

	/**
	 * Returns the column class for this column data
	 * 
	 * @return
	 */
	public Class< ? > getColumnClass()
	{
		return columnType;
	}
}
