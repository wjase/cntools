package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class TranslucentColorFill implements ShapeEffect
{

	private CompositeAdjust tranlucency = null;
	private ColorFill fillColor = null;

	ShapeEffectStack renderer;

	public TranslucentColorFill( Color toPaint, float alpha )
	{
		tranlucency = new CompositeAdjust( alpha );
		fillColor = new ColorFill( toPaint );
		renderer = new ShapeEffectStack( tranlucency, fillColor );

	}

	public Color getToPaint()
	{
		return fillColor.getToPaint();
	}

	public void setToPaint( Color toPaint )
	{
		fillColor.setToPaint( toPaint );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		AtomicDraw.draw(
			renderer,
			g2,
			s );
	}

}
