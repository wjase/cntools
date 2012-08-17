package com.cybernostics.lib.gui.declarative.events;

import javax.swing.SwingUtilities;

public abstract class RunLater
{

	public RunLater( final Object... args )
	{
		SwingUtilities.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				RunLater.this.run( args );

			}
		} );
	}

	public abstract void run( Object... args );

	/**
	 * @param doUpdate
	 */
	public static void doThis( Runnable toRun )
	{
		new RunLater( toRun )
		{

			@Override
			public void run( Object... arg )
			{
				( (Runnable) arg[ 0 ] ).run();

			}
		};

	}
}
