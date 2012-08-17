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

package com.cybernostics.lib.animator.ui.transitions;

import com.cybernostics.lib.animator.paramaterised.Point2DFunction;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class Transitions extends Point2DFunction
{

	Point2DFunction inner = null;
	EasingFunction easeFn = null;

	public Transitions( Point2DFunction toEase, EasingFunction easeFn )
	{
		this.inner = toEase;
		this.easeFn = easeFn;
	}

	@Override
	public Point2D getPoint( float t )
	{
		return inner.getPoint( easeFn.map( t ) );
	}

	public static Transitions easeInQuad( Point2DFunction toEase )
	{
		return new Transitions( toEase, EasingFunction.easeInQuad.get() );
	}

}
