package com.cybernostics.lib.gui.icon;

import com.cybernostics.lib.media.icon.PreferredSizeListener;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.image.BitmapMaker;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

/**
 *
 * @author jasonw
 */
public class BlankIcon implements ScalableIcon
{

	public static SingletonInstance< BlankIcon > theOne = new SingletonInstance< BlankIcon >()
	{

		@Override
		protected BlankIcon createInstance()
		{
			return new BlankIcon();
		}

	};

	public static BlankIcon get()
	{
		return theOne.get();
	}

	private BlankIcon()
	{
	}

	@Override
	public void setSize( Dimension2D d )
	{
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension( getIconWidth(), getIconHeight() );
	}

	@Override
	public void setMinimumSize( Dimension d )
	{
	}

	@Override
	public ScalableIcon copy()
	{
		return get();
	}

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
	}

	@Override
	public BufferedImage getImage()
	{
		return BitmapMaker.createFastImage(
			getIconWidth(),
			getIconHeight() );
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
	}

	@Override
	public int getIconWidth()
	{
		return 100;
	}

	@Override
	public int getIconHeight()
	{
		return 100;
	}

}
