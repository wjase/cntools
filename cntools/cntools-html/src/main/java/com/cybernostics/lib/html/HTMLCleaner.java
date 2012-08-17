package com.cybernostics.lib.html;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import org.w3c.tidy.Tidy;

public class HTMLCleaner
{

	private static HTMLCleaner singleton;

	public static HTMLCleaner get()
	{
		if (singleton == null)
		{
			singleton = new HTMLCleaner();
		}
		return singleton;
	}

	private HTMLCleaner()
	{
		setupTidy();
	}

	public InputStream getFilteredXMTMLStream( String uri )
		throws URISyntaxException, MalformedURLException,
		IOException
	{
		URL docuri = new URL( uri );

		URLConnection urlCon = docuri.openConnection();
		return getFilteredXMTMLStream( urlCon.getInputStream() );
	}

	public InputStream getFilteredXMTMLStream( byte[] bytes )
	{
		try
		{
			return getFilteredXMTMLStream( new ByteArrayInputStream( bytes ) );
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}

		return null;
	}

	public InputStream getFilteredXMTMLStream( InputStream is )
	{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// Stop tidy cluttering up STDERR with non-error messages

		tidy.setErrout( new PrintWriter( nullStream ) );

		tidy.parse(
			is,
			bos );
		// System.out.println( new String( bos.toByteArray() ) );
		ByteArrayInputStream bis = new ByteArrayInputStream( bos.toByteArray() );
		return bis;
	}

	Tidy tidy = new Tidy(); // obtain a new Tidy instance

	PrintStream nullStream;

	private void setupTidy()
	{
		// Ensure the html is well-formed using HTMLTidy
		tidy.setXHTML( true );
		tidy.setDocType( "strict" );
		tidy.setDropFontTags( true );
		tidy.setIndentContent( true );
		tidy.setSmartIndent( true );
		//tidy.getConfiguration().addProps( arg0 );

		tidy.setIndentAttributes( true );
		tidy.setMakeClean( false );
		tidy.setQuiet( true );
		tidy.setShowWarnings( true );
		tidy.setBreakBeforeBR( true );
		// tidy.setWord2000( true );

		OutputStream nullOS = new OutputStream()
		{

			@Override
			public void write( int b ) throws IOException
			{
				// write nothing
			}

		};
		nullStream = new PrintStream( nullOS );

	}

}
