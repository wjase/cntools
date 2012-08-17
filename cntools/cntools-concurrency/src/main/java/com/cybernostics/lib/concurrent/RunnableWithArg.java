package com.cybernostics.lib.concurrent;

/**
 * Like Runnable but with an arg provided to the run method
 * 
 * @author jasonw
 */
public interface RunnableWithArg< T >
{

	public void run( T arg );
}
