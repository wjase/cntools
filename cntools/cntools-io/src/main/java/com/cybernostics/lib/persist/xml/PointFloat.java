package com.cybernostics.lib.persist.xml;

import java.awt.geom.Point2D;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(value = Point2DAdapter.class, type = Point2D.Float.class)
@XmlRootElement
public class PointFloat
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5841761576095387421L;

	float x;
	float y;

	public PointFloat()
	{
	}

	public PointFloat( float x, float y )
	{
		this.x = x;
		this.y = y;
	}

	@XmlAttribute
	public float getX()
	{
		return x;
	}

	@XmlAttribute
	public float getY()
	{
		return y;
	}

	public void setX( float x )
	{
		this.x = x;

	}

	public void setY( float y )
	{
		this.y = y;
	}
}
