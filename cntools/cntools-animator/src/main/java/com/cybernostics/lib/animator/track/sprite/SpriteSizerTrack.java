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

import com.cybernostics.lib.animator.sprite.BitmapSprite;
import com.cybernostics.lib.animator.paramaterised.LinearChange;
import com.cybernostics.lib.maths.DimensionFloat;

/**
 * 
 * @author jasonw
 */
public class SpriteSizerTrack extends SpriteAnimatorTrack
{

	LinearChange widthChanger = null;
	LinearChange heightChanger = null;

	/**
	 * 
	 * @param name
	 * @param toSize
	 * @param startSize
	 * @param stopSize
	 * @param duration
	 */
	public SpriteSizerTrack(
		String name,
		BitmapSprite toSize,
		DimensionFloat startSize,
		DimensionFloat stopSize,
		long duration )
	{
		super( name, duration );

		setTarget( toSize );

		widthChanger = new LinearChange( (float) startSize.getWidth(),
			(float) stopSize.getWidth() );
		heightChanger = new LinearChange( (float) startSize.getHeight(),
			(float) stopSize.getHeight() );

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
		getTarget().setRelativeSize(
			widthChanger.getPoint( t ),
			heightChanger.getPoint( t ) );

	}

}
