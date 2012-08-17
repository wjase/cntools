package com.cybernostics.lib.concurrent;

import com.cybernostics.lib.maths.Random;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Having initiated a bunch of tasks which return Future<T>
 *
 * @author jasonw
 */
public class WaitAllFuture< T > extends CallableWorkerTask
{

	ConcurrentLinkedQueue< Future< T >> tasks = new ConcurrentLinkedQueue< Future< T >>();

	public final void addTask( Future< T >... items )
	{
		tasks.addAll( Arrays.asList( items ) );
	}

	public static < T > void wait( long timeout, Future< T >... items )
	{
		WaitAllFuture allDone = new WaitAllFuture( null, timeout, items );
		allDone.doTask();
	}

	long timeout;

	public WaitAllFuture( String name, long timeout, Future< T >... items )
	{
		super( name );
		addTask( items );
		this.timeout = timeout;
	}

	@Override
	protected Object doTask()
	{
		NanoTimeStamp now = new NanoTimeStamp();
		now.start();

		for (Future< T > eachtask : tasks)
		{
			now.update();
			long toWaitRemaining = timeout
				- NanoTimeStamp.toMilliSeconds( now.getElapsed() );
			if (toWaitRemaining < 0)
			{
				throw new RuntimeException( new TimeoutException( "WaitAll:"
					+ eachtask.getClass()
						.getCanonicalName() ) );
			}
			try
			{
				eachtask.get(
					toWaitRemaining,
					TimeUnit.MILLISECONDS );
			}
			catch (Exception ex)
			{
				throw new RuntimeException( ex );
			}
		}
		return null;
	}

}
