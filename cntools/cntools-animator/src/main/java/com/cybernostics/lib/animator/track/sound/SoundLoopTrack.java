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

import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.animator.track.BasicTrack;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.media.SoundEffect;

/**
 * Plays a sound clip once
 *
 * @author jasonw
 *
 */
public class SoundLoopTrack extends BasicTrack
{

	private SoundEffect soundClip;

	/**
	 *
	 * @param name
	 * @param soundFile
	 * @param volume
	 */
	public SoundLoopTrack(
		String name,
		SoundEffect effect,
		String soundFile,
		double volume )
	{
		super( name );
		try
		{
			soundClip = effect;
			soundClip.setClipVolume( volume );
			soundClip.setLoopCount( SoundEffect.LOOP );

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
	}

	/**
	 *
	 * @param volume
	 */
	public void setVolume( double volume )
	{
		soundClip.setClipVolume( volume );
	}

	@Override
	public void start()
	{
		soundClip.play();
	}

	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		super.stop( fireEvents );
		soundClip.stop();
	}

	@Override
	public void update( TimeStamp timeCode )
	{
	}

	@Override
	public void pause()
	{
		soundClip.pause();
	}

	@Override
	public void resume()
	{
		soundClip.resume();
	}

}
