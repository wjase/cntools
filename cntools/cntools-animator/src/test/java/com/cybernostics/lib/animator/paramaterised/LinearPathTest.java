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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class LinearPathTest
{

	public LinearPathTest()
	{
	}

	/**
	 * Test of getAngle method, of class LinearPath.
	 */
	@Test
	public void testGetAngle()
	{
		System.out.println( "getAngle" );
		assertEquals(
			45,
			LinearPath.create(
				0,
				0,
				1,
				1 )
				.getAngle(),
			1 );
		assertEquals(
			0,
			LinearPath.create(
				0,
				0,
				1,
				0 )
				.getAngle(),
			1 );
		assertEquals(
			-45,
			LinearPath.create(
				0,
				0,
				1,
				-1 )
				.getAngle(),
			1 );
		assertEquals(
			90,
			LinearPath.create(
				0,
				0,
				0,
				-1 )
				.getAngle(),
			1 );
		assertEquals(
			180,
			LinearPath.create(
				0,
				0,
				-1,
				0 )
				.getAngle(),
			1 );
		assertEquals(
			-90,
			LinearPath.create(
				0,
				0,
				0,
				1 )
				.getAngle(),
			1 );
	}

}
