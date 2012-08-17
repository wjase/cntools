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

import com.cybernostics.lib.animator.track.ordering.*;
import com.cybernostics.lib.concurrent.ConcurrentSet;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.exceptions.StackString;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.regex.Regex;
import java.awt.EventQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implements some common methods for all tracks like listeners and sequencer
 * and name Inherit from this and then implement start() and update()
 *
 * Remember to call firestarted and firestopped methods so othe tracks can build
 * on this.
 *
 * @author jasonw
 *
 */
public abstract class BasicTrack implements Track
{

	private static boolean showStack = false;

	public static void setShowStack( boolean showStack )
	{
		BasicTrack.showStack = showStack;
	}

	private static boolean debug = false;

	public static void setDebug( boolean debug )
	{
		BasicTrack.debug = debug;
	}

	private static Regex debugFilter = null;

	public static void setDebugFilter( Regex debugFilter )
	{
		BasicTrack.debugFilter = debugFilter;
	}

	private static final String dbgPattern = "Track %s %s.\n%s";

	TrackEndedListener endedDebug = new TrackEndedListener()
	{

		@Override
		public void trackEnded( Track source )
		{
			String trackName = source.getName();
			if (debugFilter == null || debugFilter.find( trackName ))
			{
				System.out.println( String.format(
					dbgPattern,
					trackName,
					"ended",
					showStack ? StackString
						.get( "cybernostics" ) : "" ) );

			}
		}

	};

	TrackStartedListener startedDebug = new TrackStartedListener()
	{

		@Override
		public void trackStarted( Track source )
		{
			String trackName = source.getName();
			if (debugFilter == null || debugFilter.find( trackName ))
			{
				System.out.println( String.format(
					dbgPattern,
					trackName,
					"started",
					showStack ? StackString
						.get( "cybernostics" ) : "" ) );
			}
		}

	};

	public BasicTrack( String sName )
	{
		this.name = sName;
		if (debug)
		{
			addTrackEndedListener( endedDebug );
			addTrackStartedListener( startedDebug );
		}
	}

	@Override
	public void removeTrackStartedListener( TrackStartedListener trackEndedListener )
	{
		startListeners.remove( trackEndedListener );
	}

	private String name;
	// Listeners for track events or state

	private final ConcurrentSet< TrackStartedListener > startListeners = new ConcurrentSet< TrackStartedListener >();

	private final ConcurrentSet< TrackEndedListener > endListeners = new ConcurrentSet< TrackEndedListener >();
	//private final ConcurrentLinkedQueue<TrackEndedListener> endListeners = new ConcurrentLinkedQueue<TrackEndedListener>();

	private final ConcurrentSet< TrackUpdatedListener > updateListeners = new ConcurrentSet< TrackUpdatedListener >();

	private boolean stopped = false;

	private boolean running = false;

	/**
	 *
	 * @return
	 */
	@Override
	public boolean isRunning()
	{
		return running;
	}

	// This back-pointer to parent allows this track to add others

	private TrackContainer sequencer;

	/**
	 *
	 * @param millisDelay
	 * @param aTrack
	 * @param toRun
	 */
	public static void doAfter( long millisDelay,
		final Track aTrack,
		final Runnable toRun )
	{
		WaitTrack wt = new WaitTrack( millisDelay, null );
		startAfter(
			aTrack,
			wt );
		doAfter(
			wt,
			toRun );
	}

