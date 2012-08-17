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

import com.cybernostics.lib.animator.paramaterised.DoubleFunction;
import com.cybernostics.lib.animator.paramaterised.LinearChangeDouble;
import com.cybernostics.lib.animator.paramaterised.SawToothFunction;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.track.*;
import com.cybernostics.lib.animator.track.ordering.SerialTrack;

/**
 *
 * @author jasonw
 */
public class SpriteFader extends SpritePropertyAnimator
	implements
	AnimatedProperty< Double >
{

	public static BasicTimerTrack newTrack( final ISprite toFade,
		DoubleFunction func,
		long duration )
	{
		AdapterAnimatorTrack< Double > toRun = new AdapterAnimatorTrack< Double >( "Fade"
			+ toFade.getId(),
			duration,
			func,
			new SpriteFader( toFade ) );
		return toRun;
	}

	public static BasicTimerTrack cycle( final ISprite toFade,
		double from,
		double to,
		long period )
	{
		BasicTimerTrack t = newTrack(
			toFade,
			new SawToothFunction( to, from ),
			period );
		t.setPeriodic( true );
		return t;
	}

	public static BasicTimerTrack newTrack( final ISprite toFade,
		double from,
		double to,
		long duration )
	{
		AdapterAnimatorTrack< Double > toRun = new AdapterAnimatorTrack< Double >( "Fade"
			+ toFade.getId(),
			duration,
			LinearChangeDouble.create(
				from,
				to ),
			new SpriteFader( toFade ) );

		return toRun;
	}

	public static BasicTimerTrack fadeIn( final ISprite toFade, long duration )
	{
		AdapterAnimatorTrack< Double > toRun = new AdapterAnimatorTrack< Double >( "Fadein"
			+ toFade.getId(),
			duration,
			LinearChangeDouble.create(
				0,
				1 ),
			new SpriteFader( toFade ) );

		return toRun;
	}

	public static BasicTrack fadeInOut( final ISprite toFade,
		long inDuration,
		long showDuration,
		long outDuration )
	{
		return SerialTrack.connect(
			fadeIn(
				toFade,
				inDuration ),
			WaitTrack.create( showDuration ),
			fadeOut(
				toFade,
				outDuration ) );
	}

	public static AdapterAnimatorTrack< Double > fadeOut( final ISprite toFade,
		long duration )
	{
		AdapterAnimatorTrack< Double > toRun = new AdapterAnimatorTrack< Double >( "Fadeout"
			+ toFade.getId(),
			duration,
			LinearChangeDouble.create(
				1,
				0 ),
			new SpriteFader( toFade ) );

		return toRun;
	}

	public SpriteFader( ISprite value )
	{
		super( value );
	}

	@Override
	public void update( Double value )
	{
		toAnimate.setTransparency( value );
	}

}
