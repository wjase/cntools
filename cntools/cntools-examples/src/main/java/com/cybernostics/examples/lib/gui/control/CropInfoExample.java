package com.cybernostics.examples.lib.gui.control;

import com.cybernostics.lib.gui.control.CropInfo;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.URI;


public class CropInfoExample
{

	public static void main( String[] args )
	{
		try
		{
			JFrame jf = new JFrame( "cropImageTest" );
			jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
			jf.setSize( 600, 400 );

			

			CropInfo cropped = new CropInfo(
				new URI( "file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg" ), new Rectangle2D.Float(
					0.9f, 0.9f, 0.1f, 0.1f ) );

			// Save to XML
			String xml = CropInfo.toXML( cropped );
			System.out.println( xml );

			CropInfo cropped2 = CropInfo.fromXML( xml );

			xml = CropInfo.toXML( cropped2 );
			System.out.println( xml );
			try
			{
				jf.getContentPane().add( new JLabel( new ImageIcon( CropInfo.getCroppedImage( cropped2 ) ) ) );
			}
			catch (Exception ex)
			{
				Logger.getLogger( CropInfo.class.getName() ).log( Level.SEVERE, null, ex );
			}
			jf.setVisible( true );
		}
		catch (IOException ex)
		{
			Logger.getLogger( CropInfo.class.getName() ).log( Level.SEVERE, null, ex );
		}
		catch (URISyntaxException ex)
		{
			Logger.getLogger( CropInfo.class.getName() ).log( Level.SEVERE, null, ex );
		}

	}

}
