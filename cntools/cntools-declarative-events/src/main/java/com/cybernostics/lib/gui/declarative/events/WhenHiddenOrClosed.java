package com.cybernostics.lib.gui.declarative.events;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public abstract class WhenHiddenOrClosed
{

	private final Window closedWindow;

	public WhenHiddenOrClosed( final JComponent b )
	{

		closedWindow = SwingUtilities.getWindowAncestor( b );
		b.addAncestorListener( new AncestorListener()
		{

			public void ancestorAdded( AncestorEvent event )
			{
				Container c = b.getRootPane()
					.getParent();
				while (c.getParent() != null)
				{
					c = c.getParent();
				}
				if (c instanceof Window)
				{
					Window w = (Window) c;
					w.addWindowListener( new WindowAdapter()
					{

						@Override
						public void windowClosed( WindowEvent e )
						{
						}

						@Override
						public void windowClosing( WindowEvent e )
						{
							doThis();
						}

					} );
				}
			}

			@Override
			public void ancestorMoved( AncestorEvent event )
			{

			}

			@Override
			public void ancestorRemoved( AncestorEvent event )
			{
				doThis();

			};

		} );

		b.addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentHidden( ComponentEvent e )
			{
				doThis();
			}
		} );
	}

	public WhenHiddenOrClosed( final Window d )
	{
		closedWindow = d;

		d.addWindowListener( new WindowAdapter()
		{

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.WindowAdapter#windowClosed(java.awt.event.WindowEvent
			 * )
			 */
			@Override
			public void windowClosed( WindowEvent e )
			{
				System.out.printf( "closed\n" );
				doThis();
			}

			@Override
			public void windowClosing( WindowEvent e )
			{
				System.out.printf( "closing\n" );
				doThis();

			}
		} );
	}

	public abstract void doThis();

	protected Window getWindow()
	{
		return closedWindow;
	}

	public static void main( String[] args )
	{
		final JFrame jf = new JFrame( "Hidden Test" );
		jf.setSize(
			200,
			200 );
		jf.setVisible( true );

		JButton jb = new JButton( "Close" );

		jf.getContentPane()
			.add(
				jb );

		new WhenClicked( jb )
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cybernostics.lib.gui.declarative.events.WhenClicked#doThis
			 * (java.awt.event.ActionEvent)
			 */
			@Override
			public void doThis( ActionEvent e )
			{
				jf.setVisible( false );
				//jf.closeIfAllowed();

			}
		};

		new WhenHiddenOrClosed( jf )
		{
			@Override
			public void doThis()
			{
				System.out.printf( "hidden\n" );
				System.exit( 0 );
			}
		};
	}
}
