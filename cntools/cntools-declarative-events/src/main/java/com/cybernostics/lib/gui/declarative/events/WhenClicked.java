package com.cybernostics.lib.gui.declarative.events;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JList;

public abstract class WhenClicked
{

	public WhenClicked( ActionEventSource action )
	{
		action.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				doThis( e );
			}

		} );
	}

	public WhenClicked( AbstractButton b )
	{
		b.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				doThis( e );

			}

		} );
	}

	public WhenClicked( final JComponent jc )
	{
		jc.addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent e )
			{
				doThis( new ActionEvent( jc,
					0,
					"click",
					AWTEvent.ITEM_EVENT_MASK,
					0 ) );

			}

		} );
	}

	public WhenClicked( final JList jl )
	{
		jl.addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent e )
			{
				doThis( new ActionEvent( jl,
					0,
					"click",
					AWTEvent.ITEM_EVENT_MASK,
					0 ) );

			}

		} );
	}

	public abstract void doThis( ActionEvent e );

}
