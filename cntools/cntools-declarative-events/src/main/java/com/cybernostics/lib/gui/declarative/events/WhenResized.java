package com.cybernostics.lib.gui.declarative.events;

import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

abstract public class WhenResized implements ComponentListener
{

	public WhenResized( final JComponent b )
	{
		b.addComponentListener( this );
		if (b.isVisible())
		{
			if (b.getSize()
				.getWidth() > 0)
			{
				SwingUtilities.invokeLater( new Runnable()
				{

					@Override
					public void run()
					{
						doThis( new ComponentEvent( b,
							ComponentEvent.COMPONENT_RESIZED ) );
					}
				} );

			}
		}
	}

	/**
	 * @param parent
	 */
	public WhenResized( Container parent )
	{
		parent.addComponentListener( this );
		if (parent.isVisible())
		{
			if (parent.getSize()
				.getWidth() > 0)
			{
				doThis( new ComponentEvent( parent,
					ComponentEvent.COMPONENT_RESIZED ) );
			}
		}
	}

	@Override
	public void componentMoved( ComponentEvent e )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentHidden( ComponentEvent e )
	{

	}

	@Override
	public void componentResized( ComponentEvent e )
	{
		doThis( e );
	}

	@Override
	public void componentShown( ComponentEvent e )
	{
		doThis( e );
	}

	public abstract void doThis( ComponentEvent e );

}
