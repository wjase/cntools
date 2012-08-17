package com.cybernostics.lib.gui.shape;

import java.awt.Image;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 * Convenience methods for creating rectangles from images and icons
 *
 * @author jasonw
 */
public class ImageRect
{

	/**
	 * Creates a rectangle from the image. NOTE: this does not pass an
	 * ImageObserver to the getHeight and getWidth calls - so make sure your
	 * image is fully formed.
	 *
	 * @param i
	 * @return
	 */
	public static Rectangle2D get( Image i )
	{
		return new Rectangle2D.Double( 0,
			0,
			i.getWidth( null ),
			i.getHeight( null ) );
	}

	public static Rectangle2D get( Icon i )
	{
		return new Rectangle2D.Double( 0,
			0,
			i.getIconWidth(),
			i.getIconHeight() );
	}

}
