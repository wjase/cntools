package com.cybernostics.lib.io.stream;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamPipe
{

	public static long copyInputToOutput( InputStream in, OutputStream out )
	{
		long byteCount = 0;
		BufferedInputStream bis = new BufferedInputStream( in );

		int nextChar = 0;
		try
		{
			while (( nextChar = bis.read() ) != -1)
			{
				out.write( nextChar );
				++byteCount;
			}
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		return byteCount;
	}

	public static InputStream getDebuggableStream( final InputStream toDebug )
	{
		return new InputStream()
		{

			@Override
			public int read() throws IOException
			{
				int result = toDebug.read();
				return result;
			}
		};

	}
}
