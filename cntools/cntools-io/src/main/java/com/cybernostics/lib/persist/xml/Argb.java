package com.cybernostics.lib.persist.xml;

import java.awt.Color;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Argb
{

	@XmlAttribute(required = false)
	public int a = 0;
	@XmlAttribute(required = false)
	public int r = 0;
	@XmlAttribute(required = false)
	public int g = 0;
	@XmlAttribute(required = false)
	public int b = 0;

	public Argb()
	{

	}

	public Argb( Color c )
	{
		a = c.getAlpha();
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
	}

	public static Color getColor( Argb col )
	{
		return new Color( col.r, col.g, col.b, col.a );
	}

}
