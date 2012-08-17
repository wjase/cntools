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
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * logical grouping of tracks within a Sequencer
 *
 * @author jasonw
 */
public class TrackGroup extends ConcurrentLinkedQueue< Track >
	implements
	TrackContainer
{

	private String id = "";

	public String getId()
	{
		return id;
	}

	TrackEndedListener onEnd = new TrackEndedListener()
	{

		@Override
		public void trackEnded( Track source )
		{
			remove( source );
		}

	};

	private Sequencer inner = Sequencer.get();

	public void setSequencer( Sequencer inner )
	{
		if (inner != null)
		{
			this.inner = inner;
		}
	}

	@Override
	public boolean add( Track e )
	{
		addTrack( e );
		return true;
	}

	@Override
	public synchronized void addTrack( Track aTrack )
	{
		aTrack.addTrackEndedListener( onEnd );
		super.add( aTrack );
		aTrack.setSequencer( this );
		inner.addTrack( aTrack );

	}

	@Override
	public synchronized void addAndStartTrack( Track aTrack )
	{
		addTrack( aTrack );
		aTrack.start();
	}

	@Override
	public synchronized void stop()
	{
		for (Track eachTrack : this)
		{
			eachTrack.stop( false );

		}
	}

	public void pause()
	{
		for (Track eachTrack : this)
		{
			eachTrack.pause();

		}
	}

	public void resume()
	{
		for (Track eachTrack : this)
		{

			eachTrack.resume();
		}
	}

	@Override
	public void start()
	{
		for (Track eachTrack : this)
		{

			eachTrack.start();

		}
	}

}
