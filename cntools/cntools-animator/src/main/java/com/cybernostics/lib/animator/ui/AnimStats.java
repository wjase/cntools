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

package com.cybernostics.lib.animator.ui;

/**
 *
 * @author jasonw
 */
public class AnimStats
{

	// private static long MAX_STATS_INTERVAL = 1000L;
	// record stats every 1 second (roughly)
	public static final int NO_DELAYS_PER_YIELD = 16;

	public long timeDiff, sleepTime;

	public long overSleepTime = 0L;

	public int noDelays = 0;

	public long excess = 0L;

	public static int MAX_FRAME_SKIPS = 2; // was 2;

	public long period; // period between drawing in _nanosecs_

	public long skips;
}
