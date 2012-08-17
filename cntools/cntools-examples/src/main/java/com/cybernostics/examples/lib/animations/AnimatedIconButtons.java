
package com.cybernostics.examples.lib.animations;

import com.cybernostics.examples.lib.gui.panel.AppFrameTest;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.sprite.animators.SpriteFader;
import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.animator.ui.Scene;
import com.cybernostics.lib.animator.ui.SceneLoader;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.ParentSize;
import com.cybernostics.lib.gui.CNButton;
import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.shapeeffects.ColorFill;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author jasonw
 */
public class AnimatedIconButtons extends AppFrameTest
{

    @Override
    public void registerWindows()
    {
        AnimatedScene ac = new AnimatedScene();

        ac.setAssetLoader( new SceneLoader()
        {

            @Override
            public ShapeEffect loadBackGround()
            {
                return new ColorFill( Color.YELLOW );
            }

            @Override
            public void loadAssets( Scene panel )
            {
                BasicTrack.setDebug( true );
                JButton startButton = new CNButton();
                startButton.setIcon( IconFactory.getStdIcon( IconFactory.StdButtonType.PAINT ) );
                // Button to Start


                final ISprite paletteSprite = new ComponentSprite( startButton );
                paletteSprite.setId( "Palette" );
                paletteSprite.setRelativeBounds( 0.8, 0.8, 0.1, 0.1 );

                startButton.setName( "startBtn" );
                startButton.setVisible( true );
                startButton.setContentAreaFilled( false );

//                final WatchableSprite ws = WatchableSprite.wrap( paletteSprite );
                panel.addSprites( paletteSprite );
                Track startFader = SpriteFader.cycle( paletteSprite, 0, 1, 1000 );
//               
                panel.startTrack( startFader );
            }

        } );
        ScreenStack.get().register( "main", ac );
        super.registerWindows();
    }

    public static void main( String[] args )
    {
        AnimatedIconButtons ab = new AnimatedIconButtons();
        ab.registerWindows();
        ab.run( "AnimatedIconButtons" );
 //       JFrame jf = new JFrame( "stub" );
//        jf.setContentPane( ScreenStack.get().getLayeredPanel() );
//        ParentSize.bind( jf, ScreenStack.get().getLayeredPanel() );
//
//        ScreenStack.get().pushScreen( "main", null );
//
//        GUIEventThread.show( jf );
    }

}
