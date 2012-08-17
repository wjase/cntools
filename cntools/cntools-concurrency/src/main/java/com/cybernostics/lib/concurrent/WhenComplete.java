package com.cybernostics.lib.concurrent;

import java.util.concurrent.Future;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * @author jasonw
 *
 */
abstract public class WhenComplete implements WorkerDoneListener
{

	public WhenComplete( WatchableWorkerTask theTask )
	{
		theTask.addWorkerDoneListener( this );
	}

	@Override
	public void taskDone( Future< Object > completed )
	{
		try
		{
			dothis( completed.get() );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}

	}

	/**
	 * @param returnType
	 */
	abstract public void dothis( Object returnType );

}
