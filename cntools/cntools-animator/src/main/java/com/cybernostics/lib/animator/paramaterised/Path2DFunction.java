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

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

/**
 *
 * @author jasonw
 */
public class Path2DFunction extends Point2DFunction
{

	public static Path2DFunction get( Shape toIterate )
	{
		return new Path2DFunction( toIterate, 0.02f );
	}

	/**
	 * The shape which defines the 2D path to be animated
	 */
	Shape toAnimateShape = null;

	ArrayList< Point2D.Float > pathPoints = new ArrayList< Point2D.Float >();

	int maxPoint = 0;

	/**
	 *
	 * @param toAnimate
	 * @param flatness
	 */
	public Path2DFunction( Shape toAnimate, float flatness )
	{
		this.toAnimateShape = toAnimate;

		PathIterator p = toAnimate.getPathIterator(
			null,
			flatness );

		double[] coords = new double[ 6 ];

		while (!p.isDone())
		{

			int pointType = p.currentSegment( coords );
			//System.out.println( String.format( "x:%4.2f, y:%4.2f", coords[0], coords[1] ) );
			switch (pointType)
			{
				case PathIterator.SEG_LINETO:
				case PathIterator.SEG_MOVETO:
				case PathIterator.SEG_CLOSE:
					pathPoints.add( new Point2D.Float( (float) coords[ 0 ],
						(float) coords[ 1 ] ) );
					break;
			}
			p.next();

		}
		;

		maxPoint = pathPoints.size() - 1;
	}

	@Override
	public Float getPoint( float t )
	{
		return pathPoints.get( (int) ( t * maxPoint ) );
	}

}
