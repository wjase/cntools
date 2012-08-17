package com.cybernostics.lib.gui.declarative.events;

import java.awt.AWTEvent;
import java.awt.Container;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;

public abstract class WhenVisibilityChanges
{

	public WhenVisibilityChanges( JComponent b )
	{
		new WhenMadeVisible( b )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				WhenVisibilityChanges.this.doThis( e );
			}

		};

		b.addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentHidden( ComponentEvent e )
			{
				WhenVisibilityChanges.this.doThis( e );
			}

		} );
	}

	public WhenVisibilityChanges( final Container b )
	{
		new WhenMadeVisible( b )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				WhenVisibilityChanges.this.doThis( e );
			}

		};

		b.addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentHidden( ComponentEvent e )
			{
				WhenVisibilityChanges.this.doThis( e );
			}

		} );

	}

	public abstract void doThis( AWTEvent e );

}
