package com.cybernostics.lib.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;

import org.junit.Test;

import com.cybernostics.lib.concurrent.WhenComplete;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.stream.StreamPipe;
import com.cybernostics.lib.io.stream.StringToStream;
import com.cybernostics.lib.persist.xml.XMLPropertyMapEncoder;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author jasonw
 *
 */
public class AsyncRequestTest
{

	boolean result = false;

	@SuppressWarnings("unchecked")
	@Test
	public void testAsyncRequest()
	{
		try
		{
			System.setProperty(
				"COOKIE_WEBSTART",
				"true" );
			System.setProperty(
				"COOKIE_PREVIEW",
				"preview" );
			String host = System.getenv( "jmhost" );

			Map< String, Object > posts = new TreeMap< String, Object >();

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
				protected URLConnection openConnection( URL u )
					throws IOException
				{
					return cookietestConnection;
				}
			} );

			CustomCookieHttpConnection2 login = new CustomCookieHttpConnection2( testURL,
				null,
				posts );
			AsyncWebRequest loginRequest = new AsyncWebRequest( "login", login );
			final ArrayBlockingQueue< Object > completedObject = new ArrayBlockingQueue< Object >( 1 );

			new WhenComplete( loginRequest )
			{

				@Override
				public void dothis( Object result )
				{
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					StreamPipe.copyInputToOutput(
						(InputStream) result,
						bos );

					System.out.println( bos.toString() );
					try
					{
						Map< String, String > values = XMLPropertyMapEncoder.getXMLProperties( ( new ByteArrayInputStream(
							bos.toByteArray() ) ) );

						for (String eachKey : values.keySet())
						{
							String sValue = values.get( eachKey );
							System.setProperty(
								eachKey,
								sValue );
						}

						Assert.assertTrue(
							"Empty values returned",
							values.size() > 0 );
						AsyncRequestTest.this.result = true;
						completedObject.add( values );
					}
					catch (Exception e)
					{
						Assert.fail( "Exception occured:"
							+ e.getLocalizedMessage() );
						UnhandledExceptionManager.handleException( e );
						Assert.assertTrue( false );
					}
				}
			};

			loginRequest.start();

			try
			{
				Map< String, String > values = (Map< String, String >) completedObject.poll(
					20,
					TimeUnit.SECONDS );
				for (String eachKey : values.keySet())
				{
					System.out.println( eachKey );
				}
				Assert.assertEquals(
					true,
					loginRequest.isDone() );
				Assert.assertEquals(
					true,
					result );
				return;
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			Assert.fail( "Should not make it here" );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				AsyncRequestTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}
}
