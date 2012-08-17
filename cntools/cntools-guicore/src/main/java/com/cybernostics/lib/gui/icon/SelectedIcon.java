package com.cybernostics.lib.gui.icon;

import com.cybernostics.lib.media.icon.PreferredSizeListener;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Icon;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.geom.Dimension2D;

/**
 * @author jasonw
 * 
 */
public class SelectedIcon implements ScalableIcon
{

	Icon toRender;

	public SelectedIcon( Icon toRender )
	{
		this.toRender = toRender;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight()
	{
		return toRender.getIconHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth()
	{
		return toRender.getIconWidth();
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#copy()
	 */
	@Override
	public ScalableIcon copy()
	{
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			g2.translate(
				x,
				y );
			double xScale = ( (double) getIconWidth() - 4 ) / getIconWidth();
			double yScale = ( (double) getIconHeight() - 4 ) / getIconHeight();

			g2.scale(
				xScale,
				yScale );
			toRender.paintIcon(
				c,
				g,
				(int) ( 2 / xScale ),
				(int) ( 2 / yScale ) );
			Stroke s = new BasicStroke( 2 );
			g2.scale(
				1 / xScale,
				1 / yScale );
			g.setColor( Color.yellow );
			g2.setStroke( s );
			g2.draw( new Rectangle( 1,
				1,
				getIconWidth() - 3,
				getIconHeight() - 3 ) );
		}
		finally
		{
			g2.restore();
		}

	}

	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cybernostics.lib.gui.icon.ScalableIcon#setPreferredSize(java.awt.
	 * Dimension)
	 */

	@Override
	public void setSize( Dimension2D d )
	{
		if (( dMin != null )
			&& ( ( d.getWidth() < dMin.width ) || ( d.getHeight() < dMin.height ) ))
		{
			return;
		}
		firePreferredSizeChanged( d );

	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#addPreferredSizeListener(com.cybernostics.lib.gui.icon.PreferredSizeListener)
	 */
	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		sizeListeners.add( listener );

	}

	private ArrayList< PreferredSizeListener > sizeListeners = new ArrayList< PreferredSizeListener >();

	private void firePreferredSizeChanged( Dimension2D d )
	{
		for (PreferredSizeListener eachListener : sizeListeners)
		{
			eachListener.preferredSizeChanged( d );
		}
	}

	@Override
	public BufferedImage getImage()
	{
		return null;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension( toRender.getIconWidth(), toRender.getIconHeight() );
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
	}

}
