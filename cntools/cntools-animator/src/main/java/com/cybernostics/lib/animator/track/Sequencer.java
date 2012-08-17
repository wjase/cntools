/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.animator.track;

import com.cybernostics.lib.animator.track.ordering.TrackContainer;
import com.cybernostics.lib.animator.track.ordering.UpdateCompleteListener;
import com.cybernostics.lib.concurrent.AnimationTimer;
import com.cybernostics.lib.concurrent.AnimationTimerListener;
import com.cybernostics.lib.concurrent.ConcurrentSet;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.RepaintListener;
import com.cybernostics.lib.gui.declarative.events.SupportsPropertyChanges;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.EventQueue;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 *
 */
public class Sequencer implements TrackContainer, SupportsPropertyChanges
{

	private static SingletonInstance< Sequencer > animateSequencer = new SingletonInstance< Sequencer >()
	{

		@Override
		protected Sequencer createInstance()
		{
			return new Sequencer();
		}

	};

	public static Sequencer get()
	{
		return animateSequencer.get();
	}

	private ConcurrentSet< UpdateCompleteListener > listeners = new ConcurrentSet< UpdateCompleteListener >();

	public void addUpdateCompleteListener( UpdateCompleteListener listener )
	{
		listeners.add( listener );
	}

	public void removeUpdateCompleteListener( UpdateCompleteListener listener )
	{
		listeners.remove( listener );
	}

	public void fireUpdateCompleted( TimeStamp timeNow )
	{
		for (UpdateCompleteListener eachListener : listeners)
		{
			eachListener.update( timeCode );
		}
	}

