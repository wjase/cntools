package com.cybernostics.lib.net;

import com.cybernostics.lib.io.stream.StringToStream;
import com.cybernostics.lib.persist.xml.XMLPropertyMapEncoder;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.TreeMap;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author jasonw
 *
 */
public class CustomCookieURLConnectionTest
{

	/**
	 * Test method for {@link com.cybernostics.lib.net.CustomCookieHttpConnection#CustomCookieHttpConnection(java.net.URL, java.util.Map, java.util.Map)}.
	 * @throws IOException 
	 */
	@Test
	public void testConnectionLogin() throws IOException
	{
		System.setProperty(
			"COOKIE_WEBSTART",
			"true" );
		System.setProperty(
			"COOKIE_PREVIEW",
			"preview" );
		System.setProperty(
			"COOKIE_XDEBUG_SESSION",
			"netbeans-xdebug" );

		final TestURLConnection cookietestConnection = new TestURLConnection();
		cookietestConnection.setStreamDelay( 200 );
		String contents = StringToStream.contentsAsString( getClass().getResource(
			"responseText.xml" ) );
		cookietestConnection.setContentText( contents );

		URL testURL = new URL( "test",
			"somehost",
			1200,
			"somefile",
			new URLStreamHandler()
		{

			@Override
			protected URLConnection openConnection( URL u ) throws IOException
			{
				return cookietestConnection;
			}
		} );

		Map< String, String > result = doRequest(
			testURL,
			null ); //

		Assert.assertTrue(
			"No Properties returned",
			!result.isEmpty() );

		Map< String, String > sessionCookies = CookiePropertyParser.getCookies( result );

		Assert.assertTrue(
			"No Cookies returned",
			!sessionCookies.isEmpty() );

	}

	public Map< String, String > doRequest( String url ) throws IOException
	{
		return doRequest(
			url,
			new TreeMap< String, String >() );
	}

	public Map< String, String > doRequest( String url,
		Map< String, String > cookies ) throws IOException
	{
		return doRequest(
			new URL( url ),
			cookies );
	}

	public Map< String, String > doRequest( URL url,
		Map< String, String > cookies ) throws IOException
	{
		Map< String, Object > posts = new TreeMap< String, Object >();

		CustomCookieHttpConnection2 getContactsRequest = new CustomCookieHttpConnection2( url,
			cookies,
			posts );

		getContactsRequest.submit( 16000 );

		Map< String, String > props = XMLPropertyMapEncoder.getXMLProperties( getContactsRequest.getConnection()
			.getInputStream() );

		return props;
	}
}
