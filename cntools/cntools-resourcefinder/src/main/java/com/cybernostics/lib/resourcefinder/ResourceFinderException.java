package com.cybernostics.lib.resourcefinder;

/**
 *
 * @author jasonw
 */
public class ResourceFinderException extends RuntimeException
{

	public ResourceFinderException( Throwable cause )
	{
		super( cause );
	}

	public ResourceFinderException( String message, Throwable cause )
	{
		super( message, cause );
	}

}
