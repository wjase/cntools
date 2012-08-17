package com.cybernostics.lib.gui.shape;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class IconRect extends Rectangle2D
{

	public static Rectangle2D get( Icon ic )
	{
		return new IconRect( ic );
	}

	public static Dimension2D getDimension( final Icon toRender )
	{
		return new Dimension2D()
		{

			@Override
			public double getWidth()
			{
				return toRender.getIconWidth();
			}

			@Override
			public double getHeight()
			{
				return toRender.getIconHeight();
			}

			@Override
			public void setSize( double width, double height )
			{
				throw new UnsupportedOperationException( "Not supported ever." );
			}
		};
	}

	Icon ic = null;

	public IconRect( Icon ic )
	{
		this.ic = ic;
	}

	@Override
	public void setRect( double x, double y, double w, double h )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public int outcode( double x, double y )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public Rectangle2D createIntersection( Rectangle2D r )
	{
		return r.createIntersection( this );
	}

	@Override
	public Rectangle2D createUnion( Rectangle2D r )
	{
		return r.createUnion( this );
	}

	@Override
	public double getX()
	{
		return 0;
	}

	@Override
	public double getY()
	{
		return 0;
	}

	@Override
	public double getWidth()
	{
		return ic.getIconWidth();
	}

	@Override
	public double getHeight()
	{
		return ic.getIconHeight();
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}

}
