package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.lib.gui.dialogs.DialogBox;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogBoxExample
{

	public static void main( String[] args )
	{

		
		try
		{
			System.out.println( DialogBox.getYesNoResponse( "Do you want to leave?", "Quit", null, false ) );
		}
		catch (ResourceFinderException ex)
		{
			Logger.getLogger( DialogBox.class.getName() ).log( Level.SEVERE, null, ex );
		}

		System.exit( 0 );
	}

}
