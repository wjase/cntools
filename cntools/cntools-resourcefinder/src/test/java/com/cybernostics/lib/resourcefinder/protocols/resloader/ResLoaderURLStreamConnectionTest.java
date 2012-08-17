package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.protocols.resloader.ResLoaderURLConnection;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.urlfactory.URLFactory;
import java.net.MalformedURLException;
import java.util.List;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class ResLoaderURLStreamConnectionTest
{

	public ResLoaderURLStreamConnectionTest()
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

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of toLocalURL method, of class ResourceFinderURLStreamConnection.
	 */
	@Test
	public void testToLocalURL()
	{
		try
		{
			//		URL encoded = new URL( "resloader://" + ResLoaderURLStreamConnectionTest.class.getCanonicalName()
			//			+ "/test/afile.txt" );

			URL encoded = ResloaderURLScheme.create(
				ResLoaderURLStreamConnectionTest.class,
				"/test/afile.txt" );
			// calling any method on the resource loader will enable this protocol
			Finder loader = ResourceFinder.get( ResLoaderURLStreamConnectionTest.class );
			URL expResult = loader.getResource( "test/afile.txt" );
			assertNotNull( expResult );
			System.out.println( "toLocalURL" );
			URL result = ResLoaderURLConnection.toLocalURL( encoded );
			assertEquals(
				expResult,
				result );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				ResLoaderURLStreamConnectionTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	/**
	 * Test of toLocalURL method, of class ResourceFinderURLStreamConnection.
	 */
	@Test
	public void testOpenURL() throws Exception
	{
		//Finder loader = ResourceFinder.get( ResLoaderURLConnection.class );
		// calling any method on the resource loader will enable this protocol
		System.out.println( "testOpenURL" );

		URL encoded = ResloaderURLScheme.create(
			ResLoaderURLStreamConnectionTest.class,
			"/test/afile.txt" );

		String readtext = ResourceFinder.readAllFrom( encoded.openStream() );
		System.out.println( readtext );

		assertEquals(
			"Read text did not match",
			readtext.trim(),
			"This is a test" );

	}

	@Test
	public void testRelativeURL() throws Exception
	{
		// calling any method on the resource loader will enable this protocol
		System.out.println( "testRelativeURL" );
		URL encoded = ResloaderURLScheme.create(
			ResLoaderURLStreamConnectionTest.class,
			"/test/afile.txt" );
		// test building relative URL
		URL encodedRoot = ResloaderURLScheme.create( ResLoaderURLStreamConnectionTest.class );
		URL encoded2 = new URL( encodedRoot, "test/afile.txt" );

		assertEquals(
			encoded,
			encoded2 );

	}

	@Test
	public void testGetResources() throws Exception
	{
		URL encodedRoot = ResloaderURLScheme.create(
			ResLoaderURLStreamConnectionTest.class,
			"/test/" );

		Finder finder = ResourceFinder.get( this );
		List< URL > urls = ( (ResourceFinder) finder ).getURLChildResources(
			"",
			encodedRoot,
			null );

		URL encoded2 = URLFactory.newURL( "resloader://"
			+ ResLoaderURLStreamConnectionTest.class.getCanonicalName()
			+ "/test/" );

		assertEquals(
			encoded2,
			encodedRoot );
		assertTrue(
			"FoundResources",
			urls.size() == 3 );

		for (URL url : urls)
		{
			System.out.println( url.toString() );
			System.out.println( ResourceFinder.readAllFrom( url.openStream() ) );
		}

	}
}
