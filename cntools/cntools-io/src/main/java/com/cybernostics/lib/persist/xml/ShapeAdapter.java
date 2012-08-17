package com.cybernostics.lib.persist.xml;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ShapeAdapter extends XmlAdapter< ShapeFloat, Shape >
{

	@Override
	public ShapeFloat marshal( Shape v ) throws Exception
	{
		if (v instanceof Line2D.Float)
		{
			return new LineFloat( (Line2D.Float) v );
		}
		if (v instanceof Rectangle2D.Float)
		{
			return new RectFloat( (Rectangle2D.Float) v );
		}
		if (v instanceof Path2D.Float)
		{
			return new PathFloat( (Path2D.Float) v );
		}
		return null;
	}

	@Override
	public Shape unmarshal( ShapeFloat v ) throws Exception
	{
		if (v instanceof LineFloat)
		{
			LineFloat lf = (LineFloat) v;
			return new Line2D.Float( lf.p1.getX(),
				lf.p1.getY(),
				lf.p2.getX(),
				lf.p2.getY() );
		}
		if (v instanceof PathFloat)
		{
			return PathFloat.toPath2D( (PathFloat) v );
		}
		if (v instanceof RectFloat)
		{
			RectFloat rf = (RectFloat) v;
			return new Rectangle2D.Float( rf.p1.getX(),
				rf.p1.getY(),
				rf.getWidth(),
				rf.getHeight() );
		}
		return null;
	}

}
