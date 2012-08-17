package com.cybernostics.lib.persist;

import java.io.InputStream;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.cybernostics.lib.persist.xml.XMLPropertyMapEncoder;

/**
 * @author jasonw
 *
 */
public class XMLPropertyMapEncoderTest
{

	@Test
	public void testxmlSaveLoadAddresses()
	{
		InputStream isIn = XMLPropertyMapEncoderTest.class.getResourceAsStream( "text/addresses.xml" );

		Assert.assertNotNull( isIn );

		Map< String, String > props = XMLPropertyMapEncoder.getXMLProperties( isIn );

		for (String eachString : props.keySet())
		{
			System.out.printf(
				"%s = %s\n",
				eachString,
				props.get( eachString ) );
		}
	}

	@Test
	public void testxmlSaveLoadResponse()
	{
		InputStream isIn = XMLPropertyMapEncoderTest.class.getResourceAsStream( "text/loginresp.xml" );

		Assert.assertNotNull( isIn );

		Map< String, String > props = XMLPropertyMapEncoder.getXMLProperties( isIn );

		for (String eachString : props.keySet())
		{
			System.out.printf(
				"%s = %s\n",
				eachString,
				props.get( eachString ) );
		}
	}
}
