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

import com.cybernostics.lib.animator.paramaterised.LinearChange;
import com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction;
import com.cybernostics.lib.animator.sprite.ISprite;

/**
 * @author jasonw
 *
 */
public class SpriteFaderTrack extends SpriteAnimatorTrack
{

	private ISprite toFade = null;

	private ParamaterisedFunction< Double > opacity = null;

	/**
	 *
	 * @param name
	 * @param toFade
	 * @param function
	 * @param duration
	 */
	public SpriteFaderTrack(
		String name,
		ISprite toFade,
		ParamaterisedFunction< Double > function,
		long duration )
	{
		super( name, duration );
		this.toFade = toFade;
		opacity = function;
	}

	/**
	 *
	 * @param name
	 * @param toFade
	 * @param opacityStart
	 * @param opacityFinish
	 * @param duration
	 */
	public SpriteFaderTrack(
		String name,
		ISprite toFade,
		float opacityStart,
		float opacityFinish,
		long duration )
	{
		super( name, duration );
		this.toFade = toFade;

		opacity = new LinearChange( opacityStart, opacityFinish );
	}

	/**
	 * @param fractionElapsed a value between 0 and 1 once the duration has
	 * elapsed
	 */
	@Override
	public void update( float fractionElapsed )
	{
		if (fractionElapsed > 1.0f)
		{
			if (!isPeriodic())
			{
				update( 1.0f );
				stop( true );
				return;
			}
		}

		toFade.setTransparency( opacity.getPoint( fractionElapsed ) );
	}

}
