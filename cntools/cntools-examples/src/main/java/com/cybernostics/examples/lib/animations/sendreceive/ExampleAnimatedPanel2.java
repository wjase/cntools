
package com.cybernostics.examples.lib.animations.sendreceive;

import com.cybernostics.lib.animator.SVGBackgroundImage;
import com.cybernostics.lib.animator.sprite.IconSprite;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.animator.ui.SVGScenePanel2;
import com.cybernostics.lib.animator.ui.Scene;
import com.cybernostics.lib.animator.ui.SceneLoader;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.layout.BoxUtils;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.resourcefinder.protocols.resloader.ResloaderURLScheme;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 *
 * @author jasonw
 */
public class ExampleAnimatedPanel2
{

    private static JSlider rotator = new JSlider( 0, 360 );

    private static JButton jbRotate = new JButton( "Rotate" );

    public static void main( String[] args )
    {
        JFrame jf = new JFrame( "Boo" );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setSize( 600, 400 );
        final SVGScenePanel2 ap = new SVGScenePanel2();
        //ap.setTransparency( 0.5 );
        ap.setBackground( Color.red );
        jf.getContentPane().setLayout( null );
        jf.getContentPane().setBackground( Color.blue );
        SVGScenePanel2 ap2 = new SVGScenePanel2();

        ap2.setAssetLoader( new SceneLoader()
        {

            @Override
            public ShapeEffect loadBackGround()
            {
                SVGBackgroundImage ret = new SVGBackgroundImage( new ScalableSVGIcon( ResloaderURLScheme.create( getClass(), "swirl.svg") ) );
                return ret;
            }

            @Override
            public void loadAssets( Scene panel )
            {
                JPanel buttons = new AnimatedScene();
                buttons.setOpaque( true );
                buttons.setLayout( new BoxLayout( buttons, BoxLayout.X_AXIS ) );
                BoxUtils.addAll( buttons, rotator, jbRotate );
//
                ap.addWithinBounds( buttons, new Rectangle2D.Double( 0.5, 0, 0.2, .1 ) );
                IconSprite rooSprite2 = new IconSprite( "TheRoo" );
                rooSprite2.setRelativeSize( new DimensionFloat( 0.2f, 0.2f ) );
                rooSprite2.setRelativeLocation( 0.8, 0.8 );
                rooSprite2.setIcon( new ScalableSVGIcon( new ScalableSVGIcon( ResloaderURLScheme.create( getClass(), "address-book-new.svg") ) )); // finder.getResource( "images/backdrops/joeyfront.svg" )
                ap.addSprites( rooSprite2 );
            }

        } );
        JButton jb = new JButton( "No Press Me" );
        ap2.addWithinBounds( jb, new Rectangle2D.Double( 0.6, 0.6, 0.3, 0.1 ) );
        ap.add( new JButton( "Press Me" ) );
        jf.getContentPane().setLayout( new GridLayout() );
        jf.getContentPane().add( ap );

        new WhenClicked( jb )
        {

            @Override
            public void doThis( ActionEvent e )
            {
                JOptionPane.showMessageDialog( null, "Boo" );
            }

        };
        jf.getContentPane().add( ap2 );
        ap2.setLocation( 200, 200 );
        ap.setLocation( 0, 400 );

        GUIEventThread.show( jf );
    }

}
