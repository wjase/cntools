package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.regex.Regex;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class ResourceFinderTest
{

	public ResourceFinderTest()
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
	 * Test of get method, of class ResourceFinder.
	 */
	@Test
	public void testGet_Class()
	{
		try
		{
			System.out.println( "get" );
			Finder result = ResourceFinder.get( ResourceFinder.class );
			assertNotNull( result.getResource( "" ) );
			assertNotNull( result.getResource( "test.txt" ) );
			assertNotNull( result.getResource( "/" ) );
		}
		catch (ResourceFinderException ex)
		{
			Logger.getLogger(
				ResourceFinderTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}

}
