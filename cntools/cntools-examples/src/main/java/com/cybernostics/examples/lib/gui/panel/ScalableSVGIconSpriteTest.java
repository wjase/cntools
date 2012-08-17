
package com.cybernostics.examples.lib.gui.panel;

import com.cybernostics.lib.animator.sprite.SVGSprite;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.AWTEvent;
import java.awt.GridLayout;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;

/**
 *
 * @author jasonw
 */
public class ScalableSVGIconSpriteTest
{

    public static void main( String[] args )
    {
        final JFrame jf = new JFrame( "Spritetest" );
        jf.setSize( 600, 400 );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        jf.getContentPane().setLayout( new GridLayout() );

        final AnimatedScene sp = new AnimatedScene(  );
        jf.getContentPane().add( sp );

        new WhenMadeVisible( jf )
        {

            @Override
            public void doThis( AWTEvent e )
            {
                
                ScalableSVGIcon icon = IconFactory.getStdIcon( IconFactory.StdButtonType.NEW_CONTACT );
                Rectangle2D rect = new Rectangle2D.Double( 0.1, 0.1, 0.2, 0.2 );
                SVGSprite ss = new SVGSprite( "flash", rect );

                ss.setIcon( icon );
                sp.addSprites( ss );

                ScalableSVGIcon icon2 = IconFactory.getStdIcon( IconFactory.StdButtonType.NEW_CONTACT );
                Rectangle2D rect1 = new Rectangle2D.Double( 0.7, 0.1, 0.2, 0.2 );
                SVGSprite ss1 = new SVGSprite( "flash", rect1 );
                ss1.setIcon( icon2 );

                sp.addSprites( ss1 );
            }
        };

        GUIEventThread.run( new Runnable()
        {

            @Override
            public void run()
            {
                jf.setVisible( true );
            }
        } );


    }
}
