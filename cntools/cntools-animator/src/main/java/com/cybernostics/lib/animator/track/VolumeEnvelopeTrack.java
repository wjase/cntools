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

import com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction;
import com.cybernostics.lib.animator.track.sound.SoundEffectTrack;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 * 
 */
public class VolumeEnvelopeTrack extends BasicTimerTrack
{

	SoundEffectTrack toManage = null;
	ParamaterisedFunction< Double > envelope = null;

	/**
	 * @param name
	 * @param envelope 
	 * @param toManage 
	 * @param duration
	 */
	public VolumeEnvelopeTrack(
		String name,
		ParamaterisedFunction< Double > envelope,
		SoundEffectTrack toManage,
		long duration )
	{
		super( name, duration );
		this.toManage = toManage;
		this.envelope = envelope;
	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void update( float t )
	{
		double newVolume = envelope.getPoint( t );
		try
		{
			toManage.setVolume( newVolume );
		}
		catch (ResourceFinderException ex)
		{
			Logger.getLogger(
				VolumeEnvelopeTrack.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}
}
