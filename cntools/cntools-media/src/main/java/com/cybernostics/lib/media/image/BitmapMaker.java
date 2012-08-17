package com.cybernostics.lib.media.image;

import com.cybernostics.lib.maths.Random;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class BitmapMaker
{

	public static BufferedImage createImage( Icon brushIcon )
	{
		BufferedImage bi = BitmapMaker.createFastImage(
			brushIcon.getIconWidth(),
			brushIcon.getIconHeight(),
														BufferedImage.TRANSLUCENT );
		brushIcon.paintIcon(
			null,
			bi.getGraphics(),
			0,
			0 );

		return bi;
	}

	/**
	 * Creates a bitmap with the right translucency and dimensions which will
	 * perform well on this graphics device
	 *
	 * @param width width of bitmap
	 * @param height height of bitmap
	 * @param transparency - one of Transparency.OPAQUE,Transparency.BITMASK or
	 * Transparency.TRANSLUCENT
	 * @return
	 */
	public static BufferedImage createFastImage( int width,
		int height,
		int transparency )
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();

		width = width == 0 ? 1 : width;
		height = height == 0 ? 1 : height;
		// Create an image that does not support transparency
		BufferedImage bimage = gc.createCompatibleImage(
			width,
			height,
			transparency );
		return bimage;
	}

	public static BufferedImage createFastImage( int width, int height )
	{
		return createFastImage(
			width,
			height,
			Transparency.OPAQUE );
	}

	public static BufferedImage getScaled( Image source, int width, int height )
	{
		BufferedImage bi2 = new BufferedImage( width,
			height,
			BufferedImage.TYPE_INT_ARGB );
		Graphics g = bi2.getGraphics();
		if (source != null)
		{
			g.drawImage(
				source,
				0,
				0,
				width,
				height,
				0,
				0,
				source.getWidth( null ),
				source.getHeight( null ),
				null );
		}
		return bi2;

		// BufferedImage fastImage = makeFast( bi2 );
		// if(fastImage != bi2)
		// {
		// bi2.flush();
		// }
		// return fastImage;

	}

	/**
	 * Converts a bitmap to a fast version
	 *
	 * @param src
	 * @return
	 */
	public static BufferedImage makeFast( BufferedImage src )
	{
		BufferedImage fastImage = createFastImage(
			src.getWidth(),
			src.getHeight(),
			src.getTransparency() );
		Graphics g = fastImage.getGraphics();
		g.drawImage(
			src,
			0,
			0,
			null );
		g.dispose();
		return fastImage;
	}

	/**
	 * Converts a bitmap to a fast version
	 *
	 * @param src
	 * @return
	 */
	public static BufferedImage makeFast( Image src )
	{
		BufferedImage fastImage = createFastImage(
			src.getWidth( null ),
			src.getHeight( null ),
			Transparency.OPAQUE );
		Graphics g = fastImage.getGraphics();
		g.drawImage(
			src,
			0,
			0,
			null );
		g.dispose();
		return fastImage;
	}

	public static BufferedImage createRandom( Dimension d )
	{
		BufferedImage biNew = createFastImage(
			d.width,
			d.height );
		Graphics2D g2 = biNew.createGraphics();
		g2.setColor( Random.colorValue() );
		g2.draw( new Rectangle2D.Double( 0, 0, d.width, d.height ) );
		for (int its = 0; its < 5; ++its)
		{
			// Draw some random circles
			g2.setColor( Random.colorValue() );
			int x = Random.intValue( d.width );
			int y = Random.intValue( d.height );
			int w = Random.intValue( d.width - x );
			int h = Random.intValue( d.height - y );
			g2.fill( new Ellipse2D.Double( x, y, w, h ) );
		}
		return biNew;
	}

}
