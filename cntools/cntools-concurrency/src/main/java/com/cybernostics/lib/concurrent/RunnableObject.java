package com.cybernostics.lib.concurrent;

/**
 * Runnable which passes an object parameter as an instance
 *
 * @author jasonw
 */
abstract public class RunnableObject< T > implements Runnable
{

	protected T arg = null;

	public RunnableObject( T param )
	{
		arg = param;
	}

	protected T getObject()
	{
		return arg;
	}

	public void run( T argPassed )
	{
		arg = argPassed;
		run();
	}

}
