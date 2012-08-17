package com.cybernostics.lib.io.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 *
 */
public class StringToStream
{

	public static InputStream getIn( String data )
	{
		return new ByteArrayInputStream( data.getBytes() );
	}

	public static String asString( InputStream is )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamPipe.copyInputToOutput(
			is,
			bos );
		return bos.toString();
	}

	public static String contentsAsString( URL u )
	{
		try
		{
			return asString( u.openStream() );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				StringToStream.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return "";
	}

}
