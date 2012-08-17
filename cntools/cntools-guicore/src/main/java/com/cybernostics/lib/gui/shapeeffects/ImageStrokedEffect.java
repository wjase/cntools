package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 * Fills the shape with the specified image
 * @author jasonw
 */
public class ImageStrokedEffect implements ShapeEffect
{

	Paint toFill = null;

	public ImageStrokedEffect( BufferedImage tile )
	{
		toFill = new TexturePaint( tile, new Rectangle( 0,
			0,
			tile.getWidth(),
			tile.getHeight() ) );
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Paint old = g2.getPaint();
		g2.setPaint( toFill );
		g2.fill( g2.getStroke()
			.createStrokedShape(
				s ) );
		g2.setPaint( old );
	}

}
