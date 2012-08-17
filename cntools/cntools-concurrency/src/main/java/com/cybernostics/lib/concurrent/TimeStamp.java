package com.cybernostics.lib.concurrent;

/**
 * The time stamp should be updated once per update cycle to save repeating the
 * subtraction repeatedly.
 *
 * @author jasonw
 *
 */
public class TimeStamp
{

	private long started = 0;

	private long elapsed = 0;

	private long current = 0;

	private long duration = 0;

	private boolean fractionUpdated = false;

	private boolean isPeriodic = false;

	private long pausedTotal = 0;

	private long pauseStarted = 0;

	private long headstart = 0;

	public long getHeadstart()
	{
		return headstart;
	}

	/**
	 * Sometimes for periodic or looped tracks you want to start partway through
	 * (think spacing out moving clouds across a screen) Setting a headstart
	 * will start the first iteration of the track part way through. eg a cloud
	 * starting partway across the screen when a window is first shown.
	 *
	 * @return
	 */
	public void setHeadstart( long headstart )
	{
		this.headstart = headstart;
	}

	private synchronized void setStarted( long value )
	{
		// can only set once
		if (started == 0)
		{
			started = value;
		}

	}

	/**
	 * Starts a "timeout" timer which measures "dead time" during which the
	 * timer is paused
	 */
	public synchronized void pause()
	{
		pauseStarted = getMilliTime();
	}

	public synchronized void resume()
	{
		if (pauseStarted != 0)
		{
			pausedTotal += ( getMilliTime() - pauseStarted );
			pauseStarted = 0;
		}
	}

	/**
	 * The fraction of the total duration
	 */
	private double fractionElapsed = 0; //

	/**
	 *
	 */
	public synchronized void start()
	{
		current = getMilliTime();
		setStarted( current - headstart );
		if (headstart > 0)
		{
			elapsed = headstart;
			setHeadstart( 0 );
			fractionUpdated = false;
		}
		else
		{
			// System.out.print( "Timecode started:" + started );
			current = started;
			elapsed = 0;
			fractionElapsed = 0;
			fractionUpdated = true;

		}

	}

	/**
	 *
	 * @return
	 */
	public static synchronized long getMilliTime()
	{
		return AnimationTimer.getCurrentTime();
	}

	/**
	 *
	 */
	public void update()
	{
		update( getMilliTime() );
	}

	public synchronized void update( long currentMillis )
	{
		current = currentMillis;
		elapsed = current - started - pausedTotal;
		fractionUpdated = false;
	}

	/**
	 *
	 * @return
	 */
	public synchronized long getCurrent()
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
	public synchronized void setDuration( long duration )
	{
		fractionElapsed = -1;
		this.duration = duration;
	}

	/**
	 * @return the duration
	 */
	public synchronized long getDuration()
	{
		return duration;
	}

	/**
	 * @return the fractionElapsed
	 * @throws Exception
	 */
	public synchronized double getFractionElapsed()
		throws DurationRequiredException
	{
		if (duration == 0)
		{
			throw new DurationRequiredException();
		}
		if (!fractionUpdated)
		{
			double timespent = elapsed;
			double total = duration;
			double value = timespent / total;

			setFractionElapsed( value );

			fractionUpdated = true;
		}

		return fractionElapsed;
	}

	private synchronized void setFractionElapsed( double value )
	{
		if (value > 1.0f)
		{
			//System.out.println( value );
			value = 1.0f;
		}
		this.fractionElapsed = value;
	}

	/**
	 *
	 * @param other
	 */
	public synchronized void setCurrent( TimeStamp other )
		throws DurationRequiredException
	{
		if (started == 0)
		{
			return;
		}
		current = other.current;
		elapsed = current - started - pausedTotal;

		if (isPeriodic)
		{
			elapsed = elapsed % duration;
		}
		//         System.out.printf( "Elapsed %d\n", elapsed );
		//         System.out.print( "Current updated:" + current + " started:" +
		//         started );
		fractionUpdated = false;
	}

	/**
	 * @param isPeridodic the isPeridodic to set
	 */
	public synchronized void setPeriodic( boolean isPeridodic )
	{
		this.isPeriodic = isPeridodic;
	}

	/**
	 * @return the isPeridodic
	 */
	public synchronized boolean isPeriodic()
	{
		return isPeriodic;
	}

	public synchronized void reset()
	{
		started = 0;

		elapsed = 0;

		current = 0;

		duration = 0;

		fractionUpdated = false;

		isPeriodic = false;

		/**
		 * The fraction of the total duration
		 */
		fractionElapsed = 0; //

		pausedTotal = 0;

		pauseStarted = 0;

	}

	public synchronized boolean isPaused()
	{
		return pauseStarted != 0;
	}

}
