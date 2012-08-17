package com.cybernostics.lib.concurrent;

import java.util.ArrayList;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jasonw
 *
 */
public abstract class WatchableWorkerTask extends CallableWorkerTask
{

	/**
	 * @param name
	 */
	public WatchableWorkerTask( String name )
	{
		super( name );
	}

	@Override
	protected void started()
	{
		fireTaskStarted();
	}

	@Override
	protected void finished()
	{
		fireTaskDone();
	}

	private ConcurrentLinkedQueue< WorkerDoneListener > doneWatchers = new ConcurrentLinkedQueue< WorkerDoneListener >();

	private ConcurrentLinkedQueue< WorkerStartedListener > startedWatchers = new ConcurrentLinkedQueue< WorkerStartedListener >();

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WorkerTask#finished()
	 */
	//	@Override
	//	protected void finished()
	//	{
	//		super.finished();
	//		fireTaskDone();
	//	}
	public void fireTaskDone()
	{
		for (final WorkerDoneListener eachListener : doneWatchers)
		{
			try
			{
				// System.out.println( "fireTaskDone:" + this.getTaskName() );
				//				// Queue each event notification separately
				getExecutor().submit(
					new Runnable()
				{

					public void run()
					{
						eachListener.taskDone( WatchableWorkerTask.this );
					}

				} );

			}
			catch (Exception e)
			{
				UnhandledExceptionManager.handleException( e );
			}
		}
	}

	public void fireTaskStarted()
	{
		for (final WorkerStartedListener eachListener : startedWatchers)
		{
			//			// Queue each event notification separately
			getExecutor().submit(
				new Runnable()
			{

				public void run()
				{
					eachListener.taskStarted( WatchableWorkerTask.this );
				}

			} );
		}
	}

	public void addWorkerDoneListener( WorkerDoneListener workerDoneListener )
	{
		// too late already done!
		if (this.isDone())
		{
			workerDoneListener.taskDone( WatchableWorkerTask.this );
		}
		else
		{
			doneWatchers.add( workerDoneListener );
		}
	}

	public void addWorkerStartedListener( WorkerStartedListener workerStartedListener )
	{
		startedWatchers.add( workerStartedListener );
	}

}
