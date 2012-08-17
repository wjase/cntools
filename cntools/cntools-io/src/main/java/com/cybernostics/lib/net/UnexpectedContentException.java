package com.cybernostics.lib.net;

/**
 * Thrown when a request results in something other than what is expected.
 * 
 * @author jasonw
 */
public class UnexpectedContentException extends Exception
{

	public UnexpectedContentException( String message )
	{
		super( message );
	}

	public UnexpectedContentException( String message, Throwable cause )
	{
		super( message, cause );
	}
}
