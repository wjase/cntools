package com.cybernostics.lib.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Maintains a thread-safe pool of resources
 * @author jasonw
 */
public class ResourcePool< T >
{
	ResourceFactory< T > resourceMaker;

	public ResourcePool( int maxResources, ResourceFactory< T > maker )
	{
		sem = new Semaphore( maxResources, true );
		this.resourceMaker = maker;
	}

	private Semaphore sem = null;
	private final Queue< T > resources = new ConcurrentLinkedQueue< T >();

	public T getResource( long maxWaitMillis ) throws InterruptedException,
		ResourceCreationException
	{
		// First, get permission to take or create a resource
		sem.tryAcquire(
			maxWaitMillis,
			TimeUnit.MILLISECONDS );

		// Then, actually take one if available...
		T res = resources.poll();
		if (res != null)
		{
			return res;
		}

		// ...or create one if none available
		try
		{
			return this.resourceMaker.create();
		}
		catch (ResourceCreationException e)
		{
			// Don't hog the permit if we failed to create a resource!
			sem.release();
			throw e;
		}
	}

	public void returnResource( T res )
	{
		resources.add( res );
		sem.release();
	}
}