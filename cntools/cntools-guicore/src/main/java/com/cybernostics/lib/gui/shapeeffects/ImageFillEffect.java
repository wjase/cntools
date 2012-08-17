package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.media.image.BitmapMaker;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 * Fills the shape with the specified image
 * @author jasonw
 */
public class ImageFillEffect implements ShapeEffect
{

	Paint toFill = null;

	public ImageFillEffect( Icon toFillIcon )
	{
		int width = toFillIcon.getIconWidth();
		int height = toFillIcon.getIconHeight();
		BufferedImage texImage = BitmapMaker.createFastImage(
			width,
			height,
			BufferedImage.TRANSLUCENT );
		Graphics2D g2 = (Graphics2D) texImage.getGraphics();
		toFillIcon.paintIcon(
			null,
			g2,
			0,
			0 );
		toFill = new TexturePaint( texImage,
			new Rectangle( 0, 0, width, height ) );

	}

	public ImageFillEffect( BufferedImage tile )
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
		g2.fill( s );
		g2.setPaint( old );
	}
}
