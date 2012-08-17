package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

public class WhenDialogClosed
{

	public WhenDialogClosed( JDialog dialog )
	{
		dialog.addWindowListener( new WindowAdapter()
		{

			@Override
			public void windowClosed( WindowEvent e )
			{
				doThis();
			}
		} );
	}

	public void doThis()
	{
		return;
	}

}
