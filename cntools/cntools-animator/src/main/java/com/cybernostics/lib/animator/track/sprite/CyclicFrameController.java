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
 * 
 * @author jasonw
 */
public class CyclicFrameController implements FrameController
{

	private int frameCount = 0;
	private int currentFrame = 0;

	@Override
	public void setFrameCount( int frameCount )
	{
		this.frameCount = frameCount;
	}

	public CyclicFrameController()
	{

	}

	public CyclicFrameController( int frameCount )
	{
		this.frameCount = frameCount;
	}

	@Override
	public int nextFrame()
	{
		++currentFrame;
		currentFrame %= frameCount;
		return currentFrame;

	}

}
