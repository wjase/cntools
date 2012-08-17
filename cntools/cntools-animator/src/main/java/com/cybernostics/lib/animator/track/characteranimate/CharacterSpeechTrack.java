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

import com.cybernostics.lib.animator.track.TimedEvent;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.animator.track.sound.SoundEffectTrack;
import com.cybernostics.lib.media.SoundEffect;

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
public class CharacterSpeechTrack extends CharacterAnimatorTrack
{

	private SoundEffectTrack speechTrack = null;

	//    /**
	//     * 
	//     * @param name
	//     * @param toAnimate
	//     * @param soundClip
	//     */
	//    public CharacterSpeechTrack( String name, SVGArticulatedIcon toAnimate, String soundClip ) throws ResourceFinderException
	//	{
	//		super( name, toAnimate );
	//		setPositionEventSource( soundClip + ".seq" );
	//
	//		setSpeechTrack( new SoundEffectTrack( new SoundEffect( soundClip + ".mp3", 0.9 ) ) );
	//	}

	/**
	 * 
	 * @param name
	 * @param toAnimate
	 * @param events
	 * @param toPlay
	 */
	public CharacterSpeechTrack(
		String name,
		SVGArticulatedIcon toAnimate,
		PositionSequence events,
		SoundEffect toPlay )
	{
		super( name, toAnimate );
		setName( name );
		setTimedEventSource( events );

		setSpeechTrack( new SoundEffectTrack( name, toPlay ) );
	}

	/**
	 * @param soundEffectTrack
	 */
	private void setSpeechTrack( SoundEffectTrack soundEffectTrack )
	{
		speechTrack = soundEffectTrack;
		startWithMe( speechTrack );

		// first one to stop stops the other
		speechTrack.stopAfterMe( this );
		stopAfterMe( speechTrack );

		speechTrack.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				TimedEvent post = CharacterSpeechTrack.this.getPositionEventSource()
					.getPost();
				if (post != null)
				{
					post.execute(
						CharacterSpeechTrack.this,
						getEventTarget() );
				}
			}
		} );

	}

	/**
	 * 
	 * @return
	 */
	public SoundEffectTrack getSpeechTrack()
	{
		return speechTrack;
	}

}
