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
package com.cybernostics.lib.animator;

import com.cybernostics.lib.concurrent.AnimationTimer;

/**
 * The StopWatch class represents a very simple stop watch that can be used to
 * time events.
 */
public class StopWatch
{

	private long startTime;
	private long stopTime;
	private String label;

	/**
	 * Construct a new StopWatch object.
	 */
	public StopWatch()
	{
	}

	/**
	 * 
	 * @param string
	 */
	public StopWatch( String string )
	{
		label = string;
	}

	/**
	 * Start this StopWatch object. Starting a StopWatch that was already
	 * running resets the watch.
	 */
	public void start()
	{
		startTime = AnimationTimer.getCurrentTime();
	}

	/**
	 * Stop this StopWatch object.
	 */
	public void stop()
	{
		stopTime = AnimationTimer.getCurrentTime();
	}

	/**
	 * Get the time recorded by this StopWatch object. The value returned if
	 * getTime is called before this StopWatch has been stopped is meaningless.
	 * 
	 * @return 
	 */
	public long getTime()
	{
		return stopTime - startTime;
	}

	/**
	 * 
	 */
	public void summarise()
	{
		stop();
		summary( label );
	}

	/**
	 * 
	 * @param string
	 */
	public void summary( String string )
	{
		System.out.println( string + " time:" + getTime() + "milliseconds" );
	}
}
