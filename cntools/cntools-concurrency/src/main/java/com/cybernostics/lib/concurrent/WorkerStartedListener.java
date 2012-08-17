package com.cybernostics.lib.concurrent;

import java.util.concurrent.Future;

/**
 * @author jasonw
 *
 */
public interface WorkerStartedListener
{
	public void taskStarted( Future< Object > source );
}
