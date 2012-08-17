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

package com.cybernostics.lib.animator.paramaterised;

import java.awt.geom.Point2D;

/**
 * Alters the function by adding a constant offset to the supplied function
 *
 * Eg given a function which draws a line and an offset of 0.5, this function
 * starts "half way through" (t = 0.5) the supplied fn, goes to 1.0 and then up
 * from 0.0 finishing at 0.5.
 *
 * @author jasonw
 */
public class Offset2DFunction extends Point2DFunction
{

	private Offset2DFunction toOffset;

	private float offsetAmount = 0f;

	public Offset2DFunction( Offset2DFunction toOffset, float offsetAmount )
	{
		this.offsetAmount = offsetAmount;
		this.toOffset = toOffset;
	}

	@Override
	public Point2D getPoint( float t )
	{
		return toOffset.getPoint( ( t + offsetAmount ) % 1.0f );
	}

}
