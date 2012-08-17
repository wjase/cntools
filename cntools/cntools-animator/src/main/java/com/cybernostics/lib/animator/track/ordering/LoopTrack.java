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
import com.cybernostics.lib.concurrent.TimeStamp;

/**
 *
 * @author jasonw
 */
public class LoopTrack extends BasicTrack
{

	private BasicTrack toLoop = null;

	/**
	 *
	 */
	public static final int LOOP_FOREVER = -1;

	int iterationsLeft = -1;

	int loopCount = LOOP_FOREVER;

	/**
	 * Create a loop track which will repeat the specified track
	 *
	 * @param iterations - number of repeats or LOOP_FOREVER for continuous
	 * @param toLoop - track to repeat
	 * @return the created loop track
	 */
	public static BasicTrack create( int iterations, BasicTrack toLoop )
	{
		return new LoopTrack( iterations, toLoop );
	}

	@Override
	public void setSequencer( TrackContainer sequencer )
	{
		toLoop.setSequencer( sequencer );
		super.setSequencer( sequencer );
	}

	/**
	 * Creates a LoopTrack which runs the specified toRun track either
	 * continuously or a number of times.
	 *
	 * @param iterations - number of repeats or LOOP_FOREVER for continuous
	 * @param toLoop - track to repeat
	 */
	public LoopTrack( int iterations, BasicTrack toRun )
	{
		super( "Loop:" + toRun.getName() );

		loopCount = iterations;
		toRun.addTrackEndedListener( startAgainListener );
		this.toLoop = toRun;
	}

	TrackEndedListener startAgainListener = new TrackEndedListener()
	{

		@Override
		public void trackEnded( Track source )
		{
			if (isStopped())
			{
				return;
			}
			if (loopCount != LOOP_FOREVER)
			{
				--iterationsLeft;
			}

			if (iterationsLeft == 0)
			{
				LoopTrack.this.stop( true );
			}
			else
			{
				// start it again
				TrackContainer tc = LoopTrack.this.getSequencer();
				toLoop.reset();
				tc.addAndStartTrack( toLoop );
			}
		}

	};

	public void start()
	{
		if (!isRunning())
		{
			iterationsLeft = loopCount;
			super.start();
			getSequencer().addAndStartTrack(
				toLoop );
		}
	}

	@Override
	public void update( TimeStamp timeCode )
	{
	}

	@Override
	public void stop( boolean fireEvents )
	{
		super.stop( fireEvents );
		toLoop.removeTrackEndedListener( startAgainListener );
		toLoop.stop( fireEvents );
	}

	@Override
	public void pause()
	{
		toLoop.pause();
	}

	@Override
	public void resume()
	{
		toLoop.resume();
	}

}
