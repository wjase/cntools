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

import com.cybernostics.lib.concurrent.DurationRequiredException;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.exceptions.AppExceptionManager;
import com.cybernostics.lib.resourcefinder.Finder;

/**
 * A frame animator which switches frame elements at specified instants.
 *
 * To use this you need an audio clip and a PositionSequence. To create a new
 * sequence: Open the clip in an audio editor (eg audacity)
 *
 * Create a new PositionSequence file matching the chunks of sound in the audio
 * clip with the character position corresponding to that sound.
 *
 * @author jasonw
 *
 */
public class TimedEventTrack extends BasicTrack
{

	private TimedEventSource timedEvents;

	private TimedEvent currentEvent;

	private Finder loader = null;

	/**
	 *
	 * @return
	 */
	public Finder getLoader()
	{
		return loader;
	}

	/**
	 *
	 * @param loader
	 */
	public void setLoader( Finder loader )
	{
		this.loader = loader;
	}

	private String defaultPosition = "";

	/**
	 *
	 * @return
	 */
	public String getDefaultPosition()
	{
		return defaultPosition;
	}

	/**
	 *
	 * @param defaultPosition
	 */
	public void setDefaultPosition( String defaultPosition )
	{
		this.defaultPosition = defaultPosition;
	}

	private Object targetObject = null;

	/**
	 *
	 * @param o
	 */
	public void setEventTarget( Object o )
	{
		this.targetObject = o;
	}

	/**
	 *
	 * @return
	 */
	public Object getEventTarget()
	{
		return targetObject;
	}

	/**
	 * Sets the source of animation events corresponding to the audio
	 *
	 * @param eventSource
	 */
	public void setTimedEventSource( TimedEventSource eventSource )
	{
		timedEvents = eventSource;
	}

	/**
	 *
	 * @return
	 */
	public TimedEventSource getPositionEventSource()
	{
		return timedEvents;
	}

	/**
	 *
	 * @param name
	 */
	public TimedEventTrack( String name )
	{
		super( name );
	}

	/**
	 * Used to calculate the elapsed and match it to an event
	 */
	private TimeStamp timeCode = new TimeStamp();

	@Override
	public void start()
	{
		super.start();
		if (timedEvents != null)
		{
			timeCode.start();
			timedEvents.reset();
			currentEvent = timedEvents.getNext();
		}
	}

	@Override
	public void pause()
	{
		timeCode.pause();
	}

	@Override
	public void resume()
	{
		timeCode.resume();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.animator.track.BasicTrack#stop()
	 */
	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		if (!isStopped())
		{
			super.stop( fireEvents );
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.lib.animator.track.Track#update(com.cybernostics.lib
	 * .animator.TimeStamp)
	 */
	@Override
	public void update( TimeStamp timeCode )
	{
		if (timedEvents == null)
		{
			return;
		}

		if (currentEvent == null)
		{
			try
			{
				currentEvent = timedEvents.getNext();
			}
			catch (Exception e)
			{
				return;
			}
		}

		try
		{
			this.timeCode.setCurrent( timeCode );
		}
		catch (DurationRequiredException ex)
		{
			AppExceptionManager.handleException(
				ex,
				getName() );
		}

		long elapsed = this.timeCode.getElapsed();
		if (currentEvent == null)
		{
			return;
		}
		if (elapsed > currentEvent.getTimeStamp())
		{
			// check forward in case we've skipped a frame within the update
			// cycle
			long nextEventTime = currentEvent.getTimeStamp();
			while (!hasEnded() && elapsed > nextEventTime)
			{
				if (currentEvent != null)
				{
					currentEvent.execute(
						this,
						getEventTarget() );
				}

				currentEvent = timedEvents.getNext();

				if (currentEvent == null) // no more
				{
					TimedEvent post = timedEvents.getPost();
					if (post != null)
					{
						post.execute(
							this,
							getEventTarget() );
					}
					stop( true );
					currentEvent = null;
					break;
				}
				nextEventTime = currentEvent.getTimeStamp();
			}
		}
	}

}
