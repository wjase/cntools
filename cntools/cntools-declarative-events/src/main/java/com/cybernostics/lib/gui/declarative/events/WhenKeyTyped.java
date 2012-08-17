package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;

public abstract class WhenKeyTyped
{

	public WhenKeyTyped( final JComponent jc )
	{
		jc.addKeyListener( new KeyAdapter()
		{

			@Override
			public void keyTyped( KeyEvent e )
			{
				doThis( e );
			}
		} );
	}

	public WhenKeyTyped( final JComponent jc, final char code )
	{
		jc.addKeyListener( new KeyAdapter()
		{

			@Override
			public void keyTyped( KeyEvent e )
			{
				if (e.getKeyChar() == code)
				{
					doThis( e );
				}

			}
		} );
	}

	public abstract void doThis( KeyEvent e );

}
