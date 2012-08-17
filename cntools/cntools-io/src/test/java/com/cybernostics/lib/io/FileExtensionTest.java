package com.cybernostics.lib.io;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class FileExtensionTest
{

	public FileExtensionTest()
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
	 * Test of get method, of class FileExtension.
	 */
	@Test
	public void testGet()
	{
		System.out.println( "get" );
		String filename = "somefile.ext";
		String expResult = "ext";
		String result = FileExtension.get( filename );
		assertEquals(
			expResult,
			result );
		String filename2 = "somefile.longext";
		String expResult2 = "longext";
		String result2 = FileExtension.get( filename2 );
		assertEquals(
			expResult2,
			result2 );
	}

}
