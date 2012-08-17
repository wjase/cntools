/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.io.stream;

import java.io.*;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class Base64DecoderStreamTest
{

	public Base64DecoderStreamTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	/**
	 * Test of main method, of class Base64DecoderStream.
	 */
	@Test
	public void testMain() throws IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Base64EncoderStream encode = new Base64EncoderStream( bos );
		String original = "Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode";

		PrintWriter pw = new PrintWriter( encode );
		pw.println( original );
		pw.flush();

		String encoded = new String( bos.toByteArray() );
		System.out.printf(
			"Encoded: %s\n",
			encoded );
		ByteArrayInputStream bis = new ByteArrayInputStream( encoded.getBytes() );
		Base64DecoderStream bds = new Base64DecoderStream( bis );
		InputStreamReader isr = new InputStreamReader( bds );
		BufferedReader br = new BufferedReader( isr );
		String decoded = br.readLine();
		System.out.printf(
			"Decoded: %s\n",
			decoded );
		Assert.assertEquals(
			original,
			decoded );
	}

}
