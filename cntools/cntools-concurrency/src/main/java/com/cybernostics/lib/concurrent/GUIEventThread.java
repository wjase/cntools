package com.cybernostics.lib.concurrent;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Call this static method to either execute the runnable immediately if called from
 * the Event handling thread, or later of not.
 * @author jasonw
 *
 */
public class GUIEventThread
{

	public static void show( final JFrame jf )
	{
		runLater( new Runnable()
		{

			@Override
			public void run()
			{
				jf.setVisible( true );
			}

		} );
	}

	public static void show( final Component c )
	{
		run( new Runnable()
		{

			@Override
			public void run()
			{
				c.setVisible( true );
			}

		} );
	}

	public static void runLater( Runnable toRun )
	{
		SwingUtilities.invokeLater( toRun );
	}

	public static void run( Runnable toRun )
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			toRun.run();
		}
		else
		{
			SwingUtilities.invokeLater( toRun );
		}
	}

	public static void runWait( Runnable toRun ) throws InterruptedException,
		InvocationTargetException
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			toRun.run();
		}
		else
		{
			SwingUtilities.invokeAndWait( toRun );
		}
	}

}
