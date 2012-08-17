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
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class ArrayUtilsTest
{

	public ArrayUtilsTest()
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
	 * Test of merge method, of class ArrayUtils.
	 */
	@Test
	public void testMerge()
	{
		System.out.println( "merge" );
		String[] first =
		{ "first", "second", "third" };
		String[] second =
		{ "fourth", "fifth", "sixth" };
		String[] expResult =
		{ "first", "second", "third", "fourth", "fifth", "sixth" };
		String[] result = ArrayUtils.merge(
			String.class,
			first,
			second );
		Assert.assertEquals(
			"Doesn't match",
			expResult,
			result );
	}

	/**
	 * Test of implode method, of class ArrayUtils.
	 */
	@Test
	public void testImplode_String_Collection()
	{
		System.out.println( "implode" );
		String glueString = ",";
		ArrayList< String > inputArray = new ArrayList< String >();
		inputArray.add( "first" );
		inputArray.add( "second" );
		inputArray.add( "third" );

		String expResult = "first,second,third";
		String result = ArrayUtils.implode(
			glueString,
			inputArray );
		assertEquals(
			expResult,
			result );
	}

	/**
	 * Test of implode method, of class ArrayUtils.
	 */
	@Test
	public void testImplode_String_StringArr()
	{
		System.out.println( "implode" );
		String glueString = ",";
		String[] inputArray =
		{ "first", "second", "third" };
		String expResult = "first,second,third";
		String result = ArrayUtils.implode(
			glueString,
			inputArray );
		assertEquals(
			expResult,
			result );
	}

	/**
	 * Test of asList method, of class ArrayUtils.
	 */
	@Test
	public void testAsList()
	{
		System.out.println( "asList" );

		String[] expResult =
		{ "first", "second", "third" };
		String[] result = ArrayUtils.asList(
			"first",
			"second",
			"third" );
		Assert.assertEquals(
			"Doesn't match",
			expResult,
			result );
	}

}
