package com.cybernostics.lib.gui.dialogs;

/**
 * Interface used by {@link com.cybernostics.lib.gui.dialogs.JDateChooser
 * JDateChooser} to notify when the user has selected a date.
 */
public interface DaySelectionListener
{

	/**
	 * Called when a user selects a date.
	 */
	public void daySelected( int day );

}