
package com.cybernostics.examples.lib.gui.panel;

import com.cybernostics.lib.ResourcesRoot;

import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.shapeeffects.ImageFillEffect;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;


import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;

/**
 * @author jasonw
 * 
 */
public class TiledBackgroundTest
{

    public static void main( String[] args )
    {
        JFrame jf = new JFrame( "Static SVGPAnel" );

        Finder finder = ResourceFinder.get( ResourcesRoot.class );

        ShapedPanel test = new ShapedPanel();
        ImageFillEffect ife = null;
        try
        {
            ife = new ImageFillEffect( ImageLoader.loadBufferedImage( finder.getResource( "gui/images/tiles/leaves.png" ) ) );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( TiledBackgroundTest.class.getName() ).log( Level.SEVERE, null, ex );
        }


        test.setBackgroundPainter( ife );

        jf.setContentPane( test );

        jf.setSize(
                400, 400 );
        jf.setVisible(
                true );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }
}
