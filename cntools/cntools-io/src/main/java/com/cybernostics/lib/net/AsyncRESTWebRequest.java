package com.cybernostics.lib.net;

import java.io.InputStream;
import java.util.Map;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.persist.xml.XMLPropertyMapEncoder;
import java.io.BufferedInputStream;

/**
 * 
 * Asynchronously queries a web url and returns the result
 * @author jasonw
 *
 */
public class AsyncRESTWebRequest extends WatchableWorkerTask
{
	CustomCookieHttpConnection2 myQuery = null;
	int timeout = 10000;

	public AsyncRESTWebRequest( String name, CustomCookieHttpConnection2 query )
	{
		super( name );
		myQuery = query;
	}

	public AsyncRESTWebRequest(
		String name,
		CustomCookieHttpConnection2 query,
		int timeout )
	{
		super( name );
		myQuery = query;
		this.timeout = timeout;
	}

	protected Object doTask() throws Exception
	{
		InputStream is = myQuery.submit( timeout );
		return XMLPropertyMapEncoder.getXMLProperties( is );
	}

	public static boolean requestSucceeded( Map< String, String > result )
	{
		return result.containsKey( "status" ) && result.get(
			"status" )
			.equalsIgnoreCase(
				"OK" );
	}
}
