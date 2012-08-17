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

import com.cybernostics.lib.animator.sprite.IconSprite;
import com.cybernostics.lib.animator.sprite.SVGSprite;
import com.cybernostics.lib.animator.track.*;
import com.cybernostics.lib.animator.track.ordering.TrackContainer;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.net.URL;
import java.util.List;

/**
 * @author jasonw
 *
 */
public class AnimatedCharacter
{

	private CharacterSpeechTrack currentSpeechTrack = null;

	private URL currentSpeech = null;

	private TrackContainer sequencer = null;

	private String name;

	private SVGArticulatedIcon iconToAnimate = null;

	private Finder loader = null;

	private void setCurrentSpeech( URL s )
	{
		currentSpeech = s;
		if (s == null)
		{
			if (currentSpeechTrack != null)
			{
				currentSpeechTrack.stop( true );
				currentSpeechTrack = null;

			}
		}
	}

	/**
	 *
	 * @return
	 */
	public URL getCurrentSpeech()
	{
		return this.currentSpeech;
	}

	/**
	 *
	 * @return
	 */
	public SVGArticulatedIcon getIcon()
	{
		return iconToAnimate;
	}

	/**
	 *
	 * @param icon
	 */
	public AnimatedCharacter( SVGArticulatedIcon icon )
	{
		iconToAnimate = icon;
	}

	/**
	 *
	 * @return
	 */
	public boolean isSpeaking()
	{
		return this.currentSpeech != null;
	}

	/**
	 *
	 * @param toSay
	 * @param MouthPositions
	 * @return
	 */
	public BasicTrack say( SoundEffect toSay )
	{
		if (isSpeaking())
		{
			return null;
		}

		if (currentSpeechTrack != null)
		{
			return null;
		}

		currentSpeechTrack = getSpeech( toSay );

		getSequencer().addAndStartTrack(
			currentSpeechTrack );

		currentSpeechTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				setCurrentSpeech( null );
			}

		} );

		return currentSpeechTrack;
	}

	private List< TimedEvent > mouthPositions;

	public List< TimedEvent > getMouthPositions()
	{
		return mouthPositions;
	}

	public void setMouthPositions( List< TimedEvent > positions )
	{
		mouthPositions = positions;
	}

	/**
	 *
	 * @param speechFile
	 * @param MouthPositions
	 * @return
	 */
	public CharacterSpeechTrack getSpeech( SoundEffect toSay )
		throws ResourceFinderException
	{

		CharacterSpeechTrack newSpeechTrack = new CharacterSpeechTrack( toSay.getFileName(),
			getIcon(),
			null,
			toSay );

		newSpeechTrack.setPositionEventSource( toSay.getLocation() );

		if (newSpeechTrack.getPositionEventSource() == null)
		{
			newSpeechTrack.setTimedEventSource( new RandomTimedEventSource( getMouthPositions() ) );
		}

		newSpeechTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				setCurrentSpeech( null );
			}

		} );

		return newSpeechTrack;

	}

	/**
	 *
	 * @param speechFile
	 * @param MouthPositions
	 * @return
	 */
	public BasicTrack getSpeech( URL speechFile )
		throws ResourceFinderException
	{

		CharacterSpeechTrack newSpeechTrack = new CharacterSpeechTrack( speechFile.toString(),
			getIcon(),
			null,
			new SoundEffect( speechFile, 0.7 ) );

		newSpeechTrack.setPositionEventSource( speechFile );

		if (newSpeechTrack.getPositionEventSource() == null)
		{
			newSpeechTrack.setTimedEventSource( new RandomTimedEventSource( getMouthPositions() ) );
		}

		newSpeechTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				setCurrentSpeech( null );
			}

		} );

		return newSpeechTrack;

	}

	/**
	 *
	 * @param speechFile
	 * @param MouthPositions
	 * @return
	 */
	public BasicTrack say( URL speechFile ) throws ResourceFinderException
	{
		//System.out.println( "say "+speechFile);
		if (isSpeaking())
		{
			return null;
		}

		if (currentSpeechTrack != null)
		{
			return null;
		}

		currentSpeech = speechFile;
		currentSpeechTrack = new CharacterSpeechTrack( speechFile.toString(),
			getIcon(),
			null,
			new SoundEffect(
				speechFile, 0.7 ) );

		currentSpeechTrack.setPositionEventSource( speechFile );

		if (currentSpeechTrack.getPositionEventSource() == null)
		{
			currentSpeechTrack.setTimedEventSource( new RandomTimedEventSource( getMouthPositions() ) );
		}

		getSequencer().addAndStartTrack(
			currentSpeechTrack );

		currentSpeechTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				setCurrentSpeech( null );

				currentSpeechTrack = null;
			}

		} );

		return currentSpeechTrack;
	}

	/**
	 * @param theSequencer the theSequencer to set
	 */
	public void setSequencer( TrackContainer theSequencer )
	{
		this.sequencer = theSequencer;
	}

	/**
	 * @return the theSequencer
	 */
	public TrackContainer getSequencer()
	{
		return sequencer;
	}

	/**
	 * @param loader the loader to set
	 */
	public void setFinder( Finder loader )
	{
		this.loader = loader;
	}

	/**
	 * @return the loader
	 */
	public Finder getFinder()
	{
		return loader;
	}

	/**
	 * @param name
	 * @param movements
	 */
	public void addPeriodicTrack( String name, TimedEventSource movements )
	{
		CharacterAnimatorTrack newTrack = new CharacterAnimatorTrack( name,
			getIcon() );
		newTrack.setLoader( getFinder() );
		newTrack.setTimedEventSource( movements );
		getSequencer().addAndStartTrack(
			newTrack );
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	public IconSprite createSprite()
	{
		SVGSprite created = new SVGSprite( getName(), getIcon() );
		created.setAnimatedRegions( true );
		return created;

	}

	private SingletonInstance< IconSprite > sprite = new SingletonInstance< IconSprite >()
	{

		@Override
		protected IconSprite createInstance()
		{
			return createSprite();
		}

	};

	public IconSprite getSprite()
	{
		return sprite.get();
	}

}
