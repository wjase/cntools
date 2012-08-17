package com.cybernostics.lib.persist.xml;

import java.awt.geom.Point2D;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Point2DAdapter extends XmlAdapter< PointFloat, Point2D.Float >
{

	@Override
	public PointFloat marshal( Point2D.Float v ) throws Exception
	{
		if (v != null)
		{
			return new PointFloat( v.x, v.y );
		}
		return null;
	}

	@Override
	public Point2D.Float unmarshal( PointFloat v ) throws Exception
	{
		if (v != null)
		{
			return new Point2D.Float( v.x, v.y );
		}
		return null;
	}
}
