package com.cybernostics.lib.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Task group aggregates a group of tasks. The tasks are each done in parallel but this task
 * does not signal itself done until they are all done.
 * 
 * @author jasonw
 *
 */
public class SerialTaskGroup extends WatchableWorkerTask
{
	List< WatchableWorkerTask > childTasks;

	public SerialTaskGroup( String name, WatchableWorkerTask... tasks )
	{
		super( name );

		ArrayList< WatchableWorkerTask > childTaskList = new ArrayList< WatchableWorkerTask >();

		for (WatchableWorkerTask eachTask : tasks)
		{
			childTaskList.add( eachTask );
		}
		init( childTaskList );
	}

	public SerialTaskGroup( String name, List< WatchableWorkerTask > childTasks )
	{
		super( name );
		init( childTasks );
	}

	private void init( List< WatchableWorkerTask > childTasks )
	{
		this.childTasks = childTasks;
		setExecutor( Executors.newSingleThreadExecutor() );
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WatchableWorkerTask#doTask()
	 */
	@Override
	protected List< Object > doTask() throws Exception
	{
		ExecutorService taskDoer = this.getExecutor();

		// Prepare array for results
		List< Object > results = new ArrayList< Object >();
		for (Future< Object > eachResult : taskDoer.invokeAll( childTasks ))
		{
			results.add( eachResult.get() );
		}

		return results;

	}

}
