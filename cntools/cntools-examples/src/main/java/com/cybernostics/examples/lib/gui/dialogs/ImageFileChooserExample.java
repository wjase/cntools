package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.laf.wood.WoodLookAndFeel;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.dialogs.ImageFileChooser;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.RegexURLFilter;

public class ImageFileChooserExample
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				// OyoahaLook.set( JoeyMailResources.class );
				WoodLookAndFeel.setUI();
				
				// ImageFileChooser ic = new ImageFileChooser(
				// "C:\\data\\images\\Photographs\\2008\\2008_01_17" );
				ImageFileChooser ic = new ImageFileChooser();
				ic.setItemFilter( RegexURLFilter.getExcluder( "svg" ) );
				//ic.setSelectedFile( "file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg" );
				ic.setTitle( "Choose a File baby!" );

				ic.setVisible( true );

				if (ic.getResult() == DialogResponses.OK_ANSWER)
				{
					System.out.println( "File is:" + ic.getSelectedFile() );
				}
				else
				{
					System.out.println( "Cancelled" );
				}

				System.exit( 0 );
			}
		};
	}

}
