package com.cybernostics.lib.gui.shapeeffects;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * This is a modifier and updates the g2 composite
 *
 * @author jasonw
 */
public class CompositeAdjust implements ShapeEffect
{

	AlphaComposite comp = null;

	public CompositeAdjust( AlphaComposite toSet )
	{
		this.comp = toSet;
	}

	/**
	 * Convenience method for most common alpha composite constructor
	 *
	 * @param alpha
	 */
	public CompositeAdjust( float alpha )
	{
		this.comp = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			alpha );
	}

	public CompositeAdjust( double alpha )
	{
		this( (float) alpha );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Composite gcomp = g2.getComposite();
		float existing_alpha = 1.0f;
		if (gcomp != null && gcomp instanceof AlphaComposite)
		{
			existing_alpha = ( (AlphaComposite) gcomp ).getAlpha();
		}
		if (existing_alpha != 1.0f)
		{
			g2.setComposite( comp.derive( comp.getAlpha() * existing_alpha ) );
		}
		else
		{
			g2.setComposite( comp );
		}

	}

	public void setLevel( double value )
	{
		comp = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			(float) value );
	}

	public float getLevel()
	{
		return comp.getAlpha();
	}

}
