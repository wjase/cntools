package com.cybernostics.lib.io.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * This was specifically created to handle reading a JarInputStream which has several things
 * strung together in a stream. Some readXXX methods which take a stream assume that they can close
 * the stream after having read whatever it is they read. For most streams this is a good idea, but it breaks JarInputStream.
 * 
 * So by using this class, we effectively bypass any calls to the stream close method.
 * 
 * @author jasonw
 *
 */
public class UnClosableInputStream extends InputStream
{

	InputStream wrapped = null;

	public int available() throws IOException
	{
		return wrapped.available();
	}

	public void close() throws IOException
	{
		// do nothing
		//System.out.printf("No Close please\n");
	}

	public boolean equals( Object obj )
	{
		return wrapped.equals( obj );
	}

	public int hashCode()
	{
		return wrapped.hashCode();
	}

	public void mark( int readlimit )
	{
		wrapped.mark( readlimit );
	}

	public boolean markSupported()
	{
		return wrapped.markSupported();
	}

	public int read( byte[] b, int off, int len ) throws IOException
	{
		return wrapped.read(
			b,
			off,
			len );
	}

	public int read( byte[] b ) throws IOException
	{
		return wrapped.read( b );
	}

	public void reset() throws IOException
	{
		wrapped.reset();
	}

	public long skip( long n ) throws IOException
	{
		return wrapped.skip( n );
	}

	public String toString()
	{
		return wrapped.toString();
	}

	public UnClosableInputStream( InputStream toWrap )
	{
		wrapped = toWrap;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException
	{
		return wrapped.read();
	}

}
