package com.cybernostics.lib.media.image;

import java.awt.image.BufferedImage;

/*
 * The Byte Array Image is used to transport data as an Image file. It is not intended as a
 * substitute for encryption, but merely to facilitate binary data transfer through channels which
 * easily accomodate images, like MIME mail messages.
 */
/**
 *
 * @author jasonw
 */
public class ByteArrayImage
{

	/**
	 * Takes a stream of bytes and stuffs it into an image which can then
	 * be recovered with decodeImage.
	 * 
	 * @param toEncode
	 * @return 
	 */
	public static BufferedImage encodeImage( byte[] toEncode )
	{
		int lengthBytes = toEncode.length;
		int dimension = (int) Math.round( Math.sqrt( lengthBytes / 4 ) ) + 1;
		int numPixels = dimension * dimension;
		int[] fPixels = new int[ numPixels ];
		fPixels[ 0 ] = lengthBytes;

		int byteIndex = 0;
		int sampleIndex = 1;
		final int[] shiftAmount =
		{ 24, 16, 8, 0 };

		int currentMask = 0;
		int currentSample = 0;
		while (byteIndex < lengthBytes)
		{
			currentSample |= ( ( toEncode[ byteIndex++ ] << shiftAmount[ currentMask++ ] ) );

			if (currentMask == 4)
			{
				fPixels[ sampleIndex++ ] = currentSample;
				currentSample = 0;
				currentMask = 0;
			}
		}
		// if we have a final int to pack
		if (currentMask != 0)
		{
			fPixels[ sampleIndex++ ] = currentSample;
		}

		return createImageFromIntArray(
			dimension,
			dimension,
			fPixels );

	}

	// 
	protected static BufferedImage createImageFromIntArray( int width,
		int height,
		int[] data )
	{
		BufferedImage image = new BufferedImage( width,
			height,
			BufferedImage.TYPE_INT_ARGB );
		image.setRGB(
			0,
			0,
			width,
			height,
			data,
			0,
			width );

		//raster.setPixels( 0, 0, width, height, data );
		return image;

	}

	/**
	 * Takes a buffered image and recovers the byte stream which had been 
	 * encoded  within it.
	 * @param width
	 * @param height
	 * @param data
	 * @return 
	 */
	public static byte[] decode( BufferedImage bi )
	{
		int width = bi.getWidth();
		int height = bi.getHeight();

		int numPixels = bi.getHeight() * bi.getWidth();

		int[] fPixels = new int[ numPixels ];
		bi.getRGB(
			0,
			0,
			width,
			height,
			fPixels,
			0,
			width );

		int decodedLength = fPixels[ 0 ];
		byte[] pixels = new byte[ decodedLength ];

		int byteIndex = 0;
		int sampleIndex = 1;
		final int[] shiftAmount =
		{ 24, 16, 8, 0 };
		int currentMask = 0;
		int currentSample = fPixels[ sampleIndex ];

		while (byteIndex < decodedLength)
		{
			// grab the byte from the current shift pos in the integer sample
			pixels[ byteIndex++ ] = (byte) ( ( currentSample >> shiftAmount[ currentMask++ ] ) & 0xff );

			if (currentMask == 4)
			{
				currentSample = fPixels[ ++sampleIndex ];
				currentMask = 0;

			}
		}

		return pixels;
	}
}
