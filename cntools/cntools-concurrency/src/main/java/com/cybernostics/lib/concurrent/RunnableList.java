package com.cybernostics.lib.concurrent;

import java.util.ArrayList;

/**
 *
 * Runnable List is a collection of Runnables which are all run
 * sequentially by calling run on this object.
 * 
 * @author jasonw
 */
public class RunnableList< T > implements RunnableWithArg< T >
{
	ArrayList< RunnableWithArg< T >> runnables = new ArrayList< RunnableWithArg< T >>();

	@Override
	public void run( T arg )
	{
		for (RunnableWithArg< T > eachRunnable : runnables)
		{
			eachRunnable.run( arg );
		}

	}

	public void add( RunnableWithArg< T > toAdd )
	{
		runnables.add( toAdd );
	}

}
