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

import com.cybernostics.lib.animator.paramaterised.LinearChange;

/**
 *
 * @author jasonw
 */
public class CurtainFader
{

	public static Track fadeIn( final FadingScreen toAnimate, long duration )
	{
		Track t = new AdapterAnimatorTrack< Double >( "fadein",
			duration,
			LinearChange.get(
				1.0f,
				0 ),
			new AnimatedProperty< Double >()
			{

				@Override
				public void update( Double value )
				{
					toAnimate.setTransparency( value );
				}

			} );
		return t;

	}

	public static Track fadeOut( final FadingScreen toAnimate, long duration )
	{
		Track t = new AdapterAnimatorTrack< Double >( "fadeout",
			duration,
			LinearChange.get(
				0f,
				1.0f ),
			new AnimatedProperty< Double >()
			{

				@Override
				public void update( Double value )
				{
					toAnimate.setTransparency( value );
				}

			} );
		return t;

	}

	private CurtainFader()
	{
	}

}
