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

import com.cybernostics.lib.animator.track.Track;

/**
 * This listener will add and start a new track at the end or start of an
 * existing track depending on which event is registered with that track using
 * addxyzListener()
 *
 * @author jasonw
 *
 */
public class TrackAdder implements TrackEndedListener, TrackStartedListener
{

	private Track parentTrack;

	private Track toAdd;

	public TrackAdder( Track parent, Track toAdd )
	{
		this.parentTrack = parent;
		this.toAdd = toAdd;
	}

	/**
	 *
	 * @param source
	 */
	@Override
	public void trackEnded( Track source )
	{
		addTrack();
	}

	private void addTrack()
	{
		TrackContainer tc = parentTrack.getSequencer();
		if (tc != null)
		{
			tc.addAndStartTrack( toAdd );
		}

	}

	/**
	 *
	 * @param source
	 */
	@Override
	public void trackStarted( Track source )
	{
		addTrack();
	}

}
