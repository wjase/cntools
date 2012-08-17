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

package com.cybernostics.lib.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class IterableArrayTest
{

	public IterableArrayTest()
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
	 * Test of get method, of class IterableArray.
	 */
	@Test
	public void testGet()
	{
		System.out.println( "get" );
		Integer[] toItArray =
		{ 1, 2, 3, 4, 5, 6 };
		ArrayList< Integer > toPop = new ArrayList< Integer >();
		IterableArray< Integer > aIterable = IterableArray.get( toItArray );

		for (Integer eachInt : aIterable)
		{
			toPop.add( eachInt );
		}

		for (int index = 0; index < toItArray.length; ++index)
		{
			assertEquals(
				toItArray[ index ],
				toPop.get( index ) );
		}
	}

}
