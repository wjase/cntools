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

package com.cybernostics.lib.animator.track.ordering;

import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.collections.ArrayUtils;
import com.cybernostics.lib.concurrent.TimeStamp;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convenience container to collect parallel tracks
 *
 * @author jasonw
 *
 */
public class ParallelTrack extends BasicTrack
{

	/**
	 *
	 * @param tracks
	 * @return
	 */
	public static BasicTrack connect( Track... tracks )
	{
		return new ParallelTrack( tracks );
	}

	//private Set< Track> allTracks = new HashSet< Track>();
	ConcurrentHashMap< Track, Boolean > allTracks = new ConcurrentHashMap< Track, Boolean >();

	/**
	 *
	 * @param tracks
	 */
	public ParallelTrack( Track... tracks )
	{
		super( "parallel:" + ArrayUtils.implode(
			",\n",
			Arrays.asList( tracks ) ) );

		for (Track eachTrack : tracks)
		{
			eachTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					removeTrack( source );
				}

			} );
			allTracks.put(
				eachTrack,
				true );
			startWithMe( eachTrack );
		}
	}

	/**
	 * @param source
	 */
	protected void removeTrack( Track source )
	{
		allTracks.remove( source );
		if (allTracks.isEmpty()) // all done
		{
			stop( true );
		}

	}

	/*
	 * (non-Javadoc) @see
	 * com.cybernostics.lib.animator.track.Track#update(com.cybernostics.lib.animator.TimeStamp)
	 */
	@Override
	public void update( TimeStamp timeCode )
	{
		// do nothing this is just a container
	}

	@Override
	public void stop( boolean fireEvents )
	{
		super.stop( fireEvents );

		// if stop is forced
		if (!allTracks.isEmpty())
		{
			for (Track eachTrack : allTracks.keySet())
			{
				eachTrack.stop( true );
			}
		}
	}

	@Override
	public void pause()
	{
		for (Track eachTrack : allTracks.keySet())
		{
			eachTrack.pause();
		}
	}

	@Override
	public void resume()
	{
		for (Track eachTrack : allTracks.keySet())
		{
			eachTrack.resume();
		}
	}

}
