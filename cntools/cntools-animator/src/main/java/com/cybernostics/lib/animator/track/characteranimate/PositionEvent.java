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

package com.cybernostics.lib.animator.track.characteranimate;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cybernostics.lib.animator.track.TimedEvent;
import com.cybernostics.lib.animator.track.Track;

/**
 * @author jasonw
 *
 */
@XmlRootElement
public final class PositionEvent implements TimedEvent
{

	/**
	 *
	 */
	public PositionEvent()
	{
	}

	/**
	 *
	 * @param position
	 * @param timestamp
	 */
	public PositionEvent( PartPositionPath position, long timestamp )
	{
		setPosition( position );
		setTimestamp( timestamp );
	}

	/**
	 *
	 * @param position
	 * @param timestamp
	 */
	public PositionEvent( String position, long timestamp )
	{
		this( new PartPositionPath( position ), timestamp );
	}

	/**
	 * The position of the mouth at the given instant
	 */
	private PartPositionPath position;

	/**
	 * The moment at which the mouth has the position
	 */
	private long timestamp;

	/**
	 * @param position the position to set
	 */
	public void setPosition( PartPositionPath position )
	{
		this.position = position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition( String position )
	{
		this.position = new PartPositionPath( position );
	}

	/**
	 * @return the position
	 */
	@XmlAttribute
	public String getPosition()
	{
		return position.toString();
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp( long timestamp )
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return the timestamp
	 */
	@XmlAttribute
	public long getTimestamp()
	{
		return timestamp;
	}

	/**
	 *
	 * @param parent
	 * @param obj
	 */
	@Override
	public void execute( Track parent, Object obj )
	{
		SVGArticulatedIcon characterIcon = (SVGArticulatedIcon) obj;
		characterIcon.changePosition( getPosition() );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.animator.track.TimedEvent#getTimeStamp()
	 */
	/**
	 *
	 * @return
	 */
	@Override
	public long getTimeStamp()
	{
		return this.timestamp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.animator.track.TimedEvent#setTimeStamp()
	 */
	/**
	 *
	 * @param timestamp
	 */
	@Override
	public void setTimeStamp( long timestamp )
	{
		this.timestamp = timestamp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TimedEvent clone()
	{

		return new PositionEvent( this.position, this.timestamp );
	}

}
