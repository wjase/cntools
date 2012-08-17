package com.cybernostics.lib.concurrent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This saves system calls to getTime by unifying calls to it through this class
 * and sticking to a 10ms resolution
 *
 * @author jasonw
 */
public class AnimationTimer
{

	private static long currentTime;

	private static Timer timer = null;

	private static ConcurrentSet< AnimationTimerListener > listeners = new ConcurrentSet< AnimationTimerListener >();

	public static void addListener( AnimationTimerListener toAdd )
	{
		listeners.add( toAdd );
	}

	public static void removeListener( AnimationTimerListener toRemove )
	{
		listeners.remove( toRemove );
	}

	synchronized public static void startTimer()
	{
		if (timer == null)
		{
			timer = new Timer( true );
			currentTime = System.nanoTime() / 1000000L;
			timer.scheduleAtFixedRate(
				new TimerTask()
			{

				@Override
				public void run()
				{
					try
					{
						currentTime = getNanoTime() / 1000000L;
						for (AnimationTimerListener eachListener : listeners)
						{
							eachListener.update( currentTime );
						}

					}
					finally
					{
					}

				}

			},
				0L,
				50L );

		}
	}

	public static long getCurrentTime()
	{
		if (timer == null)
		{
			startTimer();

		}
		return currentTime;
	}

	public static long getNanoTime()
	{
		return System.nanoTime();
	}

}
