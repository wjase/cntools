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
package com.cybernostics.lib.animator.track.sprite;

import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.animator.paramaterised.LinearChange;

/**
 * Scales a sprite based only on the height to maintain aspect ratio of the
 * sprite
 * 
 * @author jasonw
 * 
 */
public class SpriteScalerTrack extends SpriteAnimatorTrack
{

	LinearChange scaler;

	TimeStamp timeCode = new TimeStamp();

	double scaleChangePercentage;

	/**
	 * 
	 * @param name
	 * @param toSize
	 * @param startHeightPercentage
	 * @param stopHeightPercentage
	 * @param duration
	 */
	public SpriteScalerTrack(
		String name,
		ISprite toSize,
		float startHeightPercentage,
		float stopHeightPercentage,
		long duration )
	{
		super( name, duration );

		setTarget( toSize );

		scaler = new LinearChange( startHeightPercentage, stopHeightPercentage );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.BasicTimerTrack#update(float)
	 */
	/**
	 * 
	 * @param t
	 */
	@Override
	public void update( float t )
	{
		getTarget().setScale(
			scaler.getPoint( t ) / 100 );

	}

}
