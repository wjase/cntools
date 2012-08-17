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

import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class IntegerReverseComparatorTest
{

	public IntegerReverseComparatorTest()
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
	 * Test of compare method, of class IntegerReverseComparator.
	 */
	@Test
	public void testCompare()
	{
		Integer[] intArray =
		{ 10, 40, 1, 200, 6 };
		Integer[] sortedArray =
		{ 200, 40, 10, 6, 1 };

		Arrays.sort(
			intArray,
			new IntegerReverseComparator() );

		Assert.assertEquals(
			intArray,
			intArray );

	}

}
