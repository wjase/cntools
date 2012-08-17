package com.cybernostics.lib.persist.xml;

import java.awt.geom.Line2D;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LineAdapter extends XmlAdapter< LineFloat, Line2D.Float >
{

	@Override
	public LineFloat marshal( Line2D.Float v ) throws Exception
	{
		if (v != null)
		{
			return new LineFloat( v.getP1(), v.getP2() );
		}
		return null;
	}

	@Override
	public Line2D.Float unmarshal( LineFloat v ) throws Exception
	{
		if (v != null)
		{

			return new Line2D.Float( v.getP1()
				.getX(), v.getP1()
				.getY(), v.getP2()
				.getX(), v.getP2()
				.getY() );
		}
		return null;
	}
}
