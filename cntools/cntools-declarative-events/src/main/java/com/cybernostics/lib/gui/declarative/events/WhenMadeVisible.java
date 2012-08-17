package com.cybernostics.lib.gui.declarative.events;

import com.cybernostics.lib.concurrent.GUIEventThread;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.*;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public abstract class WhenMadeVisible
	implements
	AncestorListener,
	ComponentListener
{

	@Override
	public void componentHidden( ComponentEvent e )
	{
		armed = true;

	}

	@Override
	public void componentMoved( ComponentEvent e )
	{
	}

	@Override
	public void componentResized( ComponentEvent e )
	{
	}

	@Override
	public void componentShown( ComponentEvent e )
	{
		triggerEvent( e );
	}

	boolean armed = true;

	public WhenMadeVisible( final JComponent b )
	{
		if (b.getParent() != null)
		{
			if (b.getParent()
				.isVisible())
			{
				GUIEventThread.run( new Runnable()
				{

					@Override
					public void run()
					{
						triggerEvent( new ComponentEvent( (Component) b,
							(int) AWTEvent.COMPONENT_EVENT_MASK ) );
						armed = false;
					}

				} );
			}
		}
		b.addAncestorListener( this );
	}

	public WhenMadeVisible( Container b )
	{
		if (b.isVisible())
		{
			triggerEvent( new ComponentEvent( b,
				(int) AWTEvent.COMPONENT_EVENT_MASK ) );
			armed = false;
		}
		b.addComponentListener( this );

		Window ancestor = SwingUtilities.getWindowAncestor( b );
		if (ancestor != null)
		{
			ancestor.addWindowListener( new WindowListener()
			{

				@Override
				public void windowActivated( WindowEvent e )
				{
				}

				@Override
				public void windowClosed( WindowEvent e )
				{
					armed = true;

				}

				@Override
				public void windowClosing( WindowEvent e )
				{
					// TODO Auto-generated method stub
				}

				@Override
				public void windowDeactivated( WindowEvent e )
				{
					armed = true;

				}

				@Override
				public void windowDeiconified( WindowEvent e )
				{
					triggerEvent( e );

				}

				@Override
				public void windowIconified( WindowEvent e )
				{
					armed = true;
				}

				@Override
				public void windowOpened( WindowEvent e )
				{
					triggerEvent( e );
				}

			} );
		}
	}

	public void ancestorAdded( AncestorEvent event )
	{
		triggerEvent( event );
	}

	@Override
	public void ancestorMoved( AncestorEvent event )
	{
	}

	@Override
	public void ancestorRemoved( AncestorEvent event )
	{
		armed = true;

	}

	;

	public WhenMadeVisible( Window win )
	{
		win.addWindowListener( new WindowAdapter()
		{

			@Override
			public void windowOpened( WindowEvent e )
			{
				triggerEvent( e );

			}

		} );
	}

	public void triggerEvent( AWTEvent e )
	{
		if (armed == true)
		{
			doThis( e );
			armed = false;
		}

	}

	public abstract void doThis( AWTEvent e );

}
