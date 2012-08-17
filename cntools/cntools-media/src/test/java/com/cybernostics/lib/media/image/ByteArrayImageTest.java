package com.cybernostics.lib.media.image;

import java.awt.image.BufferedImage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class ByteArrayImageTest
{

	public ByteArrayImageTest()
	{
	}

	/**
	 * Test of encodeImage method, of class ByteArrayImage.
	 */
	@Test
	public void testEncodeImage()
	{
		String text = "This is a simple text string which needs to be encoded into an image which has been written";

		BufferedImage bi = ByteArrayImage.encodeImage( text.getBytes() );

		String finalText = new String( ByteArrayImage.decode( bi ) );
		System.out.println( finalText );
		assertEquals(
			text,
			finalText );

	}

}
