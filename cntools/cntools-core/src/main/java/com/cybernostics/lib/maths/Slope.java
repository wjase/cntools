/*
 * #%L cntools-core %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */

package com.cybernostics.lib.maths;

import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class Slope
{

	public static double getAngle( double xDistance, double yDistance )
	{
		if (xDistance == 0)
		{
			return yDistance > 0 ? -90 : 90;
		}
		if (yDistance == 0)
		{
			return xDistance >= 0 ? 0 : 180;
		}

		return Math.toDegrees( Math.atan2(
			yDistance,
			xDistance ) );

	}

	public static double getAngle( Point2D p1, Point2D p2 )
	{
		return getAngle(
			p2.getX() - p1.getX(),
			-1.0 * ( p2.getY() - p1.getY() ) );
	}

}
