package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class ColorFill implements ShapeEffect
{

	private Color toPaint = null;

	public ColorFill( Color toPaint )
	{
		this.toPaint = toPaint;
	}

	public Color getToPaint()
	{
		return toPaint;
	}

	public void setToPaint( Color toPaint )
	{
		this.toPaint = toPaint;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		g2.setColor( toPaint );
		g2.fill( s );
	}
}
