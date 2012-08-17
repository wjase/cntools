package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.*;

/**
 * Initialises a region to transparent (assuming it supports transparency)
 *
 * @author jasonw
 */
public class TransparentFill implements ShapeEffect
{
	public static void apply( Graphics2D g2, Shape s )
	{
		onlyOne.get()
			.draw(
				g2,
				s );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Composite comp = g2.getComposite();
		g2.setColor( new Color( 0, 0, 0, 0 ) );
		g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OUT ) );
		g2.fill( s );
		g2.setComposite( comp );
	}

	private static SingletonInstance< TransparentFill > onlyOne = new SingletonInstance< TransparentFill >()
	{

		@Override
		protected TransparentFill createInstance()
		{
			return new TransparentFill();
		}
	};

}
