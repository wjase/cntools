package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public class AlphaCurtain extends ColorFill
{

	private AlphaComposite alpha = null;

	public void setAlpha( float alphaVal )
	{
		alpha = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			alphaVal );
	}

	public AlphaCurtain( Color toPaint, float alphaVal )
	{
		super( toPaint );
		alpha = AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			alphaVal );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		StateSaver sg = StateSaver.wrap( g2 );
		try
		{
			sg.setComposite( alpha );
			super.draw(
				sg,
				s );
		}
		finally
		{
			sg.restore();
		}

	}

	public float getAlpha()
	{
		return alpha.getAlpha();
	}

}
