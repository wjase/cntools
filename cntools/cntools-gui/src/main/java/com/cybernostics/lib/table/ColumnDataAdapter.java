package com.cybernostics.lib.table;

/**
 * This class sets and gets values for a field on an object which represents a
 * row of table data. e.g. a Person object with a Name and Age might be
 * represented in a table by two of these classes - one for each attribute.
 * 
 * Instances of this class are used in TableColumnCellDataAdapters
 * 
 * @see TableColumnCellDataAdapter
 * @author jasonw
 * 
 */
public interface ColumnDataAdapter
{

	/**
	 * Returns the field of the selected RowItem
	 * 
	 * @param rowItem
	 *            - object representing a row of Data
	 * @return
	 */
	public Object getValue( Object rowItem );

	/**
	 * Sets the field corresponding to this column in a table to value on the
	 * Object rowItem.
	 * 
	 * @param rowItem
	 *            - object representing a row of Data
	 * @param value
	 */
	public void setValue( Object rowItem, Object value );

}
