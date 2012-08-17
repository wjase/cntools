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

import com.cybernostics.lib.animator.track.ordering.TrackStartedListener;
import com.cybernostics.lib.animator.track.ordering.TrackUpdatedListener;
import com.cybernostics.lib.animator.track.ordering.TrackContainer;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.concurrent.TimeStamp;

/**
 *
 * @author jasonw
 */
public interface Track
{

	public Track connectStart( Track toAdd );

	public Track connectEnd( Track toAdd );

	/**
	 * Updates the Track with the current time from the sequencer
	 *
	 * @param timeCode
	 */
	public void update( TimeStamp timeCode );

	/**
	 * gets the name of this track (for debugging)
	 *
	 * @return
	 */
	public String getName();

	/**
	 * Starts this element
	 */
	public void start();

	/**
	 * Set the target to start after this one
	 *
	 * @param target
	 */
	public void addTrackEndedListener( TrackEndedListener target );

	/**
	 * Set the target to start with this one
	 *
	 * @param target
	 */
	public void addTrackStartedListener( TrackStartedListener target );

	/**
	 * Set the target to monitor this one. It will get called whenever the track
	 * updates the sprite. Used for things like sound effects
	 *
	 * @param target
	 */
	public void addTrackUpdatedListener( TrackUpdatedListener target );

	/**
	 * @return a handle to the Sequencer managing this track. (Usually to
	 * add/start a new Track)
	 */
	public TrackContainer getSequencer();

	/**
	 * @param atc the Container to manage this track. Typically the container
	 * only holds onto the track while it is active.
	 */
	public void setSequencer( TrackContainer atc );

	/**
	 * @return true if the Track has reached its end condition
	 */
	public boolean hasEnded();

	/**
	 * stops this track and fires end listeners (which may start new tracks)
	 */
	public void stop( boolean fireEvents );

	/**
	 * @param trackEndedListener
	 */
	public void removeTrackEndedListener( TrackEndedListener trackEndedListener );

	/**
	 * @param trackEndedListener
	 */
	public void removeTrackStartedListener( TrackStartedListener trackEndedListener );

	/**
	 * Suspends the track such that it can be restarted by resume
	 */
	public void pause();

	/**
	 * Restarts a paused track
	 */
	public void resume();

	public boolean isRunning();

}
