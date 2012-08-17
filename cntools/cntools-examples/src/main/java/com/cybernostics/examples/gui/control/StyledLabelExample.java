
package com.cybernostics.examples.gui.control;

import com.cybernostics.laf.wood.WoodLookAndFeel;
import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.gui.control.StyledLabel;
import com.cybernostics.lib.gui.shapeeffects.CompositeAdjust;
import com.cybernostics.lib.gui.shapeeffects.ImageFillEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.media.image.ImageLoader;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

/**
 * @author jasonw
 *
 */
public class StyledLabelExample
{

    public static void main( String[] args )
    {
        try
        {
            WoodLookAndFeel.setUI();
            System.out.println( System.getProperty( "java.class.path" ) );
            //SystemLook.set();
            ShapedPanel jp = new ShapedPanel();
            jp.setBackgroundPainter( new ImageFillEffect( ImageLoader.loadBufferedImage( ResourcesRoot.getResource( "gui/images/tiles/walnut.png" ) ) ) );

            StyledLabel sl = new StyledLabel( "This is italic semi-opaque", StyledLabel.ITALIC );
            sl.setPaintEffect( new CompositeAdjust( 0.6 ) );
            sl.setBackground( Color.black );
            sl.setForeground( Color.white );
            sl.setOpaque( true );

            jp.setLayout( new BoxLayout( jp, BoxLayout.Y_AXIS) );
            jp.add( new StyledLabel( "This is bold", StyledLabel.BOLD ) );
            jp.add( new StyledLabel( "This is normal", StyledLabel.NORMAL ) );
            jp.add( new StyledLabel( "This is grey", StyledLabel.GREY ) );
            jp.add( new StyledLabel( "This is italic", StyledLabel.ITALIC ) );
            jp.add( sl );


            JFrame jf = new JFrame( "StyledLabel test" );
            jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            jf.getContentPane().add( jp );
            jf.setVisible( true );
            jf.pack();
        }
        catch ( IOException ex )
        {
            Logger.getLogger( StyledLabelExample.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
