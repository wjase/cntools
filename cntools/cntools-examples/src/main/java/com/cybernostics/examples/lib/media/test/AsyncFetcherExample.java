
package com.cybernostics.examples.lib.media.test;

import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;

import com.cybernostics.lib.concurrent.WorkerDoneListener;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.media.AsyncImageFetcher;
import com.cybernostics.lib.resourcefinder.ResourceFinder;

/**
 * @author jasonw
 * 
 */
public class AsyncFetcherExample
{

    public static void main( String[] args )
    {
        final JFrame jf = new JFrame( "SpriteTest" );

        JButton jbRefresh = new JButton( "Refresh" );

        

        GroupLayoutPlus dgl = new GroupLayoutPlus( jf.getContentPane() );
        final JLabel resultLabel = new JLabel();

        new WhenClicked( jbRefresh )
        {

            @Override
            public void doThis( ActionEvent e )
            {
                try
                {
                    resultLabel.setIcon( null );
                    resultLabel.setText( "Loading..." );
                    jf.pack();

                    AsyncImageFetcher fetcher = new AsyncImageFetcher( ResourcesRoot.getFinder().getResource( "com/cybernostics/lib/media/images/Echidna.png" ) );

                    fetcher.addWorkerDoneListener( new WorkerDoneListener()
                    {

                        public void taskDone( java.util.concurrent.Future< Object> completed )
                        {
                            try
                            {
                                resultLabel.setText( null );
                                resultLabel.setIcon( new ImageIcon( ( BufferedImage ) completed.get() ) );
                                jf.pack();
                            }
                            catch ( InterruptedException e )
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            catch ( ExecutionException e )
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } );
                    fetcher.start();
                }
                catch ( ResourceFinderException ex )
                {
                    Logger.getLogger( AsyncFetcherExample.class.getName() ).log( Level.SEVERE, null, ex );
                }

            }
        };

        dgl.setVerticalGroup( SEQUENTIAL.group( resultLabel, PARALLEL.group( jbRefresh ) ) );
        dgl.setHorizontalGroup( PARALLEL.group( Alignment.CENTER, resultLabel, SEQUENTIAL.group( jbRefresh ) ) );
        jf.pack();
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setVisible( true );

    }
}
