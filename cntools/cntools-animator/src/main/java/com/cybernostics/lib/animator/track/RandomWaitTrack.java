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

/**
 * Does nothing for a random time interval somewhere between the minimum and maximum duration
 * 
 * @author jasonw
 * 
 */
public class RandomWaitTrack extends BasicTimerTrack
{
	long minDuration = 0;
	long maxDuration = 0;

	/**
	 * 
	 * @param name
	 * @param minDuration
	 * @param maxDuration
	 * @return
	 */
	public static BasicTrack create( String name,
		long minDuration,
		long maxDuration )
	{
		return new RandomWaitTrack( name, minDuration, maxDuration );
	}

	/**
	 * Performs the specified Runnable at the end of the track
	 * @param name
	 * @param minDuration
	 * @param maxDuration  
	 */
	public RandomWaitTrack( String name, long minDuration, long maxDuration )
	{
		super( name, minDuration );
		this.minDuration = minDuration;
		this.maxDuration = maxDuration;
	}

	@Override
	public void start()
	{
		setDuration( minDuration
			+ (long) ( Math.random() * ( maxDuration - minDuration ) ) );
		super.start();
	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void update( float t )
	{
	}
}
