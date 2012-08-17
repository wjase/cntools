package com.cybernostics.lib.media;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

public class TransparentImageWriter
{

	public static void saveImageToStream( BufferedImage bi, OutputStream os )
	{
		try
		{
			ImageIO.write(
				bi,
				"png",
				os );
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
	}

	public static BufferedImage convertRGBAToIndexed( BufferedImage src )
	{
		BufferedImage dest = new BufferedImage( src.getWidth(),
			src.getHeight(),
			BufferedImage.TYPE_INT_ARGB_PRE );
		dest = makeTransparent(
			dest,
			0,
			0 );
		dest.createGraphics()
			.drawImage(
				src,
				0,
				0,
				null );
		return dest;
	}

	public static BufferedImage makeTransparent( BufferedImage image,
		int x,
		int y )
	{
		ColorModel cm = image.getColorModel();
		if (!( cm instanceof IndexColorModel ))
		{
			return image; // sorry...
		}
		IndexColorModel icm = (IndexColorModel) cm;
		WritableRaster raster = image.getRaster();
		int pixel = raster.getSample(
			x,
			y,
			0 ); // pixel is offset in ICM's
		// palette
		int size = icm.getMapSize();
		byte[] reds = new byte[ size ];
		byte[] greens = new byte[ size ];
		byte[] blues = new byte[ size ];
		icm.getReds( reds );
		icm.getGreens( greens );
		icm.getBlues( blues );
		IndexColorModel icm2 = new IndexColorModel( 8,
			size,
			reds,
			greens,
			blues,
			pixel );
		return new BufferedImage( icm2,
			raster,
			image.isAlphaPremultiplied(),
			null );
	}
}
