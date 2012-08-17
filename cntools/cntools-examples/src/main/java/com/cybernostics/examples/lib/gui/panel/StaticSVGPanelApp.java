package com.cybernostics.examples.lib.gui.panel;

import com.cybernostics.lib.gui.panel.StaticSVGPanel;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * @author jasonw
 *
 */
public class StaticSVGPanelApp
{

    private static boolean bigPict = true;
    public static URL res()
    {
        if(bigPict)
        {
            return AppResources.getResource( "images/joeymain.svg" );
        }
        try
        {
            return new URL( "file:///C:/temp/svgtest/test3.svg" );
//            
        }
        catch ( MalformedURLException ex )
        {
            Logger.getLogger( StaticSVGPanelApp.class.getName() ).log( Level.SEVERE, null, ex );
        }
        return null;
    }

    public static void main( String[] args )
    {
        JFrame jf = new JFrame( "Static SVGPAnel" );
        


        StaticSVGPanel svgContent = new StaticSVGPanel( res() );

        jf.setContentPane( svgContent );

        jf.setSize( 400, 400 );
        jf.setVisible( true );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

    }

}
