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

import com.cybernostics.lib.animator.sprite.IFrameSprite;

/**
 *
 * @author jasonw
 */
public class FrameAnimator extends SpriteAnimatorTrack
{

	private static final String idFmt = "FrameAnimate%s";

	private FrameController frameController = null;

	public FrameController getFrameController()
	{
		return frameController;
	}

	public final void setFrameController( FrameController frameController )
	{
		this.frameController = frameController;
		frameController.setFrameCount( ( (IFrameSprite) getTarget() ).getFrameCount() );
	}

	/**
	 *
	 * @param name
	 * @param frameSprite
	 * @param showPeriod
	 */
	public FrameAnimator( IFrameSprite frameSprite, long showPeriod )
	{
		this( frameSprite, showPeriod, new CyclicFrameController() );
	}

	/**
	 *
	 * @param frameSprite sprite to control
	 * @param showPeriod time between frames
	 * @param controller this selects which frame to display each period
	 */
	public FrameAnimator(
		IFrameSprite frameSprite,
		long showPeriod,
		FrameController controller )
	{
		super( String.format(
			idFmt,
			frameSprite.getId() ), showPeriod );
		setPeriodic( true );
		setTarget( frameSprite );
		controller.setFrameCount( frameSprite.getFrameCount() );
		setFrameController( controller );

	}

	float lastFraction = 0;

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
		// we've gone from 0->1.0 and back round to 0.0
		if (t < lastFraction)
		{
			( (IFrameSprite) getTarget() ).setCurrentFrameNumber( frameController.nextFrame() );
		}
		lastFraction = t;
	}

}
