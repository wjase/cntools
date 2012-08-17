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
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D.Float;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class Path2DFunctionTest
{

	public Path2DFunctionTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	/**
	 * Test of getPoint method, of class Path2DFunction.
	 */
	@Test
	public void testGetPoint()
	{

		Shape s = new Ellipse2D.Double( 0.2, 0.2, 0.6, 0.6 );

		PathIterator pi = s.getPathIterator(
			null,
			0.02 );

		double[] coords = new double[ 6 ];

		while (!pi.isDone())
		{

			pi.currentSegment( coords );
			System.out.println( String.format(
				"x:%4.2f, y:%4.2f",
				coords[ 0 ],
				coords[ 1 ] ) );
			pi.next();

		}
		;

		Path2DFunction pdf = Path2DFunction.get( s );
		System.out.println( "getPoint" );
		for (float t = 0.0F; t < 1.0f; t = t + 0.05f)
		{
			Float result = pdf.getPoint( t );
			System.out.println( String.format(
				"x:%4.2f, y:%4.2f",
				result.getX(),
				result.getY() ) );
		}

	}

}
