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

import java.util.ArrayList;
import java.util.List;

import com.cybernostics.lib.animator.track.TimedEventSource;

/**
 * @author jasonw
 * 
 */
public class RandomTimedEventSource implements TimedEventSource
{

	long currentEventTime = 0;
	/**
	 * The maximum time between random events
	 */
	long maxPeriod = 300;
	/*
	 *  The minimum gap between events
	 */
	long minPeriod = 0;

	/**
	 * 
	 * @return
	 */
	public long getMaxPeriod()
	{
		return maxPeriod;
	}

	/**
	 * 
	 * @param maxPeriod
	 */
	public void setMaxPeriod( long maxPeriod )
	{
		this.maxPeriod = maxPeriod;
	}

	/**
	 * 
	 * @return
	 */
	public long getMinPeriod()
	{
		return minPeriod;
	}

	/**
	 * 
	 * @param minPeriod
	 */
	public void setMinPeriod( long minPeriod )
	{
		this.minPeriod = minPeriod;
	}

	/**
	 * 
	 */
	public List< TimedEvent > eventList = new ArrayList< TimedEvent >();

	/**
	 * 
	 */
	public RandomTimedEventSource()
	{
	}

	/**
	 * 
	 * @param events
	 */
	public RandomTimedEventSource( List< TimedEvent > events )
	{
		setEventList( events );
		if (events == null)
		{
			return;
		}

		if (events.size() > 0)
		{
			setInitial( events.get( 0 ) );
			setPost( events.get( 0 ) );
		}
	}

	/**
	 * 
	 * @return
	 */
	public List< TimedEvent > getPositionList()
	{
		return eventList;
	}

	/**
	 * 
	 * @param mouthPositions
	 */
	public void setEventList( List< TimedEvent > mouthPositions )
	{
		this.eventList = mouthPositions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cybernostics.lib.animator.track.characteranimate.PositionEventSource
	 * #getNext()
	 */
	@Override
	public TimedEvent getNext()
	{
		TimedEvent nextPos = null;
		if (currentEventTime == 0)
		{
			updateCurrentEventTime();
			return getInitial();
		}
		else
		{
			nextPos = getRandomEvent();
		}

		return nextPos;
	}

	private void updateCurrentEventTime()
	{
		currentEventTime = currentEventTime
			+ ( minPeriod + (long) ( Math.random() * ( maxPeriod - minPeriod ) ) ); // 50-300
	}

	/**
	 * 
	 * @return
	 */
	public TimedEvent getRandomEvent()
	{
		updateCurrentEventTime();
		TimedEvent event = null;
		if (eventList.size() == 1)
		{
			event = eventList.get(
				0 )
				.clone();
		}
		else
		{
			event = eventList.get(
				(int) ( Math.random() * eventList.size() ) )
				.clone();
		}
		event.setTimeStamp( currentEventTime );
		return event;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cybernostics.lib.animator.track.characteranimate.PositionEventSource
	 * #reset()
	 */

	@Override
	public void reset()
	{
		currentEventTime = 0;
	}

	private TimedEvent initial = null;
	private TimedEvent post = null;

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getInitial()
	 */
	public TimedEvent getInitial()
	{
		return initial;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getPost()
	 */
	@Override
	public TimedEvent getPost()
	{
		return post;
	}

	/**
	 * @param initial the initial to set
	 */
	public void setInitial( TimedEvent initial )
	{
		this.initial = initial;
	}

	/**
	 * @param post the post to set
	 */
	public void setPost( TimedEvent post )
	{
		this.post = post;
	}
}
