package com.cybernostics.lib.gui.layout;

import java.awt.*;

/**
 * @author J. H. S.
 */
public class CenteredLayout implements LayoutManager
{

	@Override
	public void addLayoutComponent( String name, Component comp )
	{
	}

	@Override
	public void removeLayoutComponent( Component comp )
	{
	}

	@Override
	public Dimension preferredLayoutSize( Container toLayout )
	{
		Insets insets = toLayout.getInsets();
		int childCount = toLayout.getComponentCount();
		if (childCount > 0)
		{
			Dimension d = toLayout.getComponent(
				0 )
				.getPreferredSize();
			return new Dimension( d.width + insets.left + insets.right,
				d.height + insets.top + insets.bottom );
		}
		else
		{
			return new Dimension( insets.left + insets.right, insets.top
				+ insets.bottom );
		}
	}

	/*
	 * (non-Javadoc) @see
	 * java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	@Override
	public Dimension minimumLayoutSize( Container toLayout )
	{
		java.awt.Insets insets = toLayout.getInsets();
		int childCount = toLayout.getComponentCount();
		if (childCount > 0)
		{
			Dimension d = toLayout.getComponent(
				0 )
				.getMinimumSize();
			return new Dimension( d.width + insets.left + insets.right,
				d.height + insets.top + insets.bottom );
		}
		else
		{
			return new Dimension( insets.left + insets.right, insets.top
				+ insets.bottom );
		}
	}

	/*
	 * (non-Javadoc) @see
	 * java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	@Override
	public void layoutContainer( Container toLayout )
	{
		int childCount = toLayout.getComponentCount();
		if (childCount > 0)
		{
			Component child = toLayout.getComponent( 0 );
			Insets insets = toLayout.getInsets();

			// Caluculate the free space around the child
			int freeWidth = toLayout.getWidth() - insets.left - insets.right;
			int freeHeight = toLayout.getHeight() - insets.top - insets.bottom;
			Dimension preferredSize = child.getPreferredSize();
			double preferredWidth = preferredSize.getWidth();
			double preferredHeight = preferredSize.getHeight();

			int width;
			int height;
			int x;
			int y;
			if (preferredWidth < freeWidth)
			{
				x = (int) Math.round( insets.left
					+ ( freeWidth - preferredWidth ) / 2 );
				width = (int) Math.round( preferredWidth );
			}
			else
			{
				x = insets.left;
				width = freeWidth;
			}
			if (preferredHeight < freeHeight)
			{
				y = (int) Math.round( insets.top
					+ ( freeHeight - preferredHeight ) / 2 );
				height = (int) Math.round( preferredHeight );
			}
			else
			{
				y = insets.top;
				height = freeHeight;
			}
			child.setBounds(
				x,
				y,
				width,
				height );
		}
	}

	private static CenteredLayout instance = new CenteredLayout();

	public static CenteredLayout get()
	{
		return instance;
	}

}
