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

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class PointXAnimator extends Point2DFunction
{

	LinearChangeInteger xChanger;

	/**
	 *
	 * @param start
	 * @param stop
	 */
	public PointXAnimator( int start, int stop )
	{
		xChanger = new LinearChangeInteger( start, stop );
	}

	@Override
	public Point2D getPoint( float t )
	{
		Point2D toAnimate = getPoint();
		getPoint().setLocation(
			xChanger.getPoint( t ),
			toAnimate.getY() );
		return toAnimate;
	}

}
