package com.cybernostics.lib.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class WrapTextRenderer extends JScrollPane implements TableCellRenderer
{

	JTextArea area = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Border unselectedBorder = null;
	Border selectedBorder = null;
	boolean isBordered = true;

	Color defaultBackground = null;

	public WrapTextRenderer( boolean isBordered )
	{
		area = new JTextArea();
		area.setMargin( new Insets( 20, 20, 20, 20 ) );
		this.isBordered = isBordered;
		area.setOpaque( true ); // MUST do this for background to show up.
		area.setLineWrap( true );
		area.setColumns( 30 );
		// setRows( 2 );

		defaultBackground = area.getBackground();
		setViewportView( area );

		setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER );
	}

	public Component getTableCellRendererComponent( JTable table,
		Object theText,
		boolean isSelected,
		boolean hasFocus,
		int row,
		int column )
	{
		area.setText( (String) theText );
		area.setColumns( area.getText()
			.length() );

		int height = (int) getPreferredSize().getHeight();
		if (table.getRowHeight( row ) < height)
		{
			table.setRowHeight( height );
		}
		if (isSelected)
		{

			if (hasFocus)
			{
				area.setBackground( table.getSelectionBackground() );
			}
			else
			{
				area.setBackground( defaultBackground );
			}
		}
		else
		{
			area.setBackground( defaultBackground );
		}

		area.getCaret()
			.setVisible(
				hasFocus );
		if (hasFocus)
		{
			area.getCaret()
				.setBlinkRate(
					500 );
			area.grabFocus();
			area.requestFocus();
		}
		// if ( isBordered )
		// {
		// if ( isSelected )
		// {
		// if ( selectedBorder == null )
		// {
		// selectedBorder = BorderFactory.createMatteBorder( 2, 5, 2, 5,
		// table.getSelectionBackground() );
		// }
		// setBorder( selectedBorder );
		// }
		// else
		// {
		// if ( unselectedBorder == null )
		// {
		// unselectedBorder = BorderFactory.createMatteBorder( 2, 5, 2, 5,
		// table.getBackground() );
		// }
		// setBorder( unselectedBorder );
		// }
		// }

		return this;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension( 1, 1 );
	}

}
