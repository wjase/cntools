package com.cybernostics.lib.table;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class CalendarRenderer extends JLabel implements TableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2398479625198709031L;
	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;

	private Calendar currentDate;

	public CalendarRenderer( boolean isBordered )
	{
		this.isBordered = isBordered;
		setOpaque( true ); // MUST do this for background to show up.
	}

	SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

	public Component getTableCellRendererComponent( JTable table,
		Object theDate,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column )
	{
		Calendar newDate = (Calendar) theDate;

		// only update if its new or different
		if (( currentDate == null ) || ( !currentDate.equals( newDate ) ))
		{

			if (!( newDate == null ))
			{
				setText( sdf.format( newDate.getTime() ) );
			}
		}

		if (isBordered)
		{
			if (isSelected)
			{
				if (selectedBorder == null)
				{
					selectedBorder = BorderFactory.createMatteBorder(
						2,
						5,
						2,
						5,
						table.getSelectionBackground() );
				}
				setBorder( selectedBorder );
			}
			else
			{
				if (unselectedBorder == null)
				{
					unselectedBorder = BorderFactory.createMatteBorder(
						2,
						5,
						2,
						5,
						table.getBackground() );
				}
				setBorder( unselectedBorder );
			}
		}

		return this;
	}
}
