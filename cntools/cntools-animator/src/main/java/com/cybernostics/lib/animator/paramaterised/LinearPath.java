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

import com.cybernostics.lib.maths.Slope;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class LinearPath extends Point2DFunction
{

	private Point2D start;

	private double xDistance;

	private double yDistance;

	public static LinearPath create( double x1, double y1, double x2, double y2 )
	{
		return new LinearPath( new Point2D.Double( x1, y1 ),
			new Point2D.Double( x2, y2 ) );
	}

	public static LinearPath create( Point2D start, Point2D stop )
	{
		return new LinearPath( start, stop );
	}

	/**
	 *
	 * @param start
	 * @param stop
	 */
	public LinearPath( Point2D start, Point2D stop )
	{
		this.start = start;
		xDistance = ( stop.getX() - start.getX() );
		yDistance = ( stop.getY() - start.getY() );

	}

	@Override
	public Point2D getPoint( float t )
	{
		Point2D nextPoint = getPoint();
		double xTravelled = xDistance * t;
		double yTravelled = yDistance * t;
		nextPoint.setLocation(
			start.getX() + xTravelled,
			start.getY() + yTravelled );
		return nextPoint;
	}

	public double getAngle()
	{
		return Slope.getAngle(
			xDistance,
			yDistance );
	}

	private static String toStringFormat = "LinearPath(%f,%f,%f,%f)";

	@Override
	public String toString()
	{
		return String.format(
			toStringFormat,
			start.getX(),
			start.getY(),
			xDistance,
			yDistance );
	}

}
