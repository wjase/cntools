
package com.cybernostics.examples.lib.animator.test;

import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.animator.sprite.ComponentSpriteOwner;
import com.cybernostics.lib.animator.sprite.IconSprite;
import com.cybernostics.lib.animator.sprite.WatchableSprite;
import com.cybernostics.lib.animator.sprite.animators.SpriteFader;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.ui.AnimatedFrame;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ComponentRepainter;
import com.cybernostics.lib.gui.CNButton;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author jasonw
 *
 */
public class SpriteIconPanelExample
{

    public static void main( String[] args )
    {
        AnimatedFrame jf = new AnimatedFrame( "test" );
        jf.setSize( 800, 600 );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        final JButton jb = new CNButton();
        IconSprite test = new IconSprite( "BooIcon" );
        test.setRelativeSize( new DimensionFloat( 1.0f, 1.0f ) );
        test.setIcon( new ScalableSVGIcon( ResourcesRoot.getResource( "gui/control/icons/chest.svg" ) ) );
        ComponentSpriteOwner cso = new ComponentSpriteOwner( jb );
        test.setOwner( cso );
        jb.setIcon( test );
        jb.setContentAreaFilled( false );


        Sequencer seq = new Sequencer();
        seq.start();

        WatchableSprite ws = new WatchableSprite( test );
        ws.addPropertyChangeListener( new PropertyChangeListener()
        {

            @Override
            public void propertyChange( PropertyChangeEvent evt )
            {
                System.out.println( evt.getPropertyName() );
                jb.repaint();
            }

        } );


        seq.setRepaintListener( new ComponentRepainter( jb ) );
        seq.addTrack( SpriteFader.cycle( ws, 0, 1, 3000 ) );
        seq.start();

//        seq.addUpdateCompleteListener( new UpdateCompleteListener()
//        {
//
//            @Override
//            public void update( TimeStamp timeNow )
//            {
//                jb.invalidate();
//            }
//        } );
        JPanel jp = new JPanel( new GridLayout() );
        jp.add( jb );
        jf.setContentPane( jp );
        //jf.getContentPane().add( jb );
        //jf.getContentPane().setBackground( Color.red );

        GUIEventThread.show( jf );
    }

}
