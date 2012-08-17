package com.cybernostics.lib.io.stream;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper in case sun changes their implementation
 * @author jasonw
 *
 */
public class Base64EncoderStream extends Base64.OutputStream
{

	/**
	 * @param out
	 */
	public Base64EncoderStream( OutputStream out )
	{
		super( out );

	}

	/**
	 * Tkes a bytes stream and encodes it as a 64bit encoded string with a unique
	 * user supplied start and stop to demarcate the sequence
	 * @param toEncode
	 * @param startPattern
	 * @param stopPattern
	 * @return 
	 */
	public static String encodeString( byte[] toEncode,
		String startPattern,
		String stopPattern )
	{
		StringWriter sw = new StringWriter();
		sw.write( startPattern );
		ByteArrayInputStream bis = new ByteArrayInputStream( toEncode );
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		Base64EncoderStream b64 = new Base64EncoderStream( bos );
		StreamPipe.copyInputToOutput(
			bis,
			b64 );
		try
		{
			b64.flush();
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				Base64EncoderStream.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		sw.write( new String( bos.toByteArray() ) );
		sw.write( stopPattern );
		return sw.toString();

	}
}
