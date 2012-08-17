package com.cybernostics.lib.io.stream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Stream comparison utility class.
 * 
 * @author jasonw
 * 
 */
public class StreamCompare
{

	// don't instantiate me
	private StreamCompare()
	{

	}

	/**
	 * Brute force byte by byte comparison - intended for use in unit tests
	 * 
	 * @param stream1
	 *            - stream to compare
	 * @param stream2
	 *            - stream to compare
	 * @return true if the streams have identical byte for byte contents
	 */
	public static boolean check( InputStream stream1, InputStream stream2 )
	{

		BufferedInputStream bis1 = new BufferedInputStream( stream1 );
		BufferedInputStream bis2 = new BufferedInputStream( stream2 );

		try
		{
			while (( bis1.available() > 0 ) && ( bis2.available() > 0 ))
			{
				if (bis1.read() != bis2.read())
				{
					return false;
				}
			}
			// one of the streams is exhausted check they both are
			if (( bis1.available() > 0 ) || ( bis2.available() > 0 ))
			{
				return false;
			}
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				stream1.close();
				stream2.close();
			}
			catch (IOException e)
			{
				// do nothing if close fails
			}

		}
		return false;
	}
}
