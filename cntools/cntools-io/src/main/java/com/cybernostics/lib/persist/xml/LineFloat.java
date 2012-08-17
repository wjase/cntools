package com.cybernostics.lib.persist.xml;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LineFloat extends ShapeFloat
{

	PointFloat p1 = new PointFloat();
	PointFloat p2 = new PointFloat();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineFloat()
	{

	}

	public LineFloat( float x1, float y1, float x2, float y2 )
	{
		p1.x = x1;
		p1.y = y1;
		p2.x = x2;
		p2.y = y2;
	}

	public LineFloat( Line2D.Float line )
	{
		this( line.getP1(), line.getP2() );
	}

	public LineFloat( Point2D p1, Point2D p2 )
	{
		setP1( p1 );
		setP2( p2 );
	}

	@XmlElement
	public PointFloat getP1()
	{
		return p1;
	}

	@XmlElement
	public PointFloat getP2()
	{
		return p2;
	}

	public void setP1( Point2D p )
	{
		p1.x = (float) p.getX();
		p1.y = (float) p.getY();
	}

	public void setP1( PointFloat p )
	{
		p1.x = p.x;
		p1.y = p.y;
	}

	public void setP2( Point2D p )
	{
		p2.x = (float) p.getX();
		p2.y = (float) p.getY();
	}

	public void setP2( PointFloat p )
	{
		p2.x = p.x;
		p2.y = p.y;
	}
}
