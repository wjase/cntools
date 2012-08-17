package com.cybernostics.lib.media;

import java.io.InputStream;
import java.net.URL;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.concurrent.WorkerDoneListener;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.net.CookiePropertyParser;
import com.cybernostics.lib.net.CustomCookieHttpConnection2;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author jasonw
 *
 */
public class HttpImageRequest extends WatchableWorkerTask
{

	public static void submit( URL toFetch, WorkerDoneListener toDoWhenReady )
	{
		HttpImageRequest request = new HttpImageRequest( toFetch, 10000 );
		request.addWorkerDoneListener( toDoWhenReady );
		request.start();

	}

	// This object models the request and the response from a remote
	// web site.
	CustomCookieHttpConnection2 service;
	// This is the timeout to wait for the response
	int timeout = 10000;

	public HttpImageRequest( URL tograb, int timeout )
	{
		super( "HttpImageRequest" + tograb.toExternalForm() );
		this.timeout = timeout;
		Map< String, Object > posts = new TreeMap< String, Object >();
		Map< String, String > sessionCookies = CookiePropertyParser.getCookies();
		this.service = new CustomCookieHttpConnection2( tograb,
			sessionCookies,
			posts );

	}

	public HttpImageRequest( CustomCookieHttpConnection2 service, int timeout )
	{
		super( "HttpImageRequest" + service.toString() );
		this.timeout = timeout;
		this.service = service;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WorkerTask#doTask()
	 */
	@Override
	protected AttributedScalableIcon doTask() throws Exception
	{
		InputStream is = service.expect(
			"image",
			this.timeout );

		BufferedImage bi = ImageLoader.loadBufferedImageFromStream( is );
		if (bi == null)
		{
			return null;
		}
		ScalableImageIcon sii = new ScalableImageIcon( bi );

		AttributedScalableIcon asi = new AttributedScalableIcon( sii );
		asi.put(
			"URL",
			service.getLocation() );
		return asi;
	}
}
