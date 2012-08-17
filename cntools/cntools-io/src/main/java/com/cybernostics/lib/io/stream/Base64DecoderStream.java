package com.cybernostics.lib.io.stream;

import java.io.*;

/**
 * Wrapper in case sun change their implementation
 * 
 * @author jasonw
 * 
 */
public class Base64DecoderStream extends Base64.InputStream
{

	public static void main( String[] args )
	{

		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Base64EncoderStream encode = new Base64EncoderStream( bos );
			PrintWriter pw = new PrintWriter( encode );
			pw
				.println( "Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode.Hello there this is an extremely long piece of data to encode" );
			pw.flush();
			// encode.flush();
			// bos.flush();

			String encoded = new String( bos.toByteArray() );
			System.out.printf(
				"Encoded: %s\n",
				encoded );
			ByteArrayInputStream bis = new ByteArrayInputStream( encoded.getBytes() );
			Base64DecoderStream bds = new Base64DecoderStream( bis );
			InputStreamReader isr = new InputStreamReader( bds );
			BufferedReader br = new BufferedReader( isr );
			System.out.printf(
				"Decoded: %s\n",
				br.readLine() );

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * @param in
	 */
	public Base64DecoderStream( InputStream in )
	{
		super( in );
	}

	public Base64DecoderStream( String in )
	{
		super( new ByteArrayInputStream( in.getBytes() ) );
	}
}
