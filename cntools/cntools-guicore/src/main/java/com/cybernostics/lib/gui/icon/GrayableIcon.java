package com.cybernostics.lib.gui.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.Icon;

import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.image.BitmapMaker;

/**
 * Creates a grey version of a colour icon
 * @author jasonw
 * 
 */
public class GrayableIcon implements Icon
{

	BufferedImage renderedIcon = null;

	Icon srcIcon = null;

	public GrayableIcon( Icon src )
	{
		srcIcon = src;
	}

	@Override
	public int getIconHeight()
	{
		return srcIcon.getIconHeight();
	}

	@Override
	public int getIconWidth()
	{
		return srcIcon.getIconWidth();
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		initialiseBitmap( c );
		if (renderedIcon != null)
		{
			g.drawImage(
				renderedIcon,
				x,
				y,
				null );
		}
		else
		{
			srcIcon.paintIcon(
				c,
				g,
				x,
				y );
		}
	}

	private void initialiseBitmap( Component c )
	{
		// re-render the icon if required
		if (renderedIcon == null
			|| ( renderedIcon.getWidth() != getIconWidth() )
			|| ( renderedIcon.getHeight() != getIconHeight() ))
		{
			if (getIconWidth() == 0 || getIconHeight() == 0)
			{
				return;
			}
			renderedIcon = BitmapMaker.createFastImage(
				getIconWidth(),
				getIconHeight(),
				Transparency.TRANSLUCENT );//new BufferedImage( getIconWidth(), getIconHeight(), BufferedImage.TYPE_BYTE_GRAY );
			Graphics g = renderedIcon.getGraphics();
			srcIcon.paintIcon(
				c,
				g,
				0,
				0 );
			g.dispose();

			WritableRaster raster = renderedIcon.getRaster();
			double[] array = new double[ raster.getNumBands() ];

			for (int x = 0; x < renderedIcon.getWidth(); ++x)
			{
				for (int y = 0; y < renderedIcon.getHeight(); ++y)
				{
					raster.getPixel(
						x,
						y,
						array );
					double total = 0;
					for (int i = 0; i < array.length; ++i)
					{
						total += array[ i ];
					}
					total /= array.length;
					for (int i = 0; i < array.length; ++i)
					{
						array[ i ] = total;
					}
					raster.setPixel(
						x,
						y,
						array );
				}
			}

		}
	}

}
