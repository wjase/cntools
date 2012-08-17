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
import com.cybernostics.lib.animator.track.BasicTimerTrack;

/**
 * @author jasonw
 * 
 */
public abstract class SpriteAnimatorTrack extends BasicTimerTrack
{
	/**
	 * @param name
	 * @param duration
	 */
	public SpriteAnimatorTrack( String name, long duration )
	{
		super( name, duration );
		// TODO Auto-generated constructor stub
	}

	private ISprite target;

	/**
	 * 
	 * @param frameSprite
	 */
	public void setTarget( ISprite frameSprite )
	{
		target = frameSprite;

	}

	/**
	 * 
	 * @return
	 */
	public ISprite getTarget()
	{
		return target;
	}

}
