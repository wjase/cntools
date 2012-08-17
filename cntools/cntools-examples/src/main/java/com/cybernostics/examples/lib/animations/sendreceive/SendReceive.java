package com.cybernostics.examples.lib.animations.sendreceive;


import com.cybernostics.examples.lib.ExampleResources;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;


import com.cybernostics.lib.animator.BitmapBackground;
import com.cybernostics.lib.animator.sprite.BitmapSprite;
import com.cybernostics.lib.animator.paramaterised.HoppingPath;
import com.cybernostics.lib.animator.paramaterised.LinearPath;
import com.cybernostics.lib.animator.sprite.Anchor;
import com.cybernostics.lib.animator.track.sound.DirectionChangeListener;
import com.cybernostics.lib.animator.track.sound.MidiLoopTrack;
import com.cybernostics.lib.animator.track.sound.SoundEffectTrack;

import com.cybernostics.lib.animator.track.sprite.SpritePathMoverTrack;
import com.cybernostics.lib.animator.track.sprite.SpriteScalerTrack;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.media.image.ImageLoader;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 */
/**
 * @author jasonw
 *
 */
public class SendReceive extends JPanel
{

    String BASE_DIR_IMG = "animations/sendreceive/images/";

    String BASE_DIR_SND = "animations/sendreceive/sounds/";

    private static final long serialVersionUID = 360355862635474371L;

    /**
     * @param args
     */
    public static void main( String[] args )
    {
        
        JFrame jf = new JFrame( "AnimTest" );
        jf.setSize( 1000, 600 );
        jf.getContentPane().setLayout( new GridLayout() );
        jf.getContentPane().add( new SendReceive() );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setVisible( true );
    }

    public SendReceive()
    {
        final AnimatedScene ap = new AnimatedScene();
        setForeground( Color.blue );

        setLayout( new GridLayout() );
        add( ap );

        new WhenMadeVisible( ap )
        {

            @Override
            public void doThis( AWTEvent e )
            {
                try
                {
                    BufferedImage background = ImageLoader.loadBufferedImage( ExampleResources.getResource( BASE_DIR_IMG + "bushbackground.jpg" ) );
                    ap.setBackgroundPainter( new BitmapBackground( background ) );

                    BufferedImage carriage = ImageLoader.loadBufferedImage( ExampleResources.getResource( BASE_DIR_IMG + "MailCart.png" ) );
                    BufferedImage carriage1 = ImageLoader.loadBufferedImage( ExampleResources.getResource( BASE_DIR_IMG + "MailCart_1.png" ) );
                    // The Kangaroo sprite
                    BufferedImage rooImage = ImageLoader.loadBufferedImage( ExampleResources.getResource( BASE_DIR_IMG + "jumpingroo.png" ) );
                    BitmapSprite rooSprite = new BitmapSprite( "roo", rooImage, new DimensionFloat( 0.3f, 0.3f ) );
                    rooSprite.setRotationAngle( Math.PI / 9 );
                    // rooSprite.setShowControlPoint( true );
                    // rooSprite.setAnchor( Sprite.ANCHOR.CENTRE );
                    URL bounceSoundURL = ExampleResources.getResource( BASE_DIR_SND + "bounce.mp3" );

                    DirectionChangeListener bounce = new DirectionChangeListener( bounceSoundURL, 0.8f );

                    // Create a hopping path for the roo
                    HoppingPath rooPath2 = new HoppingPath( 0.2f, new Point2D.Float( 0.0f, 1.1f ), new Point2D.Float(
                            .52f, .63f ) );

                    SpritePathMoverTrack rooMover = new SpritePathMoverTrack( "rooMover", rooSprite, rooPath2, 10000 );

                    rooMover.addLocationListener( bounce );

                    //rooMover.addLocationListener( bounce );

                    ap.addTrack( rooMover );

                    SoundEffect bounceSound = new SoundEffect( bounceSoundURL, 0.8 );
                    bounceSound.play();
                    rooMover.startAfterMe( new SoundEffectTrack( "bounce", bounceSound ) );

                    SpriteScalerTrack rooSizer = new SpriteScalerTrack( "rooSizer", rooSprite, 100, 30, 10000 );
                    rooMover.startWithMe( rooSizer );

                    // Create a Linear path
                    BitmapSprite cartSprite = new BitmapSprite( "cart", carriage, new DimensionFloat( 0.35f, 0.35f ) );
                    // cartSprite.setShowControlPoint( true );
                    cartSprite.setAnchor( Anchor.Position.SOUTHWEST );

                    cartSprite.setIcon( new ImageIcon( carriage1 ) );
                    LinearPath cartPath = new LinearPath( new Point2D.Float( .85f, 1.2f ),
                                                          new Point2D.Float( .6f, .76f ) );
                    SpritePathMoverTrack cartMover = new SpritePathMoverTrack( "cartMover", cartSprite, cartPath, 20000 );
                    ap.addTrack( cartMover );

                    SpriteScalerTrack cartSizer = new SpriteScalerTrack( "cartSizer", cartSprite, 100, 30, 20000 );

                    cartMover.startWithMe( cartSizer );

//                    FrameAnimator cartFlipper = new FrameAnimator( "cartFrameFlipper", cartSprite, 80 );
//                    cartMover.startWithMe( cartFlipper );
//                    cartMover.stopAfterMe( cartFlipper );

                    SoundEffectTrack clipClop = new SoundEffectTrack( "clipClop", ExampleResources.getResource( BASE_DIR_SND + "clip_clop.mp3" ),
                                                                      0.6, Clip.LOOP_CONTINUOUSLY );
                    cartMover.startWithMe( clipClop );
                    cartMover.stopAfterMe( clipClop );

                    MidiLoopTrack midiLooper = new MidiLoopTrack( "miditune", ExampleResources.getResource( BASE_DIR_SND + "kookaburra.mid" ), 0.15 );

                    // cartMover.stopAfterMe( midiLooper );

                    ap.addTrack( midiLooper );

                    ap.addSprites( cartSprite );
                    ap.addSprites( rooSprite );
                   

                    // ap.setGridOn( true );
                }
                catch ( Exception e1 )
                {
                    UnhandledExceptionManager.handleException( e1 );
                }

            }

        };

        // ap.setStartWhenShown( true );

    }

}
