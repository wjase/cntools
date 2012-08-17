package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class SetColor implements ShapeEffect
{

	public static ShapeEffect get( Color c )
	{
		return new SetColor( c );
	}

	Color toSet = Color.BLACK;

	public SetColor( Color toSet )
	{
		this.toSet = toSet;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		g2.setColor( toSet );
	}

}
