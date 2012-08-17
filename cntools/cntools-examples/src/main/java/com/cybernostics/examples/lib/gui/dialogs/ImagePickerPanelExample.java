package com.cybernostics.examples.lib.gui.dialogs;


import com.cybernostics.lib.gui.dialogs.ImagePickerPanel;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class ImagePickerPanelExample
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		try
		{
			JFrame jf = new JFrame();
			ImagePickerPanel ip = new ImagePickerPanel( new URL( "file:///C:/data/images/Photographs/2008/2008_01_17" ) );
			jf.getContentPane().add( ip );
			jf.setVisible( true );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger( ImagePickerPanelExample.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

}
