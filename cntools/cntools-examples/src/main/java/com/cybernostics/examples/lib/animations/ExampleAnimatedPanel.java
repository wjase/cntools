package com.cybernostics.examples.lib.animations;

import com.cybernostics.app.joeymail.res.JoeyMailResources;
import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.animator.SVGBackgroundImage;
import com.cybernostics.lib.animator.sprite.*;
import com.cybernostics.lib.animator.sprite.animators.SpriteMover;
import com.cybernostics.lib.animator.sprite.animators.SpriteRotator;
import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.track.BasicTimerTrack;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.LoopTrack;
import com.cybernostics.lib.animator.track.ordering.SerialTrack;
import com.cybernostics.lib.animator.track.ordering.TrackEndedActions;
import com.cybernostics.lib.animator.track.sprite.FrameAnimator;
import com.cybernostics.lib.animator.ui.SVGScenePanel2;
import com.cybernostics.lib.animator.ui.Scene;
import com.cybernostics.lib.animator.ui.SceneLoader;
import com.cybernostics.lib.animator.ui.transitions.EasingFunction;
import com.cybernostics.lib.gui.layout.BoxUtils;
import com.cybernostics.lib.gui.border.TubeBorder;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.shapeeffects.*;
import com.cybernostics.lib.gui.window.CNApplication;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.media.icon.TestIcon;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ExampleAnimatedPanel extends SVGScenePanel2
{

    public static Finder joeyLoader = JoeyMailResources.getFinder();

    JButton jbLeft = new JButton( "Left" );

    JButton jbRight = new JButton( "Right" );

    JSlider rotator = new JSlider( 0, 360 );

    JButton jbRotate = new JButton( "Rotate" );

    JButton jbPause = new JButton( "Pause" );

    JButton showMenu = new JButton( "Menu" )
    {

        @Override
        public void repaint()
        {
            super.repaint();
        }

        @Override
        public void repaint( long tm, int x, int y, int width, int height )
        {
            super.repaint( tm, x, y, width, height );
        }

    };

    JPanel menuPanel = new JPanel();

    /**
     *
     */
    private static final long serialVersionUID = -7320666814519695660L;

    public static void main( String[] args )
    {
        try
        {
//            JFrame jf = new JFrame( "SpriteTest" );
//
//            Sequencer.get().start();
//
//            final ExampleAnimatedPanel tp = new ExampleAnimatedPanel();
//            tp.addComponentListener( new ComponentAdapter()
//            {
//
//                @Override
//                public void componentResized( ComponentEvent e )
//                {
//                    Rectangle r = e.getComponent().getBounds();
//                    System.out.printf( "resized %d,%d,%d,%d\n", r.x, r.y, r.width, r.height );
//
//                }
//
//            } );
//            tp.setLayout( null );
//            tp.setMinimumSize( new Dimension( 300, 300 ) );
//            //tp.setPreferredSize( tp.getMinimumSize() );
//            jf.setContentPane( tp );
//            //jf.getContentPane().validate();
//            jf.setSize( 500, 500 );
//            jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//            GUIEventThread.show( jf );
            
            ScreenStack.get().register( "example", new ExampleAnimatedPanel());
            CNApplication.testScreen( "example" );

        }
        catch ( ResourceFinderException ex )
        {
            Logger.getLogger( ExampleAnimatedPanel.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }

    String BASE_DIR_IMG = "images/";

    private ISprite rooSprite = null;

    private boolean paused = false;

    public ExampleAnimatedPanel()
    {
        super();

        setLayout( null );

        new WhenClicked( jbPause )
        {

            @Override
            public void doThis( ActionEvent e )
            {
                paused = !paused;

                if ( paused )
                {
                    getTrackGroup().pause();
                    jbPause.setText( "resume" );
                }
                else
                {
                    getTrackGroup().resume();
                    jbPause.setText( "pause" );
                }
            }

        };

        setAssetLoader( new SceneLoader()
        {

            @Override
            public ShapeEffect loadBackGround()
            {
                SVGBackgroundImage ret = new SVGBackgroundImage( new ScalableSVGIcon( JoeyMailResources.getResource( "scenes/main/images/harbourzoo.svg" ) ) );
                return ret;
            }

            @Override
            public void loadAssets( final Scene panel )
            {

                ShapedPanel buttons = new ShapedPanel();
                buttons.setOpaque( false );

                //buttons.setBackground( Color.blue );
                buttons.setLayout( new BoxLayout( buttons, BoxLayout.X_AXIS ) );
                BoxUtils.addAll( buttons, rotator, jbRotate, showMenu, jbPause );

                buttons.setBorder( new TubeBorder( 20 ) );

                ComponentSprite buttonSprite = new ComponentSprite( buttons );
                buttonSprite.setRelativeBounds( new Rectangle2D.Double( 0.1, 0.1, .3, 0.15) );
                buttonSprite.setId( "buttonpanel" );

                IconSprite rooSprite2 = new IconSprite( "TheRoo" );
                rooSprite2.setIcon( new ScalableSVGIcon( JoeyMailResources.getResource( "scenes/intro/images/cyberlogo.svg" ) ) ); // finder.getResource( "images/backdrops/joeyfront.svg" )
                rooSprite2.setRelativeSize( new DimensionFloat( 0.2f, 0.2f ) );
                rooSprite2.setZ_order( 50 );
                rooSprite = rooSprite2;
                rooSprite.setShowControlPoint( true );

                rooSprite.setAnchor( Anchor.Position.CENTRE );
                rooSprite.setRelativeLocation( 0.5f, 0.5f );

                final FrameIconSprite fisFramer = new FrameIconSprite();
                fisFramer.addFrames( new ScalableSVGIcon( ResourcesRoot.getResource( "gui/images/icons/animtest.svg" ) ) );
                fisFramer.setZ_order( 100 );
                fisFramer.setRelativeBounds( 0.8, 0.07, 0.09, 0.09 );
                fisFramer.setId( "framer" );

                SVGSprite spgr = new SVGSprite( "Head" );
                spgr.setId( "Head" );
                spgr.setIcon( new ScalableSVGIcon( JoeyMailResources.getResource( "scenes/main/images/ferry.svg" ) ) );
                spgr.setRelativeBounds( 0, 0, .1, .1 );

//                TrackUpdatedListener watcher = new TrackUpdatedListener()
//                {
//
//                    @Override
//                    public void trackUpdated( TimeStamp now, Track source )
//                    {
//                        try
//                        {
//                            System.out.printf( "Track: %s elapsed:%d fraction:%f\n", source.getName(), now.getElapsed(), now.getFractionElapsed() );
//                        }
//                        catch ( DurationRequiredException ex )
//                        {
//                        }
//                    }
//
//                };
                BasicTimerTrack move1 = SpriteMover.moveTo( spgr, 1, 1, 10000 );
                move1.setHeadstart( 5000 );
                //move1.addTrackUpdatedListener( watcher );
                BasicTimerTrack moveBack = SpriteMover.moveTo( spgr, 0, 0, 10000 );
                //moveBack.addTrackUpdatedListener( watcher );
                addTrack( LoopTrack.create( LoopTrack.LOOP_FOREVER, SerialTrack.connect( move1,
                                                                                         moveBack ) ) );
                Icon penciltest = new TestIcon( 50, 50 );

                final IconSprite penIcon = new IconSprite( "pencil" );
                penIcon.setRelativeSize( new DimensionFloat( 0.05f, 0.05f ) );
                //penIcon.setIcon( new ScalableSVGIcon( ResourcesRoot.getResource( "gui/control/icons/chest.svg" ) ) );
                penIcon.setIcon( penciltest );
                penIcon.setAnchor( Anchor.Position.NORTHWEST );
                //penIcon.setScale( 2.0 );
                penIcon.setZ_order( 200 );
                penIcon.setShowControlPoint( true );

                
                ISprite houseandbridge = new SVGSprite( "house" );
                SpriteConfig.set(
                houseandbridge ).icon(
                JoeyMailResources.getResource( "scenes/main/images/houseandbridge.svg" ) ).
                boundsFromId( ExampleAnimatedPanel.this ).anchor(
                Anchor.Position.NORTHWEST ).zorder(
                150 );

                addSprites( houseandbridge);

                Toolkit.getDefaultToolkit().addAWTEventListener( new AWTEventListener()
                {

                    private void update( MouseEvent e )
                    {
                        Point p = e.getLocationOnScreen();
                        Point pComp = getLocationOnScreen();
                        Point prel = new Point( ( p.x - pComp.x ), ( p.y - pComp.y ) );

                        Dimension d = getSize();
                        penIcon.setRelativeLocation( prel.x * 1.0 / d.width, prel.y * 1.0 / d.height );

                    }

                    @Override
                    public void eventDispatched( AWTEvent event )
                    {
                        update( ( MouseEvent ) event );
                    }

                }, AWTEvent.MOUSE_MOTION_EVENT_MASK );


                addSprites( rooSprite, penIcon, buttonSprite, fisFramer, spgr );

                FrameAnimator fa = new FrameAnimator( fisFramer, 100 );
                addTrack( fa );

                new WhenMadeVisible( ExampleAnimatedPanel.this )
                {

                    @Override
                    public void doThis( AWTEvent e )
                    {
                        getTrackGroup().start();
                    }

                };


                System.out.printf( "Components after adding sprites:\n" );
                for ( int index = 0; index < layers.getComponentCount(); ++index )
                {
                    JComponent comp = ( JComponent ) layers.getComponent( index );
                    Rectangle rect = comp.getBounds();
                    System.out.printf( "Id:%s %d,%d,%d,%d\n",
                                       ( String ) comp.getClientProperty( ComponentSprite.ID_PROP ),
                                       rect.x,
                                       rect.y,
                                       rect.width,
                                       rect.height );
                }


                new WhenClicked( showMenu )
                {

                    @Override
                    public void doThis( ActionEvent e )
                    {
                        showMenu();
                    }

                };

            }

        } );

        rotator.getModel().addChangeListener( new ChangeListener()
        {

            @Override
            public void stateChanged( ChangeEvent e )
            {
                rooSprite.setRotationAngle( rotator.getValue() );
            }

        } );

        new WhenClicked( jbRotate )
        {

            @Override
            public void doThis( ActionEvent e )
            {
                startRotate();
            }

        };

    }

    private void showMenu()
    {
        final JButton closeWindow = new JButton( "Close" );

        final ShapedPanel newMenu = new ShapedPanel();

        ShapeEffectStack stack = new ShapeEffectStack( new CompositeAdjust( .6 ), new ColorFill( Color.BLUE ) );
        newMenu.setBackgroundPainter( stack );
        newMenu.setOpaque( false );
        newMenu.add( closeWindow );
        final ComponentSprite menuSprite = new ComponentSprite( newMenu );
        menuSprite.setRelativeSize( 0.5, 0.5 );
        menuSprite.setRelativeLocation( 1.1, 0.25 ); // just offscreen
        menuSprite.setId( "menu" );
        WatchableSprite ws = WatchableSprite.wrap( menuSprite );
        addSprites( ws );
        menuSprite.setTransparency( 0.5 );
        final BasicTimerTrack easeIn = SpriteMover.moveTo( ws, 0.25, 0.25, 2000 ).setTransition( EasingFunction.easeOutExpo.get() );

//        ws.addPropertyChangeListener( new PropertyChangeListener()
//        {
//
//            @Override
//            public void propertyChange( PropertyChangeEvent evt )
//            {
//                System.out.printf( "%s %s\n", evt.getPropertyName(), evt.getNewValue().toString() );
//            }
//
//        } );

        startTrack( easeIn );

        new WhenClicked( closeWindow )
        {

            @Override
            public void doThis( ActionEvent e )
            {
                // reverse the transition
                easeIn.reset();
                easeIn.setTransition( EasingFunction.easeInBounce.get() );

                //ISprite toMove = getById( "menu" );
                //Track easeOut = SpriteMover.moveTo( toMove, 1.1, 0.25, 2000 ).setTransition( EasingFunction.easeOutBounce.get() );
                easeIn.addTrackEndedListener( TrackEndedActions.removeComponent( menuSprite ) );
                startTrack( easeIn );
//                        new TrackEndedListener()
//                {
//
//                    @Override
//                    public void trackEnded( Track source )
//                    {
//                        removeSprites( getById( "menu" ) );
//                        System.out.printf( "done" );
//
//                    }
//
//                } );

            }

        };


    }

    @Override
    public void initSceneElements()
    {
        super.initSceneElements();
        //startAnimate();
        //start();

    }

    public ISprite getSprite()
    {
        return rooSprite;
    }

    public void startRotate()
    {
        Track rotatorTrack = SpriteRotator.newTrack( rooSprite, 0, 180, 3000 ).setTransition( EasingFunction.easeInQuint.get() );

        addTrack( rotatorTrack );
        rotatorTrack.start();

    }

}
