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
import java.util.Collection;
import java.util.Iterator;

/**
 * @author jasonw
 * 
 */
public class ArrayTimedEventSource implements TimedEventSource
{

	ArrayList< TimedEvent > events = new ArrayList< TimedEvent >();

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean add( TimedEvent e )
	{
		return events.add( e );
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean addAll( Collection< ? extends TimedEvent > c )
	{
		return events.addAll( c );
	}

	/**
	 * 
	 */
	public void clear()
	{
		events.clear();
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean contains( Object o )
	{
		return events.contains( o );
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	public boolean containsAll( Collection< ? > c )
	{
		return events.containsAll( c );
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public TimedEvent get( int index )
	{
		return events.get( index );
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public int indexOf( Object o )
	{
		return events.indexOf( o );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return events.isEmpty();
	}

	/**
	 * 
	 * @return
	 */
	public Iterator< TimedEvent > iterator()
	{
		return events.iterator();
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public TimedEvent remove( int index )
	{
		return events.remove( index );
	}

	/**
	 * 
	 * @param index
	 * @param element
	 * @return
	 */
	public TimedEvent set( int index, TimedEvent element )
	{
		return events.set(
			index,
			element );
	}

	/**
	 * 
	 * @return
	 */
	public int size()
	{
		return events.size();
	}

	Iterator< TimedEvent > eventIterator = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getNext()
	 */
	@Override
	public TimedEvent getNext()
	{
		if (eventIterator == null)
		{
			eventIterator = events.iterator();
		}

		return eventIterator.next();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#reset()
	 */
	@Override
	public void reset()
	{
		eventIterator = null;

	}

	private TimedEvent initial = null;
	private TimedEvent post = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getInitial()
	 */
	@Override
	public TimedEvent getInitial()
	{
		return initial;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getPost()
	 */
	@Override
	public TimedEvent getPost()
	{
		return post;
	}

}
