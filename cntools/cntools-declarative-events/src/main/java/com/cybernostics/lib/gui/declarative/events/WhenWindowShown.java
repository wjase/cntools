package com.cybernostics.lib.gui.declarative.events;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

abstract public class WhenWindowShown
{

	public WhenWindowShown( Window dialog )
	{
		dialog.addWindowListener( new WindowAdapter()
		{

			@Override
			public void windowOpened( WindowEvent e )
			{
				doThis( e );

			}

		} );
	}

	public abstract void doThis( WindowEvent e );

}
