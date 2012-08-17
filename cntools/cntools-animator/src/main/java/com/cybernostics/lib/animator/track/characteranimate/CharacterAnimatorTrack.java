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

import com.cybernostics.lib.animator.track.TimedEventTrack;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A frame animator which switches frame elements at specified instants.
 *
 * To use this you need an audio clip and a PositionSequence. To create a new
 * sequence: Open the clip in an audio editor (eg audacity)
 *
 * Create a new PositionSequence file matching the chunks of sound in the audio
 * clip with the character position corresponding to that sound.
 *
 * @author jasonw
 *
 */
public class CharacterAnimatorTrack extends TimedEventTrack
{

	private Finder loader = null;

	/**
	 *
	 * @return
	 */
	public Finder getLoader()
	{
		return loader;
	}

	/**
	 *
	 * @param loader
	 */
	public void setLoader( Finder loader )
	{
		this.loader = loader;
	}

	/**
	 *
	 * @param eventSourceFile
	 */
	public void setPositionEventSource( URL eventSourceFile )
	{
		if (!eventSourceFile.getFile()
			.endsWith(
				".seq" ))
		{
			try
			{
				eventSourceFile = new URL( eventSourceFile, ".seq" );
			}
			catch (MalformedURLException ex)
			{
				Logger.getLogger(
					CharacterAnimatorTrack.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
		setTimedEventSource( PositionSequence.load( eventSourceFile ) );
	}

	/**
	 *
	 * @param name
	 * @param toAnimate
	 */
	public CharacterAnimatorTrack( String name, SVGArticulatedIcon toAnimate )
	{
		super( name );
		setName( name );
		setEventTarget( toAnimate );
	}

}
