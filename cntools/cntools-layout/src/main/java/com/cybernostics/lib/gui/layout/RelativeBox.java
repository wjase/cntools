/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author jasonw
 */
public class RelativeBox extends JComponent
{
	double horiz = 1;
	double vert = 1;

	Dimension size = new Dimension();
	JComponent currentParent = null;

	private void changeSize( Component c )
	{
		c.getSize( size );
		size.width *= horiz;
		size.height *= vert;

	}

	private ComponentAdapter sizeListener = new ComponentAdapter()
	{

		@Override
		public void componentResized( ComponentEvent e )
		{
			changeSize( e.getComponent() );
		}
	};

	public RelativeBox( double x, double y )
	{
		this.horiz = x;
		this.vert = y;

		addAncestorListener( new AncestorListener()
		{

			@Override
			public void ancestorAdded( AncestorEvent event )
			{
				Container c = getParent();
				if (c instanceof JComponent)
				{
					if (currentParent != null)
					{
						currentParent.removeComponentListener( sizeListener );
					}
					currentParent = (JComponent) c;
					currentParent.addComponentListener( sizeListener );
					changeSize( c );
				}
			}

			@Override
			public void ancestorRemoved( AncestorEvent event )
			{

			}

			@Override
			public void ancestorMoved( AncestorEvent event )
			{

			}
		} );

		addComponentListener( sizeListener );
	}

	@Override
	public Dimension getSize()
	{
		return size;
	}

	@Override
	public Dimension getMaximumSize()
	{
		return size;
	}

	@Override
	public Dimension getMinimumSize()
	{
		return size;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return size;
	}

}
