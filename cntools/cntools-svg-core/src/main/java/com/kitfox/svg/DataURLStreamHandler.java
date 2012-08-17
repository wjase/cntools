/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *
 * @author jasonw
 */
class DataURLStreamHandler extends URLStreamHandler
{

	public DataURLStreamHandler()
	{
	}

	@Override
	protected URLConnection openConnection( URL u ) throws IOException
	{
		return new DataUrlConnection( u );
	}

}
