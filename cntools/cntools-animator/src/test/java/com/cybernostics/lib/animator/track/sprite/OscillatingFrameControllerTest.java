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

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author jasonw
 */
public class OscillatingFrameControllerTest
{

	public OscillatingFrameControllerTest()
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
	 * Test of nextFrame method, of class OscillatingFrameController.
	 */
	@Test
	public void testNextFrame()
	{
		System.out.println( "nextFrame" );
		OscillatingFrameController instance = new OscillatingFrameController();
		instance.setFrameCount( 5 );
		int[] expected =
		{ 0, 1, 2, 3, 4, 3, 2, 1, 0, 1, 2, 3, 4 // and on and on
		};

		for (int index = 0; index < expected.length; ++index)
		{
			int result = instance.nextFrame();
			assertEquals(
				String.format(
					"Item %d didn't match",
					index ),
				expected[ index ],
				result );
		}

	}

}
