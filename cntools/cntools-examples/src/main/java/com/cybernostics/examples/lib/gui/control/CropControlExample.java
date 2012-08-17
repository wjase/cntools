
package com.cybernostics.examples.lib.gui.control;

import com.cybernostics.lib.gui.control.CropControl;
import com.cybernostics.lib.gui.control.CropInfo;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import com.cybernostics.lib.gui.declarative.events.WhenHiddenOrClosed;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.URI;

/**
 * Crop Control allows users to define a sub region of a source image.
 * 
 * @author jasonw
 * 
 */
public class CropControlExample 
{


	public static void main( String[] args )
	{
		try
		{
			JFrame jf = new JFrame( "cropImageTest" );
			jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

			
			final CropInfo cropped = new CropInfo( new URI(
				"file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg" ), new Rectangle2D.Float( 0f, 0f, 1f,
				1f ) );
			jf.getContentPane().setLayout( new GridLayout() );
			jf.getContentPane().add( new CropControl( cropped ) );
			jf.setVisible( true );
			jf.pack();

			new WhenHiddenOrClosed( jf )
			{

				@Override
				public void doThis()
				{
					System.out.println( CropInfo.toXML( cropped ) );

				}
			};
		}
		catch (URISyntaxException ex)
		{
			Logger.getLogger( CropControl.class.getName() ).log( Level.SEVERE, null, ex );
		}
	}

}