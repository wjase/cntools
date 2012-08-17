package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * resloader
 * @author jasonw
 */
public class Handler extends URLStreamHandler
{
	// only need one
	private static SingletonInstance< Handler > theOne = new SingletonInstance< Handler >()
	{

		@Override
		protected Handler createInstance()
		{
			return new Handler();
		}
	};

	public static Handler get()
	{
		return theOne.get();
	}

	@Override
	protected URLConnection openConnection( final URL u ) throws IOException
	{
		return new ResLoaderURLConnection( u );
	}
}
