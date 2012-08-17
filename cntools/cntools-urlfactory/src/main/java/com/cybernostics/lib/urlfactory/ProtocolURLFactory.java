/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.urlfactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

/**
 *
 * @author jasonw
 */
public class ProtocolURLFactory implements CustomUrlFactory
{

	private URLStreamHandler theHandler = null;

	public ProtocolURLFactory( URLStreamHandler theHandler )
	{
		this.theHandler = theHandler;
	}

	@Override
	public URL create( String spec ) throws MalformedURLException
	{
		return new URL( null, spec, theHandler );
	}

}
