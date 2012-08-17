package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

public abstract class WhenKeyPressed
{

	public WhenKeyPressed( final JComponent jc )
	{
		jc.addKeyListener( new KeyAdapter()
		{

			@Override
			public void keyPressed( KeyEvent e )
			{
				doThis( e );
			}
		} );
	}

	public WhenKeyPressed( final JComponent jc, final char code )
	{
		jc.addKeyListener( new KeyAdapter()
		{

			@Override
			public void keyPressed( KeyEvent e )
			{
				if (e.getKeyCode() == code)
				{
					doThis( e );
				}

			}
		} );
	}

	public abstract void doThis( KeyEvent e );

}
