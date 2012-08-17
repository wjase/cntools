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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jasonw
 */
public class TrackEndedEvent implements TrackEndedListener
{

	private CountDownLatch allDone = new CountDownLatch( 1 );

	private BasicTrack waitFor = null;

	TrackEndedEvent( BasicTrack aThis )
	{
		this.waitFor = aThis;
	}

	@Override
	public void trackEnded( Track source )
	{
		source.removeTrackEndedListener( this );
		allDone.countDown();
	}

	public void await( long timeoutMillies ) throws InterruptedException,
		TimeoutException
	{
		if (!waitFor.hasEnded())
		{
			if (!allDone.await(
				timeoutMillies,
				TimeUnit.MILLISECONDS ))
			{
				throw new TimeoutException();
			}
		}
	}

	/**
	 * Resets the mutex latch so this listener can be reused
	 */
	void reset()
	{
		allDone = new CountDownLatch( 1 );
	}

}
