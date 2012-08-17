package com.cybernostics.lib.concurrent;

import java.util.concurrent.Future;

/**
 * @author jasonw
 *
 */
public interface WorkerDoneListener
{
	public void taskDone( Future< Object > completed );
}
