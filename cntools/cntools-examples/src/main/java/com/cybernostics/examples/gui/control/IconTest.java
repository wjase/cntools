/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.examples.gui.control;

import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jasonw
 */
public class IconTest
{
    public static void main( String [] args )
    {
        try
        {
            JFrame jf = new JFrame();
            ScalableSVGIcon svi = new ScalableSVGIcon( new URL("file:///C:/data/Code/Java/projects/cntools-resources/src/main/resources/com/cybernostics/lib/gui/control/icons/handle.svg") );
            jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
            JLabel jl = new JLabel( svi );
            jf.getContentPane().add( jl );
            jf.setVisible( true );
        }
        catch ( MalformedURLException ex )
        {
            Logger.getLogger( IconTest.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }
}
