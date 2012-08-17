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
import com.cybernostics.lib.collections.IterableArray;
import com.cybernostics.lib.concurrent.TimeStamp;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Convenience container to collect end to end tracks
 *
 * @author jasonw
 *
 */
public class SerialTrack extends BasicTrack
{

	private Track[] allTracks = null;

	private Iterator< Track > trackIt = null;

	private Track activeTrack = null;

	public void setActiveTrack( Track nextTrack )
	{
		this.activeTrack = nextTrack;

		if (activeTrack != null)
		{
			activeTrack.addTrackEndedListener( startNextTrack );
			getSequencer().addAndStartTrack(
				activeTrack );
		}
	}

	/**
	 *
	 * @param tracks
	 * @return
	 */
	public static BasicTrack connect( Track... tracks )
	{
		return new SerialTrack( tracks );
	}

	/**
	 * As each track starts we want to update our activeTrack so we can pause or
	 * stop it as required
	 */
	private TrackEndedListener startNextTrack = new TrackEndedListener()
	{

		@Override
		public void trackEnded( Track source )
		{
			startNext();
		}

	};

	/**
	 *
	 * @param tracks
	 */
	public SerialTrack( Track... tracks )
	{
		super( "serial:\n" + ArrayUtils.implode(
			",\n",
			Arrays.asList( tracks ) ) );

		allTracks = tracks;

	}

	/**
	 *
	 * @return
	 */
	public final Track getLast()
	{
		return allTracks[ allTracks.length - 1 ];
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
	public void start()
	{
		if (!isRunning())
		{
			super.start();
			trackIt = IterableArray.get(
				allTracks )
				.iterator();

			startNext();

		}
	}

	private void startNext()
	{
		if (trackIt.hasNext())
		{
			setActiveTrack( trackIt.next() );
		}
		else
		{
			setActiveTrack( null );
			stop( true );
		}
	}

	@Override
	public void pause()
	{
		if (activeTrack != null)
		{
			activeTrack.pause();
		}
	}

	@Override
	public void resume()
	{
		if (activeTrack != null)
		{
			activeTrack.resume();
		}
	}

	@Override
	public void reset()
	{
		super.reset();
		for (Track eachTrack : IterableArray.get( allTracks ))
		{
			eachTrack.removeTrackEndedListener( startNextTrack );
			if (eachTrack instanceof BasicTrack)
			{
				( (BasicTrack) eachTrack ).reset();
			}
		}
		setActiveTrack( null );

		trackIt = null;

	}

}
