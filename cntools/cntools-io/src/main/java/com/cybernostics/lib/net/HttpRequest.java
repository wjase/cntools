package com.cybernostics.lib.net;

import java.io.InputStream;

import com.cybernostics.lib.concurrent.WorkerTask;

/**
 * @author jasonw
 *
 */
public class HttpRequest extends WorkerTask< InputStream >
{
	// This object models the request and the response from a remote
	// web site.
	CustomCookieHttpConnection2 service;

	// This is the timeout to wait for the response
	int timeout = 10000;

	public HttpRequest( CustomCookieHttpConnection2 service, int timeout )
	{
		this.timeout = timeout;
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WorkerTask#doTask()
	 */
	@Override
	protected InputStream doTask() throws Exception
	{
		return service.submit( this.timeout );
	}

}
