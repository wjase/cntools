package com.cybernostics.lib.persist.xml;

import java.awt.geom.Rectangle2D;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class RectangleAdapter extends
	XmlAdapter< RectFloat, Rectangle2D.Double >
{

	@Override
	public RectFloat marshal( Rectangle2D.Double v ) throws Exception
	{
		if (v != null)
		{
			return new RectFloat( v.getMinX(),
				v.getMinY(),
				v.getWidth(),
				v.getHeight() );
		}
		return null;
	}

	@Override
	public Rectangle2D.Double unmarshal( RectFloat v ) throws Exception
	{
		if (v != null)
		{

			return new Rectangle2D.Double( v.getP1()
				.getX(), v.getP1()
				.getY(), v.getWidth(), v.getHeight() );
		}
		return null;
	}
}
