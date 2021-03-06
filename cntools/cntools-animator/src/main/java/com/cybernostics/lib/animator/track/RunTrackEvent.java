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
 * @author jasonw
 * 
 */
public class RunTrackEvent extends AbstractTimedEvent implements TimedEvent
{
	Track toRun = null;

	/**
	 * 
	 * @param toRun
	 */
	public RunTrackEvent( Track toRun )
	{
		this.toRun = toRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.AbstractTimedEvent#clone()
	 */
	@Override
	public TimedEvent clone()
	{
		TimedEvent item = new RunTrackEvent( this.toRun );
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.cybernostics.lib.animator.track.AbstractTimedEvent#execute(com.
	 * cybernostics.lib.animator.track.Track, java.lang.Object)
	 */
	/**
	 * 
	 * @param parentTrack
	 * @param obj
	 */
	@Override
	public void execute( Track parentTrack, Object obj )
	{
		parentTrack.getSequencer()
			.addAndStartTrack(
				toRun );
	}

}
