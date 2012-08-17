package com.cybernostics.lib.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author jasonw
 */
public class FakeFuture implements Future< Object >
{
	private final Object theObject;

	public FakeFuture( Object anObject )
	{
		this.theObject = anObject;
	}

	@Override
	public boolean cancel( boolean mayInterruptIfRunning )
	{
		return true;
	}

	@Override
	public boolean isCancelled()
	{
		return false;
	}

	@Override
	public boolean isDone()
	{
		return true;
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException
	{
		return theObject;
	}

	@Override
	public Object get( long timeout, TimeUnit unit )
		throws InterruptedException, ExecutionException, TimeoutException
	{
		return theObject;
	}

}
