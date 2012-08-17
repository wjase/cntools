package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class EventConsumer
	implements
	MouseListener,
	KeyListener,
	MouseMotionListener
{

	private static final SingletonInstance< EventConsumer > eater = new SingletonInstance< EventConsumer >()
	{

		@Override
		protected EventConsumer createInstance()
		{
			return new EventConsumer();
		}
	};

	// creates an invisible panel which eats events
	public static ShapedPanel getEventSinkPanel()
	{
		ShapedPanel sp = new ShapedPanel();
		apply( sp );
		return sp;
	}

	public static void apply( JComponent comp )
	{
		comp.addMouseListener( eater.get() );
		comp.addMouseMotionListener( eater.get() );
		comp.addKeyListener( eater.get() );
	}

	private EventConsumer()
	{

	}

	@Override
	public void mouseClicked( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mousePressed( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseReleased( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseEntered( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseExited( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void keyTyped( KeyEvent e )
	{
		e.consume();
	}

	@Override
	public void keyPressed( KeyEvent e )
	{
		e.consume();
	}

	@Override
	public void keyReleased( KeyEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseDragged( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

	@Override
	public void mouseMoved( java.awt.event.MouseEvent e )
	{
		e.consume();
	}

}
