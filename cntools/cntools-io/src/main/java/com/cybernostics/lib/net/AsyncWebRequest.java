package com.cybernostics.lib.net;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;

/**
 * 
 * Asynchronously queries a web url and returns the result
 * @author jasonw
 *
 */
public class AsyncWebRequest extends WatchableWorkerTask
{
	CustomCookieHttpConnection2 myQuery = null;
	int timeout = 10000;

	public AsyncWebRequest( String name, CustomCookieHttpConnection2 query )
	{
		super( name );
		myQuery = query;
	}

	public AsyncWebRequest(
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
		//Thread.sleep( 10000 ); // long running task
		//ByteArrayInputStream bis = new ByteArrayInputStream( (new String("Hello there")).getBytes() );
		//return bis;
		return myQuery.submit( timeout );
	}
}
