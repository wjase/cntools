
package com.cybernostics.examples.lib.animator.test;

import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.animator.track.sound.SpokenNumber;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 * 
 */
public class SpokenNumberTest
{
    static boolean ended = false;

    public static void main( String [] args )
    {
        try
        {
            System.out.println( System.getProperty( "java.class.path" ) );
            
            SpokenNumber sn = new SpokenNumber( 25 );

            sn.addTrackEndedListener( new TrackEndedListener()
            {

                @Override
                public void trackEnded( Track source )
                {
                    ended = true;

                }
            } );

            sn.start();

            while ( !ended )
            {
                try
                {
                    Thread.sleep( 20 );
                }
                catch ( InterruptedException e )
                {
                    UnhandledExceptionManager.handleException( e );
                }
            }

            System.exit( 0 );
        }
        catch ( ResourceFinderException ex )
        {
            Logger.getLogger( SpokenNumberTest.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
