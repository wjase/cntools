
package com.cybernostics.examples.lib.gui;

import com.cybernostics.app.joeymail.res.JoeyMailResources;
import com.cybernostics.lib.gui.control.SoundToolTipButton;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author jasonw
 */
public class SoundTipButtonExample
{
    	public static void main( String[] args )
	{
        try
        {
            
            Finder finder = JoeyMailResources.getFinder();
            JFrame jf = new JFrame("tooltip");
            SoundEffect toPlay = new SoundEffect( finder.getResource( "sound/speech/treasurebox.mp3" ), 1.0f );
            SoundToolTipButton but = new SoundToolTipButton();
            jf.getContentPane().add( but );
            but.setTipSound(toPlay);
            but.setToolTipText( "Grab a picture from your treasure box!");
            
            
            jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            jf.setSize( 200, 300 );
            jf.setVisible( true );
        }
        catch ( ResourceFinderException ex )
        {
            Logger.getLogger( SoundTipButtonExample.class.getName() ).log( Level.SEVERE, null, ex );
        }
	}

}
