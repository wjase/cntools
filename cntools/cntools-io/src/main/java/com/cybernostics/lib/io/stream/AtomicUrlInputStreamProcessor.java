package com.cybernostics.lib.io.stream;

import com.cybernostics.lib.io.JarUrlFix;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author jasonw
 */
abstract public class AtomicUrlInputStreamProcessor
{

	private AtomicUrlInputStreamProcessor()
	{
	}

	/**
	 * Performs an atomic operation on the URL specified, closing the stream
	 * after the operation is done.
	 * @code
	 * AtomicUrlInputStreamProcessor.execute(someUrl, new InputStreamOperation()
	 * {
	 *      process(InputStream is) throws IOException
	 *      {
	 *          // do something with the stream here
	 *      }
	 * });
	 * 
	 * @param toRead
	 * @param op
	 * @throws IOException 
	 */
	public static void execute( URL toRead, InputStreamOperation op )
		throws IOException
	{
		InputStream is = null;
		try
		{
			is = JarUrlFix.getURLStream( toRead );
			op.process( is );
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException ie)
				{

				}
			}
		}

	}
}
