
package com.cybernostics.examples.lib.animator.test;

import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.animator.sprite.ComponentSpriteOwner;
import com.cybernostics.lib.animator.sprite.FrameIconSprite;
import com.cybernostics.lib.animator.sprite.WatchableSprite;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.sprite.FrameAnimator;
import com.cybernostics.lib.animator.track.sprite.OscillatingFrameController;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ComponentRepainter;
import com.cybernostics.lib.gui.CNButton;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author jasonw
 *
 */
public class FrameIconPanelExample
{

    public static void main( String[] args )
    {
        JFrame jf = new JFrame();
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setSize( 300, 300 );

        final JButton jb = new CNButton();

        final FrameIconSprite fis = new FrameIconSprite();
        fis.addFrames( new ScalableSVGIcon( ResourcesRoot.getResource( "gui/images/icons/animtest.svg" ) ) );

        fis.setRelativeSize( new DimensionFloat( 1.0f, 1.0f ) );
        ComponentSpriteOwner cso = new ComponentSpriteOwner( jb );
        fis.setOwner( cso );
        jb.setIcon( fis );
        jb.setContentAreaFilled( false );

        Sequencer seq = new Sequencer();
        seq.start();

        WatchableSprite ws = new WatchableSprite( fis );
        ws.addPropertyChangeListener( new PropertyChangeListener()
        {

            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                System.out.println( evt.getPropertyName() );
            }

        } );

//        final OscillatingFrameController ofc = new OscillatingFrameController();
//        ofc.setFrameCount( fis.getFrameCount() );

        FrameAnimator fa = new FrameAnimator( fis, 100 );
        fa.setFrameController( new OscillatingFrameController() );

        seq.setRepaintListener( new ComponentRepainter( jb ) );
        seq.addTrack( fa );
        seq.start();

        jf.getContentPane().setLayout( new BorderLayout() );
        jf.getContentPane().add( jb, BorderLayout.CENTER );
        jf.getContentPane().setBackground( Color.red );

//        new WhenClicked( jb )
//        {
//
//            @Override
//            public void doThis( ActionEvent e )
//            {
//                fis.setCurrentFrameNumber( ofc.nextFrame() );
//            }
//
//        };
        GUIEventThread.show( jf );
    }

}