	/**
	 *
	 * @param aTrack
	 * @param toRun
	 */
	public static void doAfter( final Track aTrack, final Runnable toRun )
	{
		if (aTrack == null)
		{
			toRun.run();
			return;
		}
		aTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				aTrack.removeTrackEndedListener( this );
				toRun.run();

			}

		} );
	}

	public static BasicTrack delay( BasicTrack toRun, long delayTimeMillis )
	{
		WaitTrack wt = new WaitTrack( delayTimeMillis, null );
		wt.startAfterMe( toRun );
		return wt;
	}

	/**
	 *
	 * @param event
	 * @param aTrack
	 * @param nextTrack
	 * @param millisDelay
	 */
	public static Track startAfter( Track aTrack,
		Track nextTrack,
		long millisDelay )
	{
		WaitTrack wt = new WaitTrack( millisDelay, null );
		startAfter(
			aTrack,
			wt );
		wt.startAfterMe( nextTrack );
		return nextTrack;
	}

	/**
	 *
	 * @param event
	 * @param aTrack
	 * @param nextTrack
	 */
	public static Track startAfter( Track aTrack, BasicTrack nextTrack )
	{
		aTrack.addTrackEndedListener( new TrackAdder( aTrack, nextTrack ) );
		return nextTrack;
	}

	/**
	 *
	 * @param event
	 * @param aTrack
	 * @param nextTrack
	 */
	public static Track startWith( Track aTrack, BasicTrack nextTrack )
	{
		aTrack.addTrackStartedListener( new TrackAdder( aTrack, nextTrack ) );
		return nextTrack;
	}

	/**
	 *
	 * @param event
	 * @param aTrack
	 * @param nextTrack
	 */
	public static void endAfter( String event,
		Track aTrack,
		BasicTrack nextTrack )
	{
		aTrack.addTrackEndedListener( new TrackStopper( nextTrack ) );
	}

	@Override
	public final void addTrackEndedListener( TrackEndedListener target )
	{
		endListeners.add( target );
	}

	@Override
	public final void addTrackStartedListener( TrackStartedListener target )
	{
		startListeners.add( target );
	}

	@Override
	public final void addTrackUpdatedListener( TrackUpdatedListener target )
	{
		updateListeners.add( target );

	}

	@Override
	public Track connectStart( Track toAdd )
	{
		addTrackStartedListener( new TrackAdder( this, toAdd ) );
		return toAdd;
	}

	@Override
	public Track connectEnd( Track toAdd )
	{
		addTrackEndedListener( new TrackAdder( this, toAdd ) );
		return toAdd;
	}

	/**
	 * Start the specified track after this one ends
	 *
	 * @param newTrack
	 */
	public void startAfterMe( Track newTrack )
	{
		connectEnd( newTrack );
	}

	/**
	 * Starts the specified track after this one starts
	 *
	 * @param newTrack
	 */
	public void startWithMe( Track newTrack )
	{
		connectStart( newTrack );
	}

	/**
	 *
	 * @param newTrack
	 */
	public void stopAfterMe( Track newTrack )
	{
		addTrackEndedListener( new TrackStopper( newTrack ) );
	}

	/**
	 *
	 */
	public void fireTrackUpdated( TimeStamp now )
	{
		if (updateListeners.isEmpty())
		{
			return;
		}

		for (TrackUpdatedListener eachListener : updateListeners)
		{
			eachListener.trackUpdated(
				now,
				this );
		}
	}

	@Override
	public void start()
	{
		if (!running)
		{
			stopped = false;
			running = true;
			fireTrackStarted();
		}
	}

	/**
	 *
	 */
	public void fireTrackStarted()
	{
		if (startListeners.isEmpty())
		{
			return;
		}
		for (final TrackStartedListener eachListener : startListeners)
		{
			//            GUIEventThread.run( new Runnable()
			//            {
			//
			//                @Override
			//                public void run()
			//                {
			eachListener.trackStarted( BasicTrack.this );
			//                }
			//
			//            } );
		}
	}

	CountDownLatch allDone = new CountDownLatch( 1 );

	/**
	 *
	 */
	public void fireTrackEnded()
	{
		if (!endListeners.isEmpty())
		{
			//			if (!toRemoveEndListeners.isEmpty())
			//			{
			//				for (TrackEndedListener eachToRemove : toRemoveEndListeners)
			//				{
			//					endListeners.remove( eachToRemove );
			//				}
			//                toRemoveEndListeners.clear();
			//			}

			for (TrackEndedListener eachListener : endListeners)
			{
				try
				{
					eachListener.trackEnded( BasicTrack.this );
				}
				catch (Exception e)
				{
					throw new Error( e );
				}
			}
			//   			if (!toRemoveEndListeners.isEmpty())
			//			{
			//				for (TrackEndedListener eachToRemove : toRemoveEndListeners)
			//				{
			//					endListeners.remove( eachToRemove );
			//				}
			//                toRemoveEndListeners.clear();
			//			}

		}

		allDone.countDown();
	}

	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		if (running)
		{
			running = false;
			stopped = true;
			if (fireEvents)
			{
				EventQueue.invokeLater( new Runnable()
				{

					@Override
					public void run()
					{
						fireTrackEnded();
					}
				} );

			}

		}

	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped()
	{
		return stopped;
	}

	/**
	 * @return the sequencer
	 */
	@Override
	public TrackContainer getSequencer()
	{
		return sequencer;
	}

	@Override
	public void setSequencer( TrackContainer theSequencer )
	{
		sequencer = theSequencer;
	}

	@Override
	public boolean hasEnded()
	{
		return isStopped();
	}

	/**
	 * @param trackEndedListener
	 */
	@Override
	public void removeTrackEndedListener( final TrackEndedListener trackEndedListener )
	{
		this.endListeners.remove( trackEndedListener );
		//toRemoveEndListeners.add( trackEndedListener );
	}

	public void waitForAllListeners( long timeout ) throws InterruptedException
	{
		allDone.await(
			timeout,
			TimeUnit.MILLISECONDS );
	}

	private SingletonInstance< String > uniqueName = new SingletonInstance< String >()
	{

		@Override
		protected String createInstance()
		{
			return String.format(
				"%s (%d)",
				getName(),
				System.identityHashCode( BasicTrack.this ) );
		}

	};

	@Override
	public String toString()
	{
		return uniqueName.get();
	}

	/**
	 * Resets running state call before adding to Sequencer if being reused.
	 */
	public void reset()
	{
		stopped = false;
		running = false;
		allDone = new CountDownLatch( 1 );
		sequencer = null;
		atEndEvent.get()
			.reset();
	}

	private SingletonInstance< TrackEndedEvent > atEndEvent = new SingletonInstance< TrackEndedEvent >()
	{

		@Override
		protected TrackEndedEvent createInstance()
		{
			return new TrackEndedEvent( BasicTrack.this );
		}
	};

	public void await( long timeout ) throws InterruptedException,
		TimeoutException
	{
		if (hasEnded())
		{
			return;
		}
		TrackEndedEvent event = atEndEvent.get();

		addTrackEndedListener( event );
		TimeStamp ts = new TimeStamp();
		ts.start();
		waitForAllListeners( timeout );
		event.await( timeout - ts.getElapsed() );

	}

}
