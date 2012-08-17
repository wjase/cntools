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

package com.cybernostics.lib.regex;

import junit.framework.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class RegexTest
{

	public RegexTest()
	{
	}

	/**
	 * Test of find method, of class Regex.
	 */
	@Test
	public void testFind_String()
	{
		System.out.println( "find" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "brow.*x" );
		assertTrue( instance.find( toMatchStr ) );
	}

	/**
	 * Test of find method, of class Regex.
	 */
	@Test
	public void testFind_String_int()
	{
		System.out.println( "find" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "brow.*x" );
		assertFalse( instance.find(
			toMatchStr,
			15 ) );
		instance.reset();
		assertTrue( instance.find(
			toMatchStr,
			3 ) );

	}

	/**
	 * Test of find method, of class Regex.
	 */
	@Test
	public void testReplaceall()
	{
		System.out.println( "replaceall" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "o" );
		String result = instance.replaceAll(
			toMatchStr,
			"a" );
		assertEquals(
			result,
			"The quick brawn fax jumps aver a lazy dag" );

	}

	/**
	 * Test of find method, of class Regex.
	 */
	@Test
	public void testFind_0args()
	{
		System.out.println( "find" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "brow.*x" ).setInput( toMatchStr );
		assertTrue( instance.find() );
	}

	/**
	 * Test of find method, of class Regex.
	 */
	@Test
	public void testFind_int()
	{
		System.out.println( "find" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "brow.*x" ).setInput( toMatchStr );
		assertTrue( instance.find( 3 ) );
		assertFalse( instance.find( 15 ) );
	}

	//    /**
	//     * Test of start method, of class Regex.
	//     */
	//    @Test
	//    public void testStart()
	//    {
	//        System.out.println( "start" );
	//        int group = 0;
	//        Regex instance = null;
	//        int expResult = 0;
	//        int result = instance.start( group );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	/**
	 * Test of matches method, of class Regex.
	 */
	@Test
	public void testMatches()
	{
		System.out.println( "matches" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "The.+brow.*x.+dog" ).setInput( toMatchStr );
		assertTrue( instance.matches() );
	}

	/**
	 * Test of groupCount method, of class Regex.
	 */
	@Test
	public void testGroupCount()
	{
		System.out.println( "groupcount" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "(\\w+)\\s(\\w+)\\s(\\w+)" ).setInput( toMatchStr );
		Assert.assertTrue( instance.find() );
		Assert.assertEquals(
			instance.groupCount(),
			3 );
	}

	/**
	 * Test of group method, of class Regex.
	 */
	@Test
	public void testGroup_int()
	{
		System.out.println( "group" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "(\\w+)\\s(\\w+)" ).setInput( toMatchStr );
		instance.dumpGroups();
		instance.reset();
		assertEquals(
			instance.group( 2 ),
			"quick" );
	}

	/**
	 * Test of group method, of class Regex.
	 */
	@Test
	public void testGroup_0args()
	{
		System.out.println( "group" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "(?:(\\S+)\\s?)*" ).setInput( toMatchStr );
		assertEquals(
			instance.group(),
			toMatchStr );
	}

	//    /**
	//     * Test of end method, of class Regex.
	//     */
	//    @Test
	//    public void testEnd_int()
	//    {
	//        System.out.println( "end" );
	//        int group = 0;
	//        Regex instance = null;
	//        int expResult = 0;
	//        int result = instance.end( group );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//    /**
	//     * Test of end method, of class Regex.
	//     */
	//    @Test
	//    public void testEnd_0args()
	//    {
	//        System.out.println( "end" );
	//        Regex instance = null;
	//        int expResult = 0;
	//        int result = instance.end();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//    /**
	//     * Test of appendTail method, of class Regex.
	//     */
	//    @Test
	//    public void testAppendTail()
	//    {
	//        System.out.println( "appendTail" );
	//        StringBuffer sb = null;
	//        Regex instance = null;
	//        StringBuffer expResult = null;
	//        StringBuffer result = instance.appendTail( sb );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of appendReplacement method, of class Regex.
	//     */
	//    @Test
	//    public void testAppendReplacement()
	//    {
	//        System.out.println( "appendReplacement" );
	//        StringBuffer sb = null;
	//        String replacement = "";
	//        Regex instance = null;
	//        Matcher expResult = null;
	//        Matcher result = instance.appendReplacement( sb, replacement );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	/**
	 * Test of iterator method, of class Regex.
	 */
	@Test
	public void testIterator()
	{
		System.out.println( "iterator" );

		System.out.println( "group" );
		String toMatchStr = "The quick brown fox jumps over a lazy dog";
		Regex instance = new Regex( "(\\S+)" ).setInput( toMatchStr );

		int count = 0;
		for (String sEach : instance)
		{
			System.out.println( String.format(
				"Iterating %d : %s",
				count,
				sEach ) );
			++count;
		}

	}

}
