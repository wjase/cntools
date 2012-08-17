package com.cybernostics.lib.concurrent;

import com.cybernostics.lib.concurrent.TimeStamp;

/**
 * The time stamp should be updated once per update cycle to save repeating the
 * subtraction repeatedly.
 *
 * @author jasonw
 *
 */
public class NanoTimeStamp
{

	private long started = 0;

	private long elapsed = 0;

	private long current = 0;

	private long duration = 0;

	private static final long NANO_2_MILLI = 1000000;

	public static long toMilliSeconds( long nanoseconds )
	{
		return nanoseconds / NANO_2_MILLI;
	}

	/**
	 *
	 */
	public void start()
	{
		started = getNanoTime();
		// System.out.print( "Timecode started:" + started );
		current = started;
		elapsed = 0;

	}

	/**
	 *
	 * @return
	 */
	public static long getNanoTime()
	{
		return AnimationTimer.getNanoTime();
	}

	/**
	 *
	 */
	public void update()
	{
		update( getNanoTime() );
	}

	public void update( long currentNano )
	{
		current = currentNano;
		elapsed = current - started;
	}

	/**
	 *
	 * @return
	 */
	public long getCurrent()
	{
		return current;
	}

	/**
	 *
	 * @return
	 */
	public long getElapsed()
	{
		return elapsed;
	}

	/**
	 *
	 * @return
	 */
	public long getStarted()
	{
		return started;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration( long duration )
	{
		this.duration = duration;
	}

	/**
	 * @return the duration
	 */
	public long getDuration()
	{
		return duration;
	}

	/**
	 *
	 * @param other
	 */
	public synchronized void setCurrent( NanoTimeStamp other )
	{
		if (started == 0)
		{
			start();
		}
		current = other.current;
		elapsed = current - started;

	}

	public void init( TimeStamp timeCode )
	{
		started = NANO_2_MILLI * timeCode.getStarted();
	}

}
