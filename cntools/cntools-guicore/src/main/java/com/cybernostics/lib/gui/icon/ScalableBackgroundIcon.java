package com.cybernostics.lib.gui.icon;

import com.cybernostics.lib.media.icon.PreferredSizeListener;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

/**
 * @author jasonw
 *
 */
public class ScalableBackgroundIcon implements ScalableIcon
{

	ScalableIcon toRender = null;
	Color background;

	public ScalableBackgroundIcon( ScalableIcon toRender, Color background )
	{
		this.toRender = toRender;
		this.background = background;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#addPreferredSizeListener(com.cybernostics.lib.gui.icon.PreferredSizeListener)
	 */

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		toRender.addPreferredSizeListener( listener );
	}

	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	/*
	/* (non-Javadoc)
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#copy()
	 */

	@Override
	public ScalableIcon copy()
	{
		return new ScalableBackgroundIcon( toRender, background );
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setSize( Dimension2D d )
	{
		if (( dMin != null )
			&& ( ( d.getWidth() < dMin.width ) || ( d.getHeight() < dMin.height ) ))
		{
			return;
		}
		toRender.setSize( d );
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight()
	{
		return toRender.getIconHeight();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth()
	{
		return toRender.getIconWidth();
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		g.setColor( background );
		g.drawRect(
			0,
			0,
			toRender.getIconWidth(),
			toRender.getIconHeight() );
		toRender.paintIcon(
			c,
			g,
			x,
			y );
	}

	@Override
	public BufferedImage getImage()
	{
		return toRender.getImage();
	}

	@Override
	public Dimension getPreferredSize()
	{
		return toRender.getPreferredSize();
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
	}
}
