package com.cybernostics.lib.persist.xml;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(value = RectangleAdapter.class, type = Rectangle2D.Float.class)
@XmlRootElement
public class RectFloat extends ShapeFloat
{

	PointFloat p1 = new PointFloat();

	float width;
	float height;

	public RectFloat()
	{

	}

	public RectFloat( double x1, double y1, double width, double height )
	{
		this.p1.setX( (float) x1 );
		this.p1.setY( (float) y1 );

		this.width = (float) width;
		this.height = (float) height;

	}

	public RectFloat( Point2D p1, Point2D p2 )
	{
		this( p1.getX(),
			p1.getY(),
			Math.abs( p1.getX() - p1.getX() ),
			Math.abs( p2.getY() - p1.getY() ) );
	}

	public RectFloat( Rectangle2D rect )
	{
		this( rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight() );
	}

	public float getHeight()
	{
		return height;
	}

	public PointFloat getP1()
	{
		return p1;
	}

	public float getWidth()
	{
		return width;
	}

	public void setHeight( float height )
	{
		this.height = height;
	}

	public void setP1( PointFloat p1 )
	{
		this.p1 = p1;
	}

	public void setWidth( float width )
	{
		this.width = width;
	}

}
