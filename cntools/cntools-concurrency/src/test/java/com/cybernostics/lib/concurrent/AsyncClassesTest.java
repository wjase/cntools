package com.cybernostics.lib.concurrent;

import java.util.concurrent.ArrayBlockingQueue;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author jasonw
 *
 */
public class AsyncClassesTest
{

	boolean asyncResult = false;

	@Test
	public void testWatchers()
	{
		WatchableWorkerTask testTask = new WatchableWorkerTask( "Sample Task" )
		{
			@Override
			protected Object doTask() throws Exception
			{
				Thread.sleep( 500 ); // long running task
				return "Hooray";
			}
		};

		final ArrayBlockingQueue< Object > completedObject = new ArrayBlockingQueue< Object >( 1 );
		new WhenComplete( testTask )
		{
			/* (non-Javadoc)
			 * @see com.cybernostics.lib.net.WhenComplete#dothis(java.lang.Object)
			 */
			@Override
			public void dothis( Object returnType )
			{
				asyncResult = true;
				System.out.println( "Made it here! " );
				completedObject.add( returnType );
			}
		};

		testTask.start();
		String strResult = "";

		try
		{
			strResult = (String) completedObject.take();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		Assert.assertEquals(
			"Hooray",
			strResult );
		Assert.assertEquals(
			true,
			asyncResult );
	}
}
