package com.cybernostics.lib.concurrent;

import java.awt.EventQueue;
import java.util.UUID;
import java.util.concurrent.*;

public abstract class CallableWorkerTask
	implements
	Future< Object >,
	Callable< Object >,
	Comparable< CallableWorkerTask >
{

	// These are run in the thread task not in the controlling thread
	private RunnableList< Object > runOnStart = new RunnableList< Object >();
	private RunnableList< Object > runOnFinish = new RunnableList< Object >();
	private String taskName = "unnamed";
	private String uId = UUID.randomUUID()
		.toString();
	private String compareString = null;
	private OuputConverter resultConverter = null;

	public OuputConverter getResultConverter()
	{
		return resultConverter;
	}

	public void setResultConverter( OuputConverter resultConverter )
	{
		this.resultConverter = resultConverter;
	}

	/**
	 * @return the uId
	 */
	public String getuId()
	{
		return uId;
	}

	public String getTaskName()
	{
		return taskName;
	}

	protected String getCompareString()
	{
		if (compareString == null)
		{
			compareString = taskName + uId;
		}
		return compareString;
	}

	@Override
	public int compareTo( CallableWorkerTask o )
	{
		return this.getCompareString()
			.compareTo(
				o.getCompareString() );
	}

	public void setTaskName( String taskName )
	{
		this.taskName = taskName;
		compareString = null; // reset for later
	}

	/**
	 * Default executor. Executes each task in a new thread.
	 */
	private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

	public void setRunOnStart( final RunnableWithArg< Object > toRun )
	{
		runOnStart.add( toRun );
	}

	public void setRunOnFinish( final RunnableWithArg< Object > toRun )
	{
		runOnFinish.add( toRun );
	}

	/** Executor instance. */
	private ExecutorService executor;
	/** <tt>true</tt> if <tt>start</tt> method was called. */
	private boolean started;

	/** Creates new SwingWorker with default executor. */
	public CallableWorkerTask( String name )
	{
		this( name, EXECUTOR );
	}

	/**
	 * Creates new SwingWorker with specified executor.
	 * 
	 * @param e
	 *            executor for this worker
	 */
	protected CallableWorkerTask( String name, ExecutorService e )
	{
		setExecutor( e );
		setTaskName( name );
	}

	/**
	 * Sets executor to be used when worker is started.
	 * 
	 * @param e
	 *            executor for this worker
	 */
	public synchronized void setExecutor( ExecutorService e )
	{
		executor = e;
	}

	/**
	 * Returns executor to be used when worker is started.
	 * 
	 * @return executor
	 */
	public synchronized ExecutorService getExecutor()
	{
		if (executor == null)
		{
			executor = EXECUTOR;
		}
		return executor;
	}

	/**
	 * Submits this worker to executor for execution.
	 * 
	 * @throws RejectedExecutionException
	 *             if the executor balks
	 */
	public synchronized Future< Object > start()
	{
		if (!started)
		{
			task = (Future< Object >) executor.submit( this );
			started = true;
			EventQueue.invokeLater( new Runnable()
			{

				public void run()
				{
					started();
				}
			} );
		}
		return this;
	}

	/**
	 * Override this to do something when the task is started
	 */
	protected void started()
	{
	}

	private Future< Object > task = null;

	/**
	 * Computes the value to be returned by the <tt>get</tt> method.
	 */
	protected abstract Object doTask() throws Exception;

	/**
	 * Called on the event dispatching thread (not on the worker thread) after
	 * the <tt>doTask</tt> method has returned.
	 */
	protected void finished()
	{
	}

	/* Runnable implementation. */
	//	/** Runs this task. Called by executor. */
	//	public void run()
	//	{
	//		task.run();
	//	}

	/* Future implementation. */
	/** Attempts to cancel execution of this task. */
	@Override
	public boolean cancel( boolean mayInterruptIfRunning )
	{
		return task.cancel( mayInterruptIfRunning );
	}

	/**
	 * Returns true if this task was cancelled before it completed normally.
	 */
	@Override
	public boolean isCancelled()
	{
		return task == null || task.isCancelled();
	}

	/** Returns true if this task completed. */
	@Override
	public boolean isDone()
	{
		return task != null && task.isDone();
	}

	/**
	 * Waits if necessary for this task to complete, and then returns its
	 * result.
	 */
	@Override
	public Object get() throws InterruptedException, ExecutionException
	{
		if (task == null)
		{
			throw new ExecutionException( "get() called but not started", null );
		}
		return (Object) ( task.get() );
	}

	/**
	 * Waits if necessary for at most the given time for this task to complete,
	 * and then returns its result, if available.
	 */
	@Override
	public Object get( long timeout, TimeUnit unit )
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return (Object) ( task.get(
			timeout,
			unit ) );
	}

	@Override
	public Object call() throws Exception
	{
		if (runOnStart != null)
		{
			runOnStart.run( null );
		}
		Object result = null;
		try
		{
			result = doTask();
		}
		catch (Exception e)
		{
			System.out.println( e.getLocalizedMessage() );
			e.printStackTrace();
			throw e;
		}
		finally
		{
			finished();
		}

		if (runOnFinish != null)
		{
			runOnFinish.run( result );
		}
		if (resultConverter != null)
		{
			return resultConverter.convert( result );
		}
		//		finished();
		return result;
	}
}
