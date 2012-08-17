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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cybernostics.lib.animator.track.TimedEvent;
import com.cybernostics.lib.animator.track.TimedEventSource;
import com.cybernostics.lib.persist.xml.ObjectSerialiser;

/**
 * A sequence of mouth events (position + timestamp) for animation The position
 * is one of the eight basic shapes for lip sync animation (See
 * http://www.informit.com/articles/article.aspx?p=23581)
 * 
 * @author jasonw
 * 
 */
@XmlRootElement
public class PositionSequence implements TimedEventSource
{

	/**
	 * 
	 */
	public PositionSequence()
	{

	}

	// <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
	// <positionSequence>
	// <movements>
	// <movements timestamp="0" position="head-front.mouth-closed"/>
	// <movements timestamp="212" position="head-front.mouth-eee"/>
	// <movements timestamp="326" position="head-front.mouth-ahh"/>
	// <movements timestamp="584" position="head-front.mouth-eee"/>
	// <movements timestamp="686" position="head-front.mouth-closed"/>
	// </movements>
	// </positionSequence>
	/**
	 * Generates a random mouth sequence for the specified time
	 * 
	 * @param toPlay
	 * @return  
	 */
	public static PositionSequence generateRandom( long toPlay )
	{
		PositionSequence seq = new PositionSequence();

		long current = 0;

		seq.addEvent(
			"head-front.mouth-closed",
			0 );

		String[] positions =
		{ "mouth-ahh", "mouth-closed", "mouth-ooh", "mouth-fff" };
		int toggle = 0;

		while (current < toPlay)
		{
			current += ( 50 + Math.random() * 250 ); // 50-300 milliseconds
			seq.addEvent(
				positions[ toggle % 4 ],
				current );
			++toggle;
		}

		seq.addEvent(
			"mouth-closed",
			toPlay ); // close mouth at end
		return seq;
	}

	private ArrayList< PositionEvent > movements = new ArrayList< PositionEvent >();

	/**
	 * Reads a sequence of MouthEvents from the specified stream
	 * 
	 * @param location
	 *            - URL of the mouth event stream
	 * @return a new Mouth Stream or null of there is an error
	 */
	public static PositionSequence load( URL location )
	{
		URL toLoad = location;

		if (toLoad == null)
		{
			return null;
		}

		// if the url is the accompanying sound clip
		if (!toLoad.toString()
			.endsWith(
				".seq" ))
		{
			String urlString = toLoad.toString();
			int index = urlString.lastIndexOf( '.' );
			try
			{
				toLoad = new URL( urlString.substring(
					0,
					index ) + ".seq" );
				return load( toLoad.openStream() );
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static PositionSequence load( InputStream data ) throws IOException
	{
		return (PositionSequence) ObjectSerialiser.jaxbReadObjectAsXML(
			PositionSequence.class,
			data );
	}

	/**
	 * 
	 * @param position
	 * @param timeCode
	 */
	public void addEvent( String position, long timeCode )
	{
		movements.add( new PositionEvent( new PartPositionPath( position ),
			timeCode ) );
	}

	/**
	 * 
	 * @return
	 */
	@XmlElementWrapper
	public ArrayList< PositionEvent > getMovements()
	{
		return movements;
	}

	/**
	 * 
	 * @param movements
	 */
	public void setMovements( ArrayList< PositionEvent > movements )
	{
		this.movements = movements;
	}

	@XmlTransient
	private Iterator< PositionEvent > movementIterator = null;

	/**
	 * 
	 * @param me
	 */
	public void addEvent( PositionEvent me )
	{
		movements.add( me );
	}

	public void reset()
	{
		movementIterator = movements.iterator();
	}

	public PositionEvent getNext()
	{
		if (movementIterator.hasNext())
		{
			return movementIterator.next();
		}
		return null;
	}

	private TimedEvent initial = null;

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.animator.track.TimedEventSource#getInitial()
	 */
	@Override
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
		// TODO Auto-generated method stub
		return null;
	}

}
