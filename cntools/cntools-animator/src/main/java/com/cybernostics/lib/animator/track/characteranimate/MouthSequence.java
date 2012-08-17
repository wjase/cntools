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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
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
public class MouthSequence
{

	/**
	 * 
	 */
	public MouthSequence()
	{
	}

	private ArrayList< MouthEvent > movements = new ArrayList< MouthEvent >();

	/**
	 * Reads a sequence of MouthEvents from the specified stream
	 * 
	 * @param location
	 *            - URL of the mouth event stream
	 * @return a new Mouth Stream or null of there is an error
	 */
	public static MouthSequence load( URL location ) throws IOException
	{

		return (MouthSequence) ObjectSerialiser.jaxbReadObjectAsXML(
			MouthSequence.class,
			location.openStream() );
	}

	/**
	 * 
	 * @return
	 */
	@XmlElementWrapper
	public ArrayList< MouthEvent > getMovements()
	{
		return movements;
	}

	/**
	 * 
	 * @param movements
	 */
	public void setMovements( ArrayList< MouthEvent > movements )
	{
		this.movements = movements;
	}

	@XmlTransient
	private Iterator< MouthEvent > movementIterator = null;

	/**
	 * 
	 * @param me
	 */
	public void addEvent( MouthEvent me )
	{
		movements.add( me );
	}

	/**
	 * 
	 */
	public void reset()
	{
		movementIterator = movements.iterator();
	}

	/**
	 * 
	 * @return
	 */
	public MouthEvent getNext()
	{
		if (movementIterator.hasNext())
		{
			return movementIterator.next();
		}
		return null;
	}
}
