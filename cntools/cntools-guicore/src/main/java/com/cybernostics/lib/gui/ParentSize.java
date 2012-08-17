package com.cybernostics.lib.gui;

import com.cybernostics.lib.collections.IterableArray;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 *
 * @author jasonw
 */
public class ParentSize extends ComponentAdapter
{

	private Container parentComponent = null;

	private Component child = null;

	private ParentSize( Container parentComponent, Component child )
	{
		this.parentComponent = parentComponent;
		this.child = child;
	}

	public static void bind( final Container src, final Component target )
	{
		target.setBounds(
			0,
			0,
			src.getWidth(),
			src.getHeight() );
		target.setPreferredSize( new Dimension( src.getWidth(), src.getHeight() ) );
		ParentSize listener = new ParentSize( src, target );
		src.addComponentListener( listener );
		target.repaint( 5 );
		target.invalidate();
	}

	// unbind the child component when it is removed from the parent
	public static void unbind( final Container parent )
	{
		for (ComponentListener eachListener : IterableArray.get( parent.getComponentListeners() ))
		{
			if (eachListener instanceof ParentSize)
			{
				parent.removeComponentListener( eachListener );
				break;
			}
		}

	}

	@Override
	public void componentShown( ComponentEvent e )
	{
		updateChildSize( e );
	}

	@Override
	public void componentResized( ComponentEvent e )
	{
		updateChildSize( e );
	}

	private void updateChildSize( ComponentEvent e )
	{
		child.setBounds(
			0,
			0,
			parentComponent.getWidth(),
			parentComponent.getHeight() );
		child.setPreferredSize( new Dimension( parentComponent.getWidth(),
			parentComponent.getHeight() ) );
		child.invalidate();
		child.repaint( 5 );
	}

}
