package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.lib.gui.dialogs.JDateChooser;
import java.awt.Component;
import java.util.Calendar;

/**
 * JDateChooser is a simple Date choosing component with similar functionality
 * to JFileChooser and JColorChooser. It can be used as a component, to be
 * inserted into a client layout, or can display it's own Dialog through use of
 * the {@link #showDialog(Component, String) showDialog} method. <p>
 * JDateChooser can be initialized to the current date using the no argument
 * constructor, or initialized to a predefined date by passing an instance of
 * Calendar to the constructor. <p> Using the JDateChooser dialog works in a
 * similar manner to JFileChooser or JColorChooser. The {@link #showDialog(Component, String) showDialog}
 * method returns an int that equates to the public variables ACCEPT_OPTION,
 * CANCEL_OPTION or ERROR_OPTION. <p> <tt> JDateChooser chooser = new
 * JDateChooser();<br> if (chooser.showDialog(this, "Select a date...") ==
 * JDateChooser.ACCEPT_OPTION) {<br> &nbsp;&nbsp;Calendar selectedDate =
 * chooser.getSelectedDate();<br> &nbsp;&nbsp;// process date here...<br> }<p>
 * To use JDateChooser as a component within a GUI, users should subclass
 * JDateChooser and override the {@link #acceptSelection() acceptSelection} and
 * {@link #cancelSelection() cancelSelection} methods to process the
 * corresponding user selection.<p> The current date can be retrieved by calling {@link #getSelectedDate() getSelectedDate}
 * method.
 */
public class JDateChooserExample
{

	public static void main( String[] args )
	{
		
		JDateChooser chooser = new JDateChooser();
		if (chooser.showDialog( null, "Select a date..." ) == JDateChooser.ACCEPT_OPTION)
		{
			Calendar selectedDate = chooser.getSelectedDate();
			System.out.println( selectedDate.toString() );
		}

		System.exit( 0 );
	}

}
