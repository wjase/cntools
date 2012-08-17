package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.laf.wood.WoodLookAndFeel;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.dialogs.CropDialog;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.URI;
import javax.swing.JFrame;

public final class CropDialogExample 
{

	public static void main( String[] args )
	{

		WoodLookAndFeel.setUI();

		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				try
				{
					JFrame test = new JFrame( "Crop Tester" );

					
					CropDialog ic = new CropDialog( test, new URI(
						"file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg" ) );

					test.setVisible( true );
					ic.setVisible( true );

					if (ic.getResult() == DialogResponses.OK_ANSWER)
					{
					}
					else
					{
						System.out.println( "Cancelled" );
					}

					System.exit( 0 );
				}
				catch (URISyntaxException ex)
				{
					Logger.getLogger( CropDialog.class.getName() ).log( Level.SEVERE, null, ex );
				}
			}
		};

	}

}
