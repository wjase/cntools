package com.cybernostics.lib.concurrent;

/**
 *
 * @author jasonw
 */
public class ConcurrentStringBuilder
{

	public ConcurrentStringBuilder()
	{
	}

	private StringBuilder sb = new StringBuilder();

	public synchronized StringBuilder append( char c )
	{
		return sb.append( c );
	}

	public synchronized StringBuilder append( String str )
	{
		return sb.append( str );
	}

	@Override
	public synchronized String toString()
	{
		return sb.toString();
	}

}
