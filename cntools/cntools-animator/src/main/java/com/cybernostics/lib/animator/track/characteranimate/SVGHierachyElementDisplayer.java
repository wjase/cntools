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

import com.cybernostics.lib.concurrent.ConcurrentSet;
import com.cybernostics.lib.exceptions.AppExceptionManager;
import com.kitfox.svg.elements.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.animation.AnimationElement;
import java.util.*;
import java.util.logging.Logger;

/**
 * Controls which elements to be shown and which to be hidden in a hierarchy
 *
 * @author jasonw
 *
 */
public class SVGHierachyElementDisplayer
{

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		SVGHierachyElementDisplayer newItem = new SVGHierachyElementDisplayer();
		newItem.allControlledElements.putAll( this.allControlledElements );
		return newItem;
	}

	private Set< PartPositionPath > shownElements = new TreeSet< PartPositionPath >();

	/**
	 * the population of all elements which can be switched on or off
	 */
	Map< PartPositionPath, PartPositionPath > allControlledElements = new TreeMap< PartPositionPath, PartPositionPath >();

	/**
	 *
	 * @param toAdd
	 */
	public void addControlledElement( PartPositionPath toAdd )
	{
		allControlledElements.put(
			toAdd,
			toAdd );
	}

	private ConcurrentSet< PartPositionPath > pendingToShow = new ConcurrentSet< PartPositionPath >();

	private ConcurrentSet< PartPositionPath > pendingToHide = new ConcurrentSet< PartPositionPath >();

	private PartPositionPath findPath( String newPosition )
	{
		// we need to look it up because we need the associated SVGElement
		// stored
		// in the instance in the Map
		PartPositionPath posToFind = new PartPositionPath( newPosition );
		PartPositionPath newPos = allControlledElements.get( posToFind );

		if (newPos == null)
		{
			for (PartPositionPath eachPath : shownElements)
			{
				if (eachPath.containsSubPath( posToFind ))
				{
					newPos = allControlledElements.get( new PartPositionPath( eachPath.getParentPath(),
						posToFind ) );
					// System.out.printf( "found path:%s \n", newPos.getPath()
					// );
					break;

				}
			}
		}

		if (newPos == null)
		{
			for (PartPositionPath eachItem : allControlledElements.keySet())
			{
				// simply changing mouth position without head position
				if (eachItem.endsWithSubPath( posToFind ))
				{
					if (isShown( eachItem.getParentPath() ))
					{
						newPos = eachItem;
						break;
					}
				}

			}
		}
		if (newPos == null)
		{
			Logger.getAnonymousLogger()
				.severe(
					String.format(
						"Couldn't get element for %s",
						newPosition ) );
			return null;
		}

		return newPos;
	}

	/**
	 * @param parentPath
	 * @return
	 */
	private boolean isShown( PartPositionPath parentPath )
	{
		return shownElements.contains( parentPath );
	}

	private void calculateChanges( PartPositionPath newPos )
	{

		// first find all the items which will be hidden by this item
		// ie for body-front find all the items which have body in some other
		// orientation
		// eg
		// "body-front.head-front.eyes-closed"
		// "body-front.head-right"

		// get a list of parents in reverse order
		Stack< PartPositionPath > parents = new Stack< PartPositionPath >();
		PartPositionPath current = newPos;

		while (current != null)
		{
			parents.add( current );
			current = current.getParentPath();
		}

		// loop through changing all refs from old orientation to new while
		// filtering against newPos
		while (!parents.empty())
		{
			current = parents.pop();

			// already shown no work to do
			if (shownElements.contains( current ))
			{
				continue;
			}

			pendingToShow.add( current );

			for (PartPositionPath eachItem : shownElements)
			{
				if (current.shareRoot( eachItem ))
				{
					continue;
				}

				// if the PartIds match but the position is different then
				// remove them
				// (but add any corresponding child elements in the new
				// position)
				if (current.matchesPart( eachItem )
					&& !current.matchesPosition( eachItem ))
				{
					pendingToHide.add( eachItem );

					// Get corresponding child element to add
					PartPositionPath toAdd = current.getChildPathFrom( eachItem );

					if (toAdd != null)
					{
						toAdd = allControlledElements.get( toAdd ); // look it
						// up
						if (toAdd != null)
						{
							if (!newPos.isMutuallyExclusiveWith( toAdd ))
							{
								// System.out.printf("Adding %s not exclusive with %s\n",toAdd,
								// newPos);
								pendingToShow.add( toAdd );
							}
							// else
							// {
							// System.out.printf("Not showing %s\n",toAdd);
							// }
						}
					}

				}
			}

			for (PartPositionPath eachItem : allControlledElements.keySet())
			{
				if (current.isMutuallyExclusiveWith( eachItem ))
				{
					pendingToHide.add( eachItem );
				}
			}

		}

	}

	/**
	 * Changes the items displayed based on the position descriptor
	 *
	 * @param newPosition
	 */
	public void changePosition( String newPosition )
	{
		// System.out.printf( "ChangePosition %s\n", newPosition );

		pendingToShow.clear();
		pendingToHide.clear();

		calculateChanges( findPath( newPosition ) );

		// // hide what needs to be hidden
		for (PartPositionPath eachPart : pendingToHide)
		{
			// System.out.printf( "Hide %s\n", eachPart );
			setElementVisibility(
				eachPart.getAssociatedElement(),
				false );
			shownElements.remove( eachPart );
		}
		//
		// // show what needs to be shown
		for (PartPositionPath eachPart : pendingToShow)
		{
			// System.out.printf( "Show %s\n", eachPart );
			setElementVisibility(
				eachPart.getAssociatedElement(),
				true );
			shownElements.add( eachPart );
		}
		//
		// changes.firePropertyChange( "displayedElements", null, null );
	}

	final static private String attName = "display";

	private void setElementVisibility( SVGElement e, boolean visible )
	{
		if (e == null)
		{
			return;
		}
		String value = visible ? "inline" : "none";

		// System.out.printf( "%s %s\n", visible ? "show" : "hide", e.getId() );
		try
		{
			if (e.hasAttribute(
				attName,
				AnimationElement.AT_CSS ))
			{
				e.setAttribute(
					attName,
					AnimationElement.AT_CSS,
					value );
			}
			else
			{
				e.addAttribute(
					attName,
					AnimationElement.AT_CSS,
					value );
			}

		}
		catch (SVGException e1)
		{
			AppExceptionManager.handleException(
				e1,
				SVGHierachyElementDisplayer.class );
		}
	}

	/**
	 *
	 */
	public void clear()
	{
		shownElements.clear();
		allControlledElements.clear();
		pendingToShow.clear();
		pendingToHide.clear();
	}

}
