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
import java.util.Collection;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class FilteredCollectionTest
{

	public FilteredCollectionTest()
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
	 * Test of addAll method, of class FilteredCollection.
	 */
	@Test
	public void testAddAll()
	{
		System.out.println( "addAll" );
		Collection< String > c = new ArrayList< String >();
		String[] items =
		{ "BlueBoy", "BlueSock", "BlueCar", "RedSock", "RedCar" };
		c.addAll( Arrays.asList( items ) );
		FilteredCollection< String > instance = new FilteredCollection< String >( new CollectionFilter< String >()
		{

			@Override
			public boolean include( String anItem )
			{
				return anItem.startsWith( "Blue" );
			}

		},
			c );

		for (String eaString : instance)
		{
			System.out.printf(
				"%s\n",
				eaString );
		}

		Assert.assertEquals(
			3,
			instance.size() );

	}

}
