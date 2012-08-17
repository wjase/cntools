package com.cybernostics.lib.gui.mouse;

import com.cybernostics.lib.concurrent.GUIEventThread;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author jasonw
 */
public class MouseWatcher
{

	public static boolean mousePressed = false;

	private static AWTEventListener listener = new AWTEventListener()
	{

		@Override
		public void eventDispatched( AWTEvent e )
		{
			if (e.getID() == MouseEvent.MOUSE_PRESSED)
			{
				mousePressed = true;
			}
			if (e.getID() == MouseEvent.MOUSE_RELEASED)
			{
				mousePressed = false;
			}
		}

	};

	public static void register()
	{
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK
			+ AWTEvent.MOUSE_EVENT_MASK;

		Toolkit.getDefaultToolkit()
			.addAWTEventListener(
				listener,
				eventMask );
	}

	public static boolean getMousePressed()
	{
		return mousePressed;
	}

	public static void main( String[] args )
	{

		MouseWatcher.register();

		JFrame jf = new JFrame( "Watcher" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setSize(
			400,
			400 );
		GUIEventThread.show( jf );

		boolean state = false;
		while (true)
		{
			try
			{
				Thread.sleep( 200 );

				if (state != MouseWatcher.getMousePressed())
				{
					state = MouseWatcher.getMousePressed();
					System.out.println( "MousePressedd:" + state );
				}
			}
			catch (InterruptedException ex)
			{
				Logger.getLogger(
					MouseWatcher.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}

	}

}
