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

/**
 * Frame sequence starts at 0 goes up to maxIndex then backwards through to 0.
 * FOr harmonic motion type animations like swings
 *
 * @author jasonw
 */
public class OscillatingFrameController implements FrameController
{

	private static final int UP = 1;

	private static final int DOWN = -1;

	private int delta = UP;

	private int maxIndex = 0;

	private int currentFrame = -1;

	@Override
	public int nextFrame()
	{
		if (maxIndex == 0)
		{
			currentFrame = 0;
			return currentFrame;
		}
		if (currentFrame == maxIndex)
		{
			delta = DOWN;
		}
		else
			if (currentFrame == 0)
			{
				delta = UP;
			}
			else
				if (currentFrame == -1)
				{
					currentFrame = 0;
					return currentFrame;
				}
		currentFrame += delta;
		return currentFrame;
	}

	@Override
	public void setFrameCount( int frameCount )
	{
		this.maxIndex = frameCount - 1; // zero-based
	}

}
