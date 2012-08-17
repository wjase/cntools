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

package com.cybernostics.lib.animator.sprite.animators;

import com.cybernostics.lib.animator.paramaterised.LinearChangeDouble;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.track.AdapterAnimatorTrack;
import com.cybernostics.lib.animator.track.AnimatedProperty;

/**
 *
 * @author jasonw
 */
public class SpriteScaler extends SpritePropertyAnimator
	implements
	AnimatedProperty< Double >
{

	public static AdapterAnimatorTrack< Double > newTrack( final ISprite toScale,
		double from,
		double to,
		long duration )
	{
		AdapterAnimatorTrack< Double > toRun = new AdapterAnimatorTrack< Double >( "Scale"
			+ toScale.getId(),
			duration,
			LinearChangeDouble.create(
				from,
				to ),
			new SpriteScaler( toScale ) );

		return toRun;
	}

	public SpriteScaler( ISprite value )
	{
		super( value );
	}

	@Override
	public void update( Double value )
	{
		toAnimate.setScale( value );
	}

}
