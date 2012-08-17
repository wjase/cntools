
package com.cybernostics.examples.lib.gui.panel;

import com.cybernostics.lib.animator.sprite.component.ComponentSprite;
import com.cybernostics.lib.animator.sprite.component.ToolBarSlider;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.shapeeffects.*;
import com.cybernostics.lib.gui.window.CNApplication;
import com.cybernostics.lib.gui.windowcore.DialogPanel;
import com.cybernostics.lib.gui.windowcore.ScreenStack;
import com.cybernostics.lib.gui.windowcore.WebDialog;
import com.cybernostics.lib.media.image.BitmapMaker;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jasonw
 */
public class AppFrameTest
{

    private String rootName = "testLauncher";

    public void setRootName( String rootName )
    {
        this.rootName = rootName;
    }
    
    public String getRootScreenName()
    {
        return rootName;
    }

    public void registerWindows()
    {
        ScreenStack.get().register( "background", new TestWindow( "background", Color.pink, "screen2" ) );
        ScreenStack.get().register( "screen2", new TestWindow( "screen2", Color.yellow, "screen3" ) );
        ScreenStack.get().register( "screen3", new TestWindow( "screen3", Color.red, null ) );
        ScreenStack.get().register( "slider", new Slidertest( ) );
        
    }

    public void run( String arg )
    {
        registerWindows();
        ScreenStack.get().register( "testLauncher", new WindowPicker() );
        showMain( arg );
    }

    private void showMain( String arg )
    {
        Sequencer.get().start();
        CNApplication jf = new CNApplication( arg );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        GUIEventThread.show( jf );

        new WhenMadeVisible( jf )
        {

            @Override
            public void doThis( AWTEvent e )
            {
                ScreenStack.get().showScreen( getRootScreenName() );
            }

        };

    }

    static class Slidertest extends AnimatedScene
    {

        
        ToolBarSlider topSlider;
        ToolBarSlider bottomSlider;
        ToolBarSlider leftSlider;
        ToolBarSlider rightSlider;
        
        public Slidertest()
        {


            setBackgroundPainter( new ColorFill( Color.ORANGE ) );
            JButton jbClose = new JButton( "Close" );
            
            addWithinBounds( jbClose, new Rectangle2D.Double( .4, .4, .15, .08) );
            
            ShapedPanel topPanel = new ShapedPanel();
            topPanel.setBackgroundPainter( new ColorFill(Color.BLUE));
            addWithinBounds( topPanel, new Rectangle2D.Double( 0.02, 0.02, 0.96, 0.1) );
            topSlider = new ToolBarSlider( this, ComponentSprite.getFrom( topPanel ), ToolBarSlider.Dock.NORTH);
            topPanel.addMouseListener( new MouseListener() {

                @Override
                public void mouseClicked( MouseEvent e )
                {
                    //topSlider.slideInOut();
                }

                @Override
                public void mousePressed( MouseEvent e )
                {
                    
                }

                @Override
                public void mouseReleased( MouseEvent e )
                {
                    
                }

                @Override
                public void mouseEntered( MouseEvent e )
                {
                    if( topSlider.getState() == ToolBarSlider.States.OUT )
                    {
                        topSlider.slideInOut();
                    }
                }

                @Override
                public void mouseExited( MouseEvent e )
                {
                    if( topSlider.getState() == ToolBarSlider.States.IN )
                    {
                        topSlider.slideInOut();
                    }
                    
                }
            });
            
            new WhenClicked( jbClose )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    ScreenStack.get().popScreen();
                }

            };

            final JComboBox jcbWindows = new JComboBox();
            for ( String eachName : ScreenStack.get().getScreenNames() )
            {
                jcbWindows.addItem( eachName );
            }
            add( jcbWindows );

            JButton jbShow = new JButton( "Show" );
            add( jbShow );
            new WhenClicked( jbShow )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    ScreenStack.get().pushScreen( jcbWindows.getSelectedItem().toString(), null );
                }

            };

            revalidate();
            repaint();

        }

    }

    static class WindowPicker extends ShapedPanel
    {

        public WindowPicker()
        {


            setBackgroundPainter( new ColorFill( Color.ORANGE ) );
            JButton jbClose = new JButton( "Close" );
            setLayout( new FlowLayout() );
            add( jbClose );
            add( new JLabel( "testLauncher" ) );
            new WhenClicked( jbClose )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    ScreenStack.get().popScreen();
                }

            };

            final JComboBox jcbWindows = new JComboBox();
            for ( String eachName : ScreenStack.get().getScreenNames() )
            {
                jcbWindows.addItem( eachName );
            }
            add( jcbWindows );

            JButton jbShow = new JButton( "Show" );
            add( jbShow );
            new WhenClicked( jbShow )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    ScreenStack.get().pushScreen( jcbWindows.getSelectedItem().toString(), null );
                }

            };

            revalidate();
            repaint();

        }

    }

    static class TestWindow extends ShapedPanel
    {

        public TestWindow( String name, Color toPaint, final String nextScreen )
        {


            setBackgroundPainter( ShapeEffectStack.get(
                    new ImageFillEffect( BitmapMaker.createRandom( new Dimension( 50, 50 ) ) ),
                    new CompositeAdjust( 0.5 ),
                    new ColorFill( toPaint ) ) );
            JButton jbClose = new JButton( "Close" );
            setLayout( new FlowLayout() );
            add( jbClose );
            add( new JLabel( name ) );
            new WhenClicked( jbClose )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    System.out.printf( "Hide!\n" );
                    ScreenStack.get().popScreen();
                }

            };

            if ( nextScreen != null )
            {
                JButton jbNext = new JButton( nextScreen );
                new WhenClicked( jbNext )
                {

                    @Override
                    public void doThis( ActionEvent e )
                    {
                        ScreenStack.get().showScreen( nextScreen );
                    }

                };
                add( jbNext );

            }

            JButton jbDlg = new JButton( "show dialog" );
            add( jbDlg );

            new WhenClicked( jbDlg )
            {

                @Override
                public void doThis( ActionEvent e )
                {
                    ShapedPanel jpAsker = new DialogPanel( "Want to close me?","Boo",null, DialogPanel.DialogTypes.YES_NO );
                    //jpAsker.setOpaque( true );
                    jpAsker.setBackgroundPainter( new TranslucentColorFill( Color.pink, 0.3f ) );
                    WebDialog asker = new WebDialog( jpAsker, new Rectangle2D.Double( 0.2, 0.2, 0.6, 0.6 ) );
                    asker.setOpaque( true );
                    asker.setBackgroundPainter( new TranslucentColorFill( Color.black, 0.7f ) );
                    System.out.printf( "Showing dialog now...\n" );
                    asker.showDlg();
                    System.out.printf( "Showing dialog done...\n" );
                }

            };

            revalidate();
            repaint();

        }

    }

    public static void main( String[] args )
    {
        AppFrameTest afp = new AppFrameTest();
        afp.run( "AppFrametest Harness" );

    }

}
