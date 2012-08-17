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

import com.cybernostics.lib.animator.sprite.SVGSprite;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.net.URL;

/**
 * A speaking character class. This class ensures that a character isn't saying two things at the same time.
 * It also does standard animation on the mouth.
 * @author jasonw
 *
 */
public class SpeakingCharacter
{

	private BasicTrack currentSpeechTrack = null;
	private URL currentSpeech = null;

	/**
	 * 
	 * @param speechSpec
	 * @return
	 */
	public BasicTrack getSpeechTrack( URL speechSpec )
		throws ResourceFinderException
	{
		return new CharacterSpeechTrack( speechSpec.toString(),
			(SVGArticulatedIcon) myCharacter.getIcon(),
			null,
			new SoundEffect( speechSpec, 0.7 ) );
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSpeaking()
	{
		return ( currentSpeechTrack != null && !currentSpeechTrack.isStopped() );
	}

	/**
	 * 
	 * @param speechSpec
	 * @param speechTrack
	 * @return
	 */
	public BasicTrack addSpeech( URL speechSpec, BasicTrack speechTrack )
	{
		if (currentSpeech != null && currentSpeech.equals( speechSpec ))
		{
			if (isSpeaking())
			{
				return null;
			}
		}

		currentSpeech = speechSpec;
		if (isSpeaking())
		{
			currentSpeechTrack.startAfterMe( speechTrack );
		}
		currentSpeechTrack = speechTrack;
		mySequencer.addAndStartTrack( currentSpeechTrack );

		return currentSpeechTrack;

	}

	/**
	 * 
	 * @param speechSpec
	 * @return
	 */
	public BasicTrack say( URL speechSpec ) throws ResourceFinderException
	{

		if (currentSpeechTrack != null && !currentSpeechTrack.isStopped())
		{
			if (currentSpeech.equals( speechSpec ))
			{
				return null;
			}
			currentSpeechTrack.stop( true );
			currentSpeechTrack = null;
		}

		currentSpeech = speechSpec;
		currentSpeechTrack = new CharacterSpeechTrack( speechSpec.toString(),
			(SVGArticulatedIcon) myCharacter
				.getIcon(),
			null,
			new SoundEffect( speechSpec, 0.7 ) );

		mySequencer.addAndStartTrack( currentSpeechTrack );

		return currentSpeechTrack;
	}

	private Sequencer mySequencer = null;
	private SVGSprite myCharacter = null;

	/**
	 * 
	 * @param seq
	 * @param characterSprite
	 */
	public SpeakingCharacter( Sequencer seq, SVGSprite characterSprite )
	{
		mySequencer = seq;
		myCharacter = characterSprite;
		myCharacter.setAnimatedRegions( true ); // cause we're going to animate you!
	}
}
