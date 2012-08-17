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

import com.cybernostics.lib.animator.ui.transitions.EasingFunction;
import com.cybernostics.lib.concurrent.DurationRequiredException;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.exceptions.AppExceptionManager;

/**
 * @author jasonw
 *
 */
public abstract class BasicTimerTrack extends BasicTrack
{

	private TimeStamp timeCode = new TimeStamp();

	private EasingFunction easing = EasingFunction.linear.get();

	/*
	 * This allows you to get all kinds of whacky transitions as t moves from
	 * 0.0 to 1.0, rather than just a straight linear transition. It also allows
	 * ease out(reverse) transitions, so the same Function can be used to ease
	 * in and ease out.
	 *
	 * The default easing is EasingFunction.linear
	 *
	 */
	public BasicTimerTrack setTransition( EasingFunction easing )
	{
		this.easing = easing;
		return this; // allow this function call to be chained
	}

	public TimeStamp getTimeCode()
	{
		return timeCode;
	}

	/**
	 *
	 *
	 * @param name
	 * @param duration
	 */
	public BasicTimerTrack( String name, long duration )
	{
		super( name );
		timeCode.setDuration( duration );
	}

	/**
	 * @param duration
	 */
	public void setDuration( long duration )
	{
		timeCode.setDuration( duration );

	}

	/**
	 *
	 * @param flag
	 */
	public BasicTimerTrack setPeriodic( boolean flag )
	{
		timeCode.setPeriodic( flag );
		return this;

	}

	/**
	 * This has no effect if called after the track is started. It backdates the
	 * start time as if it had started earlier.
	 *
	 * @param timeInMillis
	 */
	public void setHeadstart( long timeInMillis )
	{
		timeCode.setHeadstart( timeInMillis );
	}

	/**
	 *
	 * @return
	 */
	public boolean isPeriodic()
	{
		return timeCode.isPeriodic();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.lib.animator.track.Track#update(com.cybernostics.lib
	 * .animator.TimeStamp)
	 */
	@Override
	public void update( TimeStamp timeCodeFromSequencer )
	{
		if (isStopped() || timeCode.isPaused())
		{
			return;
		}
		try
		{
			timeCode.setCurrent( timeCodeFromSequencer );

			double fractionOfDuration = 0;
			fractionOfDuration = timeCode.getFractionElapsed();
			//System.out.printf("%s %f\n",getName(),fractionOfDuration);
			if (fractionOfDuration >= 1.0)
			{
				if (timeCode.getElapsed() < timeCode.getDuration())
				{
					throw new RuntimeException( "Stopping too early" );
				}

				if (!timeCodeFromSequencer.isPeriodic())
				{
					stop( true );
				}
			}
			else
			{
				doUpdate( (float) fractionOfDuration );
				fireTrackUpdated( timeCode );
			}
		}
		catch (Exception e1)
		{
			throw new RuntimeException( e1 );
		}

	}

	public void doUpdate( float t )
	{
		update( easing.map( (float) t ) );
	}

	/**
	 *
	 * @param t
	 */
	abstract public void update( float t );

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.animator.track.BasicTrack#start()
	 */
	@Override
	public synchronized void start()
	{
		long headstart = timeCode.getHeadstart();
		if (isStopped())
		{
			reset();
			timeCode.setHeadstart( headstart );
		}
		// System.out.printf("%s started. \n",getName());
		timeCode.start();
		super.start();
		try
		{
			timeCode.getFractionElapsed();
			doUpdate( (float) timeCode.getFractionElapsed() );
		}
		catch (DurationRequiredException ex)
		{
			AppExceptionManager.handleException(
				ex,
				getName() );
		}

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
	public void stop( boolean fireUpdates )
	{
		doUpdate( 1.0f );
		super.stop( fireUpdates );

		// System.out.printf("%s stopped. \n",getName());
	}

	@Override
	public void reset()
	{
		super.reset();
		long duration = timeCode.getDuration();
		timeCode.reset();
		timeCode.setDuration( duration );
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

}
