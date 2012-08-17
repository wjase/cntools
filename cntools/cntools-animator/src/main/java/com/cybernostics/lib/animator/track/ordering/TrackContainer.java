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
 * 
 * @author jasonw
 */
public interface TrackContainer
{

	/**
	 * Adds a track but doesn't start it yet.
	 * 
	 * @param aTrack
	 */
	public void addTrack( Track aTrack );

	/**
	 * Adds and starts a track
	 * 
	 * @param aTrack
	 */
	public void addAndStartTrack( Track aTrack );

	/**
	 * Stops all tracks in this container
	 */
	public void stop();

	public void start();

	public void pause();

	public void resume();

	public void clear();

}
