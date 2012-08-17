package com.cybernostics.lib.concurrent;

/**
 *
 * @author jasonw
 */
public class DurationRequiredException extends Exception
{

	public static final String SET_DURATION = "You must set a duration in order to get the fraction elapsed.";

	public DurationRequiredException()
	{
		super( SET_DURATION );
	}

	public DurationRequiredException( Throwable cause )
	{
		super( SET_DURATION, cause );
	}

}
