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

/**
 * @author jasonw
 * 
 */
@XmlRootElement
public class MouthEvent
{
	/**
	 * 
	 */
	public MouthEvent()
	{
	}

	/**
	 * 
	 * @param position
	 * @param timestamp
	 */
	public MouthEvent( MouthPosition position, long timestamp )
	{
		setPosition( position );
		setTimestamp( timestamp );
	}

	/**
	 * The position of the mouth at the given instant
	 */
	private MouthPosition position;

	/**
	 * The moment at which the mouth has the position
	 */
	private long timestamp;

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition( MouthPosition position )
	{
		this.position = position;
	}

	/**
	 * @return the position
	 */
	@XmlAttribute
	public MouthPosition getPosition()
	{
		return position;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
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
}
