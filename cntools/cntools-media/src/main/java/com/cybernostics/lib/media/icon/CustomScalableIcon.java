package com.cybernostics.lib.media.icon;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public abstract class CustomScalableIcon implements ScalableIcon
{

	Dimension2D d = ScreenRelativeIconSizer.getDefaultDimension();
	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	@Override
	public void setSize( Dimension2D d )
	{
		if (( dMin != null )
			&& ( ( d.getWidth() >= dMin.width ) && ( d.getHeight() >= dMin.height ) ))
		{
			this.d = d;
		}
	}

	@Override
	public ScalableIcon copy()
	{
		return this;
	}

	List< PreferredSizeListener > listeners = new ArrayList< PreferredSizeListener >();

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		listeners.add( listener );
	}

	@Override
	public int getIconWidth()
	{
		return (int) d.getWidth();
	}

	@Override
	public int getIconHeight()
	{
		return (int) d.getHeight();
	}
}
