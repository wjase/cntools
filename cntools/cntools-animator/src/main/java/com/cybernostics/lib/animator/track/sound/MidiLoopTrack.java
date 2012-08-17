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
package com.cybernostics.lib.animator.track.sound;

import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.media.MidiPlayer;
import java.net.URL;

/**
 * Plays a sound midi file while the track is active
 *
 * @author jasonw
 *
 */
public class MidiLoopTrack extends BasicTrack
{

	// private final Sequence midiSequence;
	// double volume;
	//
	// // midi meta-event constant used to signal the end of a track
	// private static final int END_OF_TRACK = 47;
	// private static final int RESET = 88;
	//
	// private static final int VOLUME_CONTROLLER = 7;
	//
	// private Sequencer sequencer;
	// private Synthesizer synthesizer;
	// private String filename;
	//
	// // holds the synthesizer's channels
	// private MidiChannel[] channels;
	boolean started = false;

	MidiPlayer myMidiPlayer;

	/**
	 *
	 * @param name
	 * @param soundFile
	 * @param volume
	 * @throws Exception
	 */
	public MidiLoopTrack( String name, String soundFile, double volume )
		throws Exception
	{
		super( name );

		myMidiPlayer = new MidiPlayer( soundFile, volume );

	}

	/**
	 *
	 * @param name
	 * @param soundFile
	 * @param volume
	 * @throws Exception
	 */
	public MidiLoopTrack( String name, URL soundFile, double volume )
	{
		super( name );

		myMidiPlayer = new MidiPlayer( soundFile, volume );

	}

	@Override
	public void start()
	{
		// soundClip.setLoopPoints( 0, -1 );
		super.start();

		myMidiPlayer.play();

	}

	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		super.stop( fireEvents );
		myMidiPlayer.stop();
	}

	@Override
	public void update( TimeStamp timeCode )
	{
	}

	public void setVolume( double musicVolume )
	{
		myMidiPlayer.setVolume( musicVolume );
	}

	public double getVolume()
	{
		return myMidiPlayer.getVolume();
	}

	@Override
	public void pause()
	{
		myMidiPlayer.stop();
	}

	@Override
	public void resume()
	{
		myMidiPlayer.play();
	}

}
