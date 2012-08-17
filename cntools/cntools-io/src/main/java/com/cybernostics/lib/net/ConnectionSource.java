package com.cybernostics.lib.net;

import java.io.InputStream;
import java.net.URLConnection;

/**
 * @author jasonw
 *
 */
public interface ConnectionSource
{
	public URLConnection getConnection();

	public InputStream submit( int timeout );
}
