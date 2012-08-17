package com.cybernostics.lib.media;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.logging.AppLog;
import com.cybernostics.lib.media.sound.SoundLoader;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

/**
 * Wrapper for an audio clip adding easy volume control
 *
 * @author jasonw
 *
 */
public class SoundEffect implements LineListener
{

	/**
	 *
	 */
	public static final String STOPPED = "STOPPED";

	public static URL getResource( Finder loader, String resPath )
		throws ResourceFinderException
	{
		URL resURL = null;

		resURL = loader.getResource( resPath );

		if (resURL == null)
		{
			int length = resPath.length();
			// if the location doesn't end in .XXX, try appending common 
			// file extensions
			if (length > 4 && ( resPath.charAt( length - 3 ) != '.' ))
			{
				// try all common sound file extensions...
				for (String eachExtension : SoundEffect.soundFileExtensions)
				{
					resURL = loader.getResource( resPath + eachExtension );

					// Found it?
					if (resURL != null)
					{
						// Append the correct extension
						resPath += eachExtension;
						break; // Got one! now load the clip
					}
				}
			}
		}
		// not found?
		if (resURL == null)
		{
			AppLog.getLogger()
				.log(
					Level.WARNING,
					String.format(
						"No URL for %s",
						resPath ) );
		}
		return resURL;

	}

	private PropertyChangeSupport changes = new PropertyChangeSupport( this );

	public static SoundEffect play( URL toPlay, double volume )
		throws ResourceFinderException
	{
		SoundEffect effect = new SoundEffect( toPlay, volume );
		effect.play();
		return effect;
	}

	public static SoundEffect getEffect( ResourceFinder finder, String toLoad )
		throws ResourceFinderException
	{
		SoundEffect se = new SoundEffect( finder.getResource( toLoad ), 0.7 );
		return se;
	}

	public void addPropertyChangeListener( String id,
		PropertyChangeListener listener )
	{
		changes.addPropertyChangeListener(
			id,
			listener );
	}

	private Clip clip = null;

	private FloatControl volControl;

	private float volRange;

	private double volume = 1f;

	private int loopCount = 1;

	private URL locationURL;

	public static final int LOOP = Clip.LOOP_CONTINUOUSLY;

	WatchableWorkerTask soundLoad = null;

	public WatchableWorkerTask getLoadTask()
	{
		if (soundLoad == null)
		{
			soundLoad = new WatchableWorkerTask( "Sound Load:"
				+ locationURL.toString() )
			{

				@Override
				protected Object doTask() throws Exception
				{
					loadClip();

					getClip().addLineListener(
						SoundEffect.this );

					setClipVolume( volume );

					return clip;
				}

			};

			soundLoad.start();
		}

		return soundLoad;
	}

	public SoundEffect( URL location )
	{
		this( location, SoundParameters.getDefault() );
	}

	public SoundEffect( URL location, double volume )
	{
		setLocation( location );
		setVolume( volume );
	}

	public SoundEffect( URL toLoad, SoundParameters params )
	{
		setLocation( toLoad );
		apply( params );
	}

	private void apply( SoundParameters params )
	{
		setVolume( params.getVolume() );
		setLoopCount( params.getLoopCount() );
	}

	//    public SoundEffect( String location, double volume, ResourceFinder loader ) throws ResourceFinderException
	//    {
	//        setLoader( loader );
	//        setLocation( location );
	//        setVolume( volume );
	//    }
	public void setLocation( URL location )
	{
		this.locationURL = location;
		//        this.location = loader.getRelativePath( location );
		getLoadTask();
	}

	public boolean isPlaying()
	{
		if (!getLoadTask().isDone())
		{
			return false;
		}

		if (clip == null)
		{
			return false;
		}

		return getClip().isRunning();
	}

