package com.cybernostics.lib.gui.declarative.events;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class WhenChanged
{

	public WhenChanged( AbstractButton b )
	{
		this( b.getModel() );
	}

	public WhenChanged( final ButtonModel m )
	{
		m.addChangeListener( new ChangeListener()
		{

			@Override
			public void stateChanged( ChangeEvent e )
			{

				doThis(
					e,
					(ButtonModel) e.getSource() );

			}
		} );
	}

	public abstract void doThis( ChangeEvent e, ButtonModel m );
}
