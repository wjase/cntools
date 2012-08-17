package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Component.BaselineResizeBehavior;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PanelUI;

/**
 *
 * @author jasonw
 */
public class ShapedPanelUI extends PanelUI
{

	PanelUI inner = null;

	public ShapedPanelUI( PanelUI towrap )
	{
		inner = towrap;
	}

	@Override
	public void update( Graphics g, JComponent c )
	{
		inner.update(
			g,
			c );
	}

	@Override
	public void uninstallUI( JComponent c )
	{
		inner.uninstallUI( c );
	}

	@Override
	public void paint( Graphics g, JComponent c )
	{
		c.paint( g );
	}

	@Override
	public void installUI( JComponent c )
	{
		inner.installUI( c );
	}

	@Override
	public Dimension getPreferredSize( JComponent c )
	{
		return inner.getPreferredSize( c );
	}

	@Override
	public Dimension getMinimumSize( JComponent c )
	{
		return inner.getMinimumSize( c );
	}

	@Override
	public Dimension getMaximumSize( JComponent c )
	{
		return inner.getMaximumSize( c );
	}

	@Override
	public BaselineResizeBehavior getBaselineResizeBehavior( JComponent c )
	{
		return inner.getBaselineResizeBehavior( c );
	}

	@Override
	public int getBaseline( JComponent c, int width, int height )
	{
		return inner.getBaseline(
			c,
			width,
			height );
	}

	@Override
	public int getAccessibleChildrenCount( JComponent c )
	{
		return inner.getAccessibleChildrenCount( c );
	}

	@Override
	public Accessible getAccessibleChild( JComponent c, int i )
	{
		return inner.getAccessibleChild(
			c,
			i );
	}

	public static ComponentUI createUI( JComponent c )
	{
		return new ShapedPanelUI( (PanelUI) UIManager.getUI( c ) );
	}

	@Override
	public boolean contains( JComponent c, int x, int y )
	{
		return inner.contains(
			c,
			x,
			y );
	}

}