	public void play()
	{
		try
		{
			getLoadTask().get();

			if (clip != null)
			{
				setClipVolume( this.volume );

				if (loopCount > 0)
				{
					clip.loop( loopCount - 1 );
				}
				else
					if (loopCount == Clip.LOOP_CONTINUOUSLY)
					{
						clip.loop( loopCount );
					}
					else
					{
						clip.loop( 0 );
					}

			}
			else
			{
				UnhandledExceptionManager.handleException( new Exception( "No clip for "
					+ getFileName() ) );
			}
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				SoundEffect.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		catch (ExecutionException ex)
		{
			Logger.getLogger(
				SoundEffect.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	private static String[] soundFileExtensions =
	{ ".mp3", ".wav", ".ogg" };

	/**
	 *
	 */
	private void loadClip()
	{
		// not found?
		if (locationURL == null)
		{
			return;
		}
		try
		{
			//            if ( clip != null )
			//            {
			//                //clip.loop( 0 );
			//                clip.stop();
			//                clip.removeLineListener( this );
			//
			//                clip = null;
			//            }
			if (clip == null)
			{
				clip = SoundLoader.loadClip( locationURL );

				if (clip != null)
				{
					getClip().addLineListener(
						this );
					setVolume( volume );
				}

			}

		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}

	}

	private Clip getClip()
	{
		if (clip == null)
		{
			try
			{
				getLoadTask().get();
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(
					SoundEffect.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			catch (ExecutionException ex)
			{
				Logger.getLogger(
					SoundEffect.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}

		return clip;
	}

	public int getLoopCount()
	{
		return loopCount;
	}

	public void setLoopCount( int loopCount )
	{
		this.loopCount = loopCount;
	}

	public void setVolume( double volume )
	{
		this.volume = volume;

		setClipVolume( this.volume );
	}

	public void setClipVolume( double volume )
	{
		if (clip == null)
		{
			return;
		}

		if (volControl == null)
		{
			volControl = (FloatControl) getClip().getControl(
				Type.MASTER_GAIN );
			volRange = volControl.getMaximum() - volControl.getMinimum();
		}
		try
		{
			volControl.setValue( (float) ( volControl.getMinimum() + ( volume * volRange ) ) );
		}
		catch (Exception e)
		{
			System.out.printf(
				"vol min %f, vol max %f\n",
				volControl.getMinimum(),
				volControl.getMaximum() );
			UnhandledExceptionManager.handleException( e );
		}

	}

	public void stop()
	{
		Clip toStop = getClip();
		if (toStop != null)
		{
			//            if ( loopCount == SoundEffect.LOOP )
			//            {
			//                toStop.loop( 0 );
			//            }
			toStop.loop( 0 );
			toStop.stop();
			toStop.setFramePosition( 0 );
		}

	}

	public void pause()
	{
		Clip toStop = getClip();
		if (toStop != null)
		{
			toStop.loop( 0 );
			toStop.stop();
		}

	}

	public void resume()
	{
		play();
	}

	@Override
	public void update( LineEvent lineEvent )
	// called when the clip's line detects open, close, start, stop events
	{
		// has the clip has reached its end?
		if (lineEvent.getType() == LineEvent.Type.STOP)
		{
			stop();
			// lineEvent.getLine().close();
			changes.firePropertyChange(
				STOPPED,
				false,
				true );
		}
	} // end of update()

	/**
	 * @return
	 */
	public PropertyChangeSupport getPropertySupport()
	{
		return changes;
	}

	/**
	 * @return the duration in milliseconds
	 */
	public long getDuration()
	{
		return getClip().getMicrosecondLength() / 1000;
	}

	//    /**
	//     * @param loader
	//     *            the loader to set
	//     */
	//    public void setLoader( ResourceFinder loader )
	//    {
	//        if ( loader != null )
	//        {
	//            this.loader = loader;
	//        }
	//    }
	//    /**
	//     * @return the loader
	//     */
	//    public ResourceFinder getLoader() throws Exception
	//    {
	//        if ( loader == null )
	//        {
	//            throw new Exception( "Must set loader for sound effects" );
	//        }
	//        return loader;
	//    }
	public String getFileName()
	{
		return locationURL.getFile();
	}

	public URL getLocation()
	{
		return locationURL;
	}

}
