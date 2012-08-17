package com.cybernostics.lib.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author jasonw
 *
 */
public class ObjectFutureWrapper< ResultClass > implements Future< Object >
{

	Future< ResultClass > wrapped = null;

	public ObjectFutureWrapper( Future< ResultClass > toWrap )
	{
		this.wrapped = toWrap;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#cancel(boolean)
	 */
	@Override
	public boolean cancel( boolean mayInterruptIfRunning )
	{
		// TODO Auto-generated method stub
		return this.wrapped.cancel( mayInterruptIfRunning );
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#get()
	 */
	@Override
	public Object get() throws InterruptedException, ExecutionException
	{
		return (Object) this.wrapped.get();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public Object get( long timeout, TimeUnit unit )
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return (Object) this.wrapped.get(
			timeout,
			unit );
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#isCancelled()
	 */
	@Override
	public boolean isCancelled()
	{
		return this.wrapped.isCancelled();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Future#isDone()
	 */
	@Override
	public boolean isDone()
	{
		return this.wrapped.isDone();
	}

}
