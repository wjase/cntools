package com.cybernostics.lib.io.stream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author jasonw
 * 
 */
public class ConsoleEchoInputStream extends InputStream
{

	InputStream inner = null;

	public ConsoleEchoInputStream( InputStream toWrap )
	{
		inner = toWrap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException
	{
		int read = inner.read();
		if (read != -1)
		{
			System.out.print( (char) read );
		}
		return read;
	}

	/**
	 * @param is
	 * @return
	 */
	public static InputStream pipe( InputStream is )
	{
		return new ConsoleEchoInputStream( is );
	}

	/**
	 * @param toRead
	 */
	public static void dump( InputStream toRead )
	{
		int charin = -1;
		try
		{
			while (-1 != ( charin = toRead.read() ))
			{
				System.out.println( (char) charin );
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String dumpToString( InputStream toRead )
	{
		String out = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int charin = -1;
		try
		{
			while (-1 != ( charin = toRead.read() ))
			{
				bos.write( (char) charin );
			}
			return bos.toString();
		}
		catch (IOException e)
		{
			//e.printStackTrace();
		}
		return out;
	}

}
