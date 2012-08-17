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

import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;

/**
 * Does nothing for the prescribed time
 *
 * @author jasonw
 *
 */
public class WaitTrack extends BasicTimerTrack
{

	private Runnable doWhenFinished = null;

	private static final String WAIT_NAME = "Wait %d";

	/**
	 *
	 * @param name
	 * @param duration
	 * @return
	 */
	public static BasicTimerTrack create( long duration )
	{
		return new WaitTrack( duration, null );
	}

	/**
	 * Performs the specified Runnable at the end of the track
	 *
	 * @param name
	 * @param duration
	 * @param atEndTask
	 */
	public WaitTrack( long duration, Runnable atEndTask )
	{
		super( String.format(
			WAIT_NAME,
			duration ), duration );
		doWhenFinished = atEndTask;
		if (doWhenFinished != null)
		{
			addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					doWhenFinished.run();

				}

			} );

		}
	}

	/**
	 * @param fractionElapsed a value between 0 and 1 once the duration has
	 * elapsed
	 */
	public void update( float fractionElapsed )
	{
	}

}
