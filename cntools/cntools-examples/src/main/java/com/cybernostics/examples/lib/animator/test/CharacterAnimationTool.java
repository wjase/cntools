package com.cybernostics.examples.lib.animator.test;

import com.cybernostics.lib.animator.track.characteranimate.DisplayedElementsListener;
import com.cybernostics.lib.animator.track.characteranimate.PartPositionPath;
import com.cybernostics.lib.animator.track.characteranimate.SVGArticulatedIcon;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Filename   : CharacterAnimation.java
 * Created By : jasonw
 * Description: see class description below
 *
 * Copyright Cybernostics Australia 2009
 */
/**
 * @author jasonw
 * 
 */
public class CharacterAnimationTool
{

    public static SVGArticulatedIcon faceIcon = null;
    public static JFileChooser jfc = new JFileChooser();
    static DefaultListModel dlm = new DefaultListModel();

    public static void main( String[] args )
    {
        try
        {
            

            faceIcon = SVGArticulatedIcon.get( AppResources.getResource( "images/head.svg" ) );

            final JButton jbLoadImage = new JButton( "Load" );

            new WhenClicked( jbLoadImage )
            {
                /*
                 * (non-Javadoc)
                 * 
                 * @see
                 * com.cybernostics.lib.gui.declarative.events.WhenClicked#doThis
                 * (java.awt.event.ActionEvent)
                 */

                @Override
                public void doThis( ActionEvent e )
                {
                    int result = jfc.showOpenDialog( new JPanel() );

                    // if we selected an image, load the image
                    if ( result == JFileChooser.APPROVE_OPTION )
                    {
                        try
                        {
                            URL fileURL = jfc.getSelectedFile().toURI().toURL();
                            System.out.printf( "%s\n", fileURL );
                            URI uri = faceIcon.getSvgUniverse().loadSVG( fileURL );
                            faceIcon.setSvgURI( uri );
                            dlm.clear();
                            Set< PartPositionPath> elements = faceIcon.getAnimatedElements();
                            for ( PartPositionPath eachPath : elements )
                            {
                                dlm.addElement( eachPath.getPath() );
                            }
                        }
                        catch ( MalformedURLException e1 )
                        {
                            e1.printStackTrace();
                        }
                    }
                }
            };

            final JList positions = new JList();
            Set< PartPositionPath> elements = faceIcon.getAnimatedElements();

            for ( PartPositionPath eachPath : elements )
            {
                dlm.addElement( eachPath.getPath() );
            }

            positions.setModel( dlm );

            //faceIcon.setScaleToFit( true );
            // faceIcon.setPreferredSize( new Dimension(200,200) );

            JFrame jf = new JFrame( "Head Test" );
            jf.setSize( 600, 400 );
            jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            jf.setVisible( true );

            BoxLayout bl = new BoxLayout( jf.getContentPane(), BoxLayout.Y_AXIS );

            final JLabel jb = new JLabel( faceIcon );
            jf.getContentPane().setLayout( bl );
            jf.getContentPane().add( jbLoadImage );
            jf.getContentPane().add( jb );
            jf.getContentPane().add( new JScrollPane( positions ) );

            faceIcon.addDisplayedElementsListener( new DisplayedElementsListener()
            {

                @Override
                public void displayedElementChanged()
                {
                    jb.repaint();
                }
            } );

//            faceIcon.addPropertyChangeListener( new PropertyChangeListener()
//            {
//
//                @Override
//                public void propertyChange( PropertyChangeEvent evt )
//                {
//                    jb.repaint();
//
//                }
//            } );

            positions.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

            positions.addListSelectionListener( new ListSelectionListener()
            {

                @Override
                public void valueChanged( ListSelectionEvent e )
                {
                    if ( !e.getValueIsAdjusting() )
                    {
                        Object[] selections = positions.getSelectedValues();
                        for ( Object eachPos : selections )
                        {
                            faceIcon.changePosition( eachPos.toString() );
                        }
                    }
                }
            } );
        }
        catch ( URISyntaxException ex )
        {
            Logger.getLogger( CharacterAnimationTool.class.getName() ).log( Level.SEVERE, null, ex );
        }

    }
}
