package com.cybernostics.lib.concurrent;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author jasonw
 * 
 */
public class AppTaskQueue
{

	private AppTaskQueue()
	{

	}

	/**
	 * Default executor. Executes each task in a new thread.
	 */
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	private static Set< CallableWorkerTask > managedTasks = new TreeSet< CallableWorkerTask >();

	private static synchronized void addTask( CallableWorkerTask toAdd )
	{
		managedTasks.add( toAdd );
	}

	private static synchronized void removeTask( CallableWorkerTask toAdd )
	{
		managedTasks.remove( toAdd );
	}

	public static Future< Object > submitTask( final CallableWorkerTask toAdd )
	{
		addTask( toAdd );
		toAdd.setRunOnFinish( new RunnableWithArg< Object >()
		{
			@Override
			public void run( Object arg )
			{
				removeTask( toAdd );

			}
		} );

		toAdd.setExecutor( EXECUTOR );
		toAdd.start();

		return toAdd;
	}

	public static synchronized final Set< CallableWorkerTask > getActive()
	{
		return managedTasks;
	}
}
