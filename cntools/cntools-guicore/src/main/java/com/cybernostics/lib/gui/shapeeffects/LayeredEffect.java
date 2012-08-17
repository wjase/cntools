package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * Embodies a ShapeEffect for foreground, background and child components for a
 * Component. Usage:
 *
 * Override your draw / paint method and replace with this draw method
 *
 * @author jasonw
 */
public class LayeredEffect implements ShapeEffect
{

	private ShapeEffect content = NOPEffect.get();

	public ShapeEffect getContent()
	{
		return content;
	}

	public void setContent( ShapeEffect content )
	{
		this.content = content;
	}

	private ShapeEffect background = NOPEffect.get();

	public ShapeEffect getBackground()
	{
		return background;
	}

	public void setBackground( ShapeEffect background )
	{
		this.background = background;
	}

	public ShapeEffect getForeground()
	{
		return foreground;
	}

	public void setForeground( ShapeEffect foreground )
	{
		this.foreground = foreground;
	}

	private ShapeEffect foreground = NOPEffect.get();

	public LayeredEffect()
	{
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		background.draw(
			g2,
			s );
		content.draw(
			g2,
			s );
		foreground.draw(
			g2,
			s );
	}

}
