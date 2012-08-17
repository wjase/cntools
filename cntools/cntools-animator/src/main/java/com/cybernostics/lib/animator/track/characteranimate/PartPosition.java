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

/**
 * Identifies one part of the hierarchy in an animated feature position
 * 
 * e.g. mouth-open
 * 
 * @author jasonw
 * 
 */
public class PartPosition implements Comparable< PartPosition >
{

	/**
	 * 
	 */
	public PartPosition()
	{

	}

	/**
	 * 
	 * @param other
	 * @return
	 */
	public boolean partIdMatches( PartPosition other )
	{
		return partId.equals( other.partId );

	}

	/**
	 * 
	 * @param composite
	 */
	public PartPosition( String composite )
	{
		int index = composite.indexOf( '-' );
		if (index != -1)
		{
			partId = composite.substring(
				0,
				index );
			position = composite.substring( index + 1 );
		}
	}

	public String toString()
	{
		return partId + "-" + position;
	}

	/**
	 * Identifies a body part to be animated
	 */
	private String position = null;

	/**
	 * Identifies a position for the part
	 */
	private String partId = null;

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition( String position )
	{
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public String getPosition()
	{
		return position;
	}

	/**
	 * @param partId
	 *            the partId to set
	 */
	public void setPartId( String partId )
	{
		this.partId = partId;
	}

	/**
	 * @return the partId
	 */
	public String getPartId()
	{
		return partId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		PartPosition other = (PartPosition) obj;
		return this.partId.equals( other.partId )
			&& this.position.equals( other.position );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( PartPosition o )
	{
		return toString().compareTo(
			o.toString() );
	}

	/**
	 * @param other 
	 * @return
	 */
	public boolean positionMatches( PartPosition other )
	{
		// TODO Auto-generated method stub
		return position.equals( other.position );
	}
}
