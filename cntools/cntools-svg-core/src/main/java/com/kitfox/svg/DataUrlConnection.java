/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author jasonw
 */
class DataUrlConnection extends URLConnection
{

	URL toLoad;

	InputStream is = null;

	public DataUrlConnection( URL u )
	{
		super( u );
		toLoad = u;
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		if (is == null)
		{
			connect();
		}

		return is;
	}

	@Override
	public void connect() throws IOException
	{
		String sUrl = toLoad.toString();

		String path = sUrl;//imageURI.getRawSchemeSpecificPart();
		int idx = path.indexOf( ';' );
		String mime = path.substring(
			0,
			idx );
		String content = path.substring( idx + 1 );

		if (content.startsWith( "base64" ))
		{
			content = content.substring( 6 );

			byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer( content );
			ByteArrayInputStream bais = new ByteArrayInputStream( buf );
			is = bais;

		}

	}
}