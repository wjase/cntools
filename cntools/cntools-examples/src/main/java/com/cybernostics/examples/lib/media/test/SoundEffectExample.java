package com.cybernostics.examples.lib.media.test;

//import com.cybernostics.app.joeymail.scenes.harbour.HarbourScene;
import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackStartedListener;
import com.cybernostics.lib.animator.track.sound.SoundEffectTrack;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 *
 */
public class SoundEffectExample
{

    static boolean endNow = false;

    static SoundEffectTrack set = null;

    static SoundEffectTrack set2 = null;

    public static void main( String[] args )
    {
        try
        {
            
            //

            Finder finder = ResourcesRoot.getFinder();

            System.out.printf( "play now\n" );

            Sequencer seq = new Sequencer();

            set = new SoundEffectTrack( "G'day", new SoundEffect( finder.getResource(
                    "animations/sendreceive/sounds/bubbles2.mp3" ), 0.8 ) );
            //        set = new SoundEffectTrack( "blast", "sounds/ferryblast.mp3", 0.8 );
            //        set2 = new SoundEffectTrack( "blast", "sounds/ferryblast.mp3", 0.8 );
            //        set.startAfterMe( set2 );
            //        //set = new SoundEffectTrack( "blast", "sounds/joeygday.mp3", 0.8 );

            set.addTrackStartedListener( new TrackStartedListener()
            {

                @Override
                public void trackStarted( Track source )
                {
                    System.out.println( "At Start:" + SimpleDateFormat.getInstance().format( new Date() ) );
                    //ScalableSVGIcon svi = new ScalableSVGIcon( AppResources.getResource( "images/cloud.svg" ) );
                }

            } );

            BasicTrack.doAfter( set, new Runnable()
            {

                @Override
                public void run()
                {
                    System.out.println( "At End:" + SimpleDateFormat.getInstance().format( new Date() ) );
                    endNow = true;
                }

            } );

            seq.addAndStartTrack( set );
            try
            {
                set.await( 5000 );


            }
            catch ( TimeoutException ex )
            {
                Logger.getLogger( SoundEffectExample.class.getName() ).log( Level.SEVERE, null, ex );
            }
            catch ( InterruptedException ex )
            {
                Logger.getLogger( SoundEffectExample.class.getName() ).log( Level.SEVERE, null, ex );
            }

            System.exit( 0 );
        }
        catch ( ResourceFinderException ex )
        {
            Logger.getLogger( SoundEffectExample.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
