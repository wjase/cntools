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

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.kitfox.svg.elements.SVGElement;

/**
 * @author jasonw
 * 
 */
@XmlRootElement
public class PartPositionPath implements Comparable< PartPositionPath >
{
	PartPosition[] path = null;

	PartPositionPath()
	{

	}

	private SVGElement associatedElement = null;

	/**
	 * @param id
	 */
	public PartPositionPath( String id )
	{
		setPath( id );
	}

	PartPosition getPathPart( int index )
	{
		if (index >= path.length)
		{
			return null;
		}
		return path[ index ];
	}

	int getNumParts()
	{
		return path.length;
	}

	/**
	 * 
	 * @param sPath
	 */
	public void setPath( String sPath )
	{
		ArrayList< PartPosition > path = new ArrayList< PartPosition >();
		int index = sPath.indexOf( "." );
		int lastIndex = 0;

		do
		{
			if (index == -1)
			{
				path.add( new PartPosition( sPath.substring( lastIndex == 0 ? lastIndex
					: ++lastIndex ) ) );
				break;
			}
			else
			{
				path.add( new PartPosition( sPath.substring(
					lastIndex == 0 ? lastIndex : ++lastIndex,
					index ) ) );
			}
			lastIndex = index;
			index = sPath.indexOf(
				".",
				index + 1 );
		}
		while (index != -1);

		if (lastIndex != 0)
		{
			path.add( new PartPosition( sPath.substring( ++lastIndex ) ) );
		}

		this.path = new PartPosition[ path.size() ];
		int addIndex = 0;
		for (PartPosition eachPart : path)
		{
			this.path[ addIndex++ ] = eachPart;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getPath();
	}

	/**
	 * 
	 * @return
	 */
	@XmlAttribute
	public String getPath()
	{
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		for (PartPosition part : path)
		{
			if (!isFirst)
			{
				sb.append( "." );

			}
			sb.append( part.toString() );
			isFirst = false;

		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( PartPositionPath o )
	{
		return compareTo(
			o,
			Math.max(
				path.length - 1,
				o.path.length - 1 ) );
	}

	/**
	 * 
	 * @param o
	 * @param maxItemToCompare
	 * @return
	 */
	public int compareTo( PartPositionPath o, int maxItemToCompare )
	{
		int index = 0;

		int comparison = getPathPart(
			index ).compareTo(
			o.getPathPart( index ) );

		while (comparison == 0)
		{
			++index;

			if (index > maxItemToCompare)
			{
				break;
			}

			PartPosition myBit = getPathPart( index );
			PartPosition otherBit = o.getPathPart( index );

			if (myBit == null)
			{
				if (otherBit != null)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}

			if (( myBit != null ) && ( otherBit == null ))
			{
				return 1;
			}
			comparison = myBit.compareTo( otherBit );

		}
		return comparison;
	}

	/**
	 * @param associatedElement
	 *            the associatedElement to set
	 */
	public void setAssociatedElement( SVGElement associatedElement )
	{
		this.associatedElement = associatedElement;
	}

	/**
	 * @return the associatedElement
	 */
	public SVGElement getAssociatedElement()
	{
		return associatedElement;
	}

	/**
	 * Returns true if this item matches each of the items in the otherPos.
	 * 
	 * so
	 * 
	 * body-front.head-side
	 * 
	 * will match:
	 * 
	 * <pre>
	 * body-front.head-front 
	 * body-front.head-front.mouth.open
	 * body-front.head-front.eyes-open
	 * </pre>
	 * 
	 * So all but the last bit of this path must match the other + the last bit
	 * must have the same part id The other part must be at least as long as
	 * this one to match
	 * 
	 * @param otherPos 
	 * @return
	 */
	public boolean matchesPart( PartPositionPath otherPos )
	{
		if (otherPos.getNumParts() < getNumParts())
		{
			return false;
		}
		// get the highest valid index for both items
		int highestCommonIndex = Math.min(
			otherPos.path.length - 1,
			path.length - 1 );

		if (highestCommonIndex > 0)
		{
			if (compareTo(
				otherPos,
				highestCommonIndex - 1 ) != 0)
			{
				return false;
			}
		}

		PartPosition lastBit = getPathPart( highestCommonIndex );
		PartPosition otherBit = otherPos.getPathPart( highestCommonIndex );

		if (otherBit == null)
		{
			return false;
		}

		return lastBit.partIdMatches( otherBit );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone()
	{
		return new PartPositionPath( getPath() );

	}

	/**
	 * Takes the sub items of the supplied path and adds them to this one
	 * 
	 * @param toBeHidden
	 * @return
	 */
	public PartPositionPath getChildPathFrom( PartPositionPath toBeHidden )
	{
		// check if the other item has the same or less bits
		if (toBeHidden.getNumParts() <= getNumParts())
		{
			return null;
		}

		PartPositionPath newPath = (PartPositionPath) toBeHidden.clone();

		int index = path.length - 1;

		newPath.getPathPart(
			index )
			.setPosition(
				getPathPart(
					index ).getPosition() );

		return newPath;
	}

	/**
	 * 
	 * @param parts
	 * @param size
	 */
	public PartPositionPath( PartPosition[] parts, int size )
	{
		path = new PartPosition[ size ];

		for (int index = 0; index < size; ++index)
		{
			path[ index ] = parts[ index ];
		}

	}

	/**
	 * Build a composit path from the combination of a root plus a leaf path
	 * 
	 * @param parentPath
	 * @param childPath
	 */
	public PartPositionPath(
		PartPositionPath parentPath,
		PartPositionPath childPath )
	{
		path = new PartPosition[ parentPath.getNumParts()
			+ childPath.getNumParts() ];
		int index = 0;

		for (int parentIndex = 0; parentIndex < parentPath.getNumParts(); ++parentIndex)
		{
			path[ index ] = parentPath.getPathPart( parentIndex );
			index++;
		}
		for (int childIndex = 0; childIndex < childPath.getNumParts(); ++childIndex)
		{
			path[ index ] = childPath.getPathPart( childIndex );
			index++;
		}

	}

	/**
	 * @param newParts
	 */
	public PartPositionPath( PartPosition[] newParts )
	{
		path = newParts;
	}

	/**
	 * Get the parent path of this one
	 * 
	 * @return
	 */
	public PartPositionPath getParentPath()
	{
		if (path.length > 1)
		{
			// return a path with one less path bit
			return new PartPositionPath( path, path.length - 1 );
		}
		return null;
	}

	/**
	 * @param eachItem
	 * @return
	 */
	public boolean isParentOf( PartPositionPath eachItem )
	{
		return compareTo(
			eachItem,
			path.length - 1 ) == 0;
	}

	/**
	 * @param eachItem
	 * @return
	 */
	public boolean shareRoot( PartPositionPath eachItem )
	{

		return compareTo(
			eachItem,
			Math.min(
				path.length - 1,
				eachItem.path.length - 1 ) ) == 0;
	}

	/**
	 * @param posToFind
	 * @return
	 */
	public boolean containsSubPath( PartPositionPath posToFind )
	{
		// this must be longer than the other (more path bits)
		if (path.length < posToFind.path.length)
		{
			return false;
		}

		int myHighestIndex = path.length - 1;
		int itemsToCheck = posToFind.path.length;
		int itemIndex = 0;
		int myIndexToCheck = 0;

		while (myIndexToCheck <= myHighestIndex)
		{
			if (( path[ myIndexToCheck ].compareTo( posToFind.path[ itemIndex ] ) == 0 )
				|| ( ( itemsToCheck == 1 )
					&& ( myIndexToCheck == myHighestIndex ) && path[ myIndexToCheck ]
					.partIdMatches( posToFind.path[ itemIndex ] ) ))
			{
				itemIndex++;
				myIndexToCheck++;
				itemsToCheck--;

				if (itemsToCheck == 0)
				{
					return true;
				}
			}
			else
			{
				if (itemIndex > 0) // paths don't match
				{
					// if the last item of the path to find matches path id of
					// the current part of this path

					return false;
				}
				myIndexToCheck++; // try next one
			}

		}
		// PartPosition firstBit =
		return false;
	}

	/**
	 * @param eachItem
	 * @return
	 */
	public boolean matchesPosition( PartPositionPath eachItem )
	{
		return ( getNumParts() == eachItem.getNumParts() )
			&& ( compareTo(
				eachItem,
				Math.min(
					path.length - 1,
					eachItem.path.length - 1 ) ) == 0 );
	}

	/**
	 * @param toAdd
	 * @return
	 */
	public boolean isMutuallyExclusiveWith( PartPositionPath toAdd )
	{
		int maxIndex = getNumParts() - 1;
		for (int index = 0; index <= maxIndex; ++index)
		{
			PartPosition mine = getPathPart( index );
			PartPosition other = toAdd.getPathPart( index );

			if (other == null)
			{
				return false;
			}

			// if any part of the path matches part but not position then they
			// are exclusive
			if (mine.partIdMatches( other ) && !mine.positionMatches( other ))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @param posToFind
	 * @return
	 */
	public boolean endsWithSubPath( PartPositionPath posToFind )
	{
		int startIndex = getNumParts() - posToFind.getNumParts();
		return getSubPathFrom(
			startIndex ).equals(
			posToFind );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		PartPositionPath other = (PartPositionPath) obj;
		for (int i = 0; i < path.length; ++i)
		{
			if (!( path[ i ].equals( other.path[ i ] ) ))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * @param startIndex
	 * @return
	 */
	private PartPositionPath getSubPathFrom( int startIndex )
	{
		PartPosition[] newParts = new PartPosition[ getNumParts() - startIndex ];
		for (int i = startIndex; i < getNumParts(); ++i)
		{
			newParts[ i - startIndex ] = getPathPart( i );
		}
		return new PartPositionPath( newParts );

	}
}
