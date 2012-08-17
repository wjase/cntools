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
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * Plays a sound clip once
 *
 * @author jasonw
 *
 */
public class SoundEffectTrack extends BasicTrack
	implements
	PropertyChangeListener
{

	private SoundEffect theEffect;

	/**
	 *
	 * @param effect
	 * @return
	 */
	public static SoundEffectTrack create( SoundEffect effect )
	{
		return new SoundEffectTrack( effect );
	}

	/**
	 *
	 * @param effect
	 */
	public SoundEffectTrack( SoundEffect effect )
	{
		super( "SoundEffectTrack:" + effect.getFileName() );
		setSoundEffect( effect );
	}

	/**
	 *
	 * @param name
	 * @param effect
	 */
	public SoundEffectTrack( String name, SoundEffect effect )
	{
		super( name );
		setSoundEffect( effect );
	}

	/**
	 *
	 * @param name
	 * @param soundFile
	 * @param volume
	 * @param loopCount
	 */
	public SoundEffectTrack(
		String name,
		URL soundFile,
		double volume,
		int loopCount ) throws ResourceFinderException
	{
		super( name );
		setSoundEffect( new SoundEffect( soundFile, volume ) );

		theEffect.setLoopCount( loopCount );
	}

	/**
	 *
	 * @param name
	 * @param soundFile
	 * @param volume
	 */
	public SoundEffectTrack( String name, URL soundFile, double volume )
		throws ResourceFinderException
	{
		super( name );

		setSoundEffect( new SoundEffect( soundFile, volume ) );
	}

	/**
	 *
	 * @param volume
	 */
	public void setVolume( double volume ) throws ResourceFinderException
	{
		theEffect.setVolume( volume );
	}

	private void setSoundEffect( SoundEffect effect )
	{
		this.theEffect = effect;
		theEffect.addPropertyChangeListener(
			SoundEffect.STOPPED,
			this );
	}

	@Override
	public void start()
	{
		if (isRunning())
		{
			return;
		}
		super.start();

		//System.out.println( "Starting sound effect track " + theEffect.getFileName() );

		try
		{
			theEffect.getLoadTask()
				.get();
			theEffect.play();
		}
		catch (Exception e1)
		{
			UnhandledExceptionManager.handleException( e1 );
		}

	}

	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		//System.out.println( "Stopping sound effect track " + theEffect.getFileName() );
		if (theEffect.isPlaying())
		{
			theEffect.stop();
		}

		super.stop( fireEvents );
	}

	@Override
	public void update( TimeStamp timeCode )
	{
		fireTrackUpdated( timeCode );
	}

	/*
	 * (non-Javadoc) @see
	 * java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		super.stop( true );

	}

	@Override
	public void pause()
	{
		theEffect.stop();
	}

	@Override
	public void resume()
	{
		theEffect.play();
	}

}