	void remove( Track eachTrack )
	{
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	@Override
	public void resume()
	{
		if (state == SequenceState.PAUSED)
		{
			state = SequenceState.RUNNING;
		}
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		changeSupport.addPropertyChangeListener( listener );
	}

	@Override
	public void addPropertyChangeListener( String propertyName,
		PropertyChangeListener listener )
	{
		changeSupport.addPropertyChangeListener(
			propertyName,
			listener );
	}

	@Override
	public void removePropertyChangeListener( PropertyChangeListener listener )
	{
		changeSupport.removePropertyChangeListener( listener );
	}

	@Override
	public void clear()
	{
		tracks.clear();
		pendingTracks.clear();
	}

	public enum SequenceState
	{

		NOT_STARTED, RUNNING, PAUSED, STOPPING, STOPPED
	}

	private SequenceState state = SequenceState.NOT_STARTED;

	private boolean stopWhenDone = false;

	public void setStopWhenDone( boolean stopWhenDone )
	{
		this.stopWhenDone = stopWhenDone;
	}

	public boolean isStopWhenDone()
	{
		return stopWhenDone;
	}

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport( this );

	/**
	 * This list contains the current set of active tracks being updated each
	 * animation period
	 */
	//private ArrayList< Track> tracks = new ArrayList< Track>();
	private ConcurrentLinkedQueue< Track > tracks = new ConcurrentLinkedQueue< Track >();

	/**
	 * This list serves two purposes i) During updates active tracks are copied
	 * from the tracks list, leaving stopped tracked behind for garbage
	 * collection when that list is cleared. This then gets swapped into the
	 * tracks list for the next update() ii) Tracks added during an update loop
	 * are added to this array to prevent adding to a list which we are
	 * iterating.
	 */
	private ConcurrentLinkedQueue< Track > pendingTracks = new ConcurrentLinkedQueue< Track >();
	//private ArrayList< Track> pendingTracks = new ArrayList< Track>();

	/**
	 *
	 */
	// private Timer animTimer = null;;
	//private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool( 1 );
	//ScheduledFuture< ?> animTaskHandle = null;
	AnimationTimerListener updater = new AnimationTimerListener()
	{

		@Override
		public void update( long milliTime )
		{
			timeCode.update( milliTime );
			updateTracks();
		}

	};

	/**
	 * This is a nominal timestamp set at the start of each update() cycle. The
	 * idea is that tracks which don't require supreme accuracy can use this
	 * rather than hammering System.currentTimeMillis()
	 *
	 */
	private TimeStamp timeCode;

	/**
	 * This is the GUI component with a repaint method which gets called after
	 * each update. The actual repaints is done in Swing's own sweet time... via
	 * paintComponent
	 */
	private RepaintListener theRenderer;

	/**
	 * Sets the object which will actually paint the updated scene
	 *
	 * @param theRenderer
	 */
	public void setRepaintListener( RepaintListener theRenderer )
	{
		this.theRenderer = theRenderer;
	}

	/**
	 * creates an animation Sequencer
	 *
	 */
	public Sequencer()
	{
	}

	boolean isDoingUpdate = false;

	// Current Track being processed
	Track currentTrack = null;

	/**
	 *
	 */
	protected void updateTracks()
	{
		if (( state == SequenceState.STOPPED )
			|| ( state == SequenceState.PAUSED ))
		{
			return;
		}
		if (isDoingUpdate)
		{
			return; // already doing it!
		}

		if (pendingTracks.isEmpty() && tracks.isEmpty())
		{
			return;
		}
		// synchronized ( lock )
		// {
		isDoingUpdate = true;
		while (!tracks.isEmpty())
		{

			currentTrack = tracks.remove();// tracks.get( index );

			if (currentTrack != null)
			{
				if (!currentTrack.hasEnded())
				{
					try
					{
						currentTrack.update( timeCode );
					}
					catch (Exception e)
					{
						UnhandledExceptionManager.handleException( e );
						currentTrack.stop( false );
					}
					pendingTracks.add( currentTrack );
				}

			}
		}

		if (!pendingTracks.isEmpty())
		{
			// Swap the surviving tasks copied from the survivors array
			// The current tasks array then becomes the next survivors array
			ConcurrentLinkedQueue< Track > tempSwap = pendingTracks;
			pendingTracks = tracks; // this now becomes the next survivors list
			tracks = tempSwap; // copy the remaining tracks

		}

		isDoingUpdate = false;
		if (tracks.isEmpty() && isStopWhenDone())
		{
			this.stop();
		}

		fireUpdateCompleted( timeCode );
		// }

		if (theRenderer != null)
		{
			theRenderer.repaint();
		}
	}

	public void remove( String trackName )
	{
		for (Track eachTrack : tracks)
		{
			if (eachTrack.getName()
				.equals(
					trackName ))
			{
				eachTrack.stop( false );
				break;
			}
		}
	}

	/**
	 * Calls start on all tasks in the tasks list
	 */
	public void start()
	{
		AnimationTimer.startTimer();

		if (state == SequenceState.NOT_STARTED
			|| state == SequenceState.STOPPED)
		{
			timeCode = new TimeStamp();
			timeCode.start();

			if (!isDoingUpdate)
			{
				isDoingUpdate = true;
				for (Track eachTrack : tracks)
				{
					TrackContainer tc = eachTrack.getSequencer();
					if (tc == null)
					{
						eachTrack.setSequencer( this );

					}
					eachTrack.start();
				}
				isDoingUpdate = false;
			}

			state = SequenceState.RUNNING;
			AnimationTimer.addListener( updater );

		}

	}

	/*
	 * Adds the specified track either to the tracks list if update is not in
	 * progress or to pendingTracks if it is to prevent modifying an array which
	 * is being iterated. (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.animator.AnimationTrackContainer#addTrack(com.cybernostics
	 * .animator.AnimationTrack)
	 */
	@Override
	public void addTrack( final Track aTrack )
	{
		TrackContainer tc = aTrack.getSequencer();
		if (tc == null)
		{
			aTrack.setSequencer( this );

		}
		if (!isDoingUpdate)
		{
			tracks.add( aTrack );
		}
		else
		{
			pendingTracks.add( aTrack );
		}

	}

	@Override
	public void addAndStartTrack( final Track track )
	{
		if (state == SequenceState.STOPPED)
		{
			// don't add tracks when this is stopping...
			return;
		}

		if (state == SequenceState.NOT_STARTED)
		{
			try
			{
				start();
			}
			catch (Exception ex)
			{
				throw new RuntimeException( ex );
			}
		}
		addTrack( track );

		EventQueue.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				track.start();
			}
		} );

	}

	/**
	 * Force the sequencer to cease and desist all tracks
	 */
	public void stop()
	{
		state = SequenceState.STOPPING;

		isDoingUpdate = true;

		for (Track eachTrack : tracks)
		{
			if (eachTrack != null)
			{
				eachTrack.stop( false );
			}
		}

		if (currentTrack != null)
		{
			currentTrack.stop( false );
		}

		for (Track eachTrack : pendingTracks)
		{
			if (eachTrack != null)
			{
				eachTrack.stop( false );
			}
		}

		AnimationTimer.removeListener( updater );
		isDoingUpdate = false;
		changeSupport.firePropertyChange(
			"RUNNING",
			true,
			false );
		state = SequenceState.STOPPED;
		allDone.countDown();

	}

	public boolean isRunning()
	{
		return state == SequenceState.RUNNING;
	}

	private CountDownLatch allDone = new CountDownLatch( 1 );

	/**
	 *
	 */
	public void waitTillDone( long timeoutMillies ) throws TimeoutException
	{
		setStopWhenDone( true );

		if (state == SequenceState.STOPPED)
		{
			return;
		}
		try
		{
			allDone.await(
				timeoutMillies,
				TimeUnit.MILLISECONDS );
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				Sequencer.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		if (state != SequenceState.STOPPED)
		{
			throw new TimeoutException();
		}

	}

	public void pause()
	{
		state = SequenceState.PAUSED;
	}

}
