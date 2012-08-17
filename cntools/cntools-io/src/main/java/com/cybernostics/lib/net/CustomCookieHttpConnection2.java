package com.cybernostics.lib.net;

import com.cybernostics.lib.collections.ArrayUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.stream.StreamPipe;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * When WebStart connects to the server it starts a different session to the one
 * in your browser. This is because the Java client can't access the local
 * cookie store from your browser (i., firefox, opera etc). This may not be
 * useful if you want to preserve the existing session from your browser (e.g.
 * which has verified that the user has logged on)
 * 
 * By Passing the cookie name/value pairs as environment properties with the
 * names prefixed with something like COOKIE_cookieName1 =
 * cookieValue1,COOKIE_cookieName2 = cookieValue2 you can then peel these from
 * System.properties and pass them to this class which makes requests using the
 * cookies you provide, rather than the ones saved when the WebStart client
 * connected to the host.
 * 
 * 
 * @author jasonw borrows heavily from ClientHttpRequest by Vlad Patryshev
 * 
 */
public class CustomCookieHttpConnection2 implements ConnectionSource
{

	private URLConnection theConnection = null;
	private URL location;

	public URL getLocation()
	{
		return location;
	}

	private Map< String, Object > postProperties = null;
	private Map< String, String > cookies = null;
	private PrintStream ps = null;
	private boolean needsClosingBoundary = false;
	private static boolean overrideCookies = true;
	boolean debugstream = false;

	public void setDebug()
	{
		this.debugstream = true;
	}

	private static Random random = new Random();

	protected static String randomString()
	{
		try
		{
			return URLEncoder.encode(
				Long.toString(
					random.nextLong(),
					36 ),
				"UTF-8" );
		}
		catch (UnsupportedEncodingException e)
		{
		}
		return Long.toString(
			random.nextLong(),
			36 );
	}

	final String BOUNDARY = randomString() + randomString() + randomString();

	private void writeBoundary()
	{
		needsClosingBoundary = true;
		ps.print( "--" );
		ps.print( BOUNDARY );
	}

	/**
	 * Create an underlying URL connection and set up the relevant properties.
	 * 
	 * @param location
	 * @param cookies
	 * @param postProperties
	 */
	public CustomCookieHttpConnection2(
		String location,
		Map< String, String > cookies,
		Map< String, Object > postProperties )
	{
		URL locationURL = null;
		try
		{

			if (cookies == null)
			{
				cookies = CookiePropertyParser.getCookies();
			}
			locationURL = new URL( location );
			this.postProperties = postProperties;
			this.cookies = cookies;
			this.location = locationURL;
		}
		catch (MalformedURLException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

	}

	/**
	 * Create an underlying URL connection and set up the relevant properties.
	 * 
	 * @param location
	 * @param cookies
	 * @param postProperties
	 */
	public CustomCookieHttpConnection2(
		URL location,
		Map< String, String > cookies,
		Map< String, Object > postProperties )
	{
		this.postProperties = postProperties;
		if (cookies == null)
		{
			cookies = CookiePropertyParser.getCookies();
		}
		this.cookies = cookies;
		this.location = location;

	}

	private void doPropertyPost()
	{
		if (overrideCookies)
		{
			CookieHandler.setDefault( null );
			setCookies( cookies );
		}

		theConnection.setRequestProperty(
			"Content-Type",
			"multipart/form-data; boundary=" + BOUNDARY );
	}

	private void postItem( String name, EncodedItem toPost ) throws IOException
	{
		String filename = toPost.getName();
		writeBoundary();
		ps.println();
		ps.println( String.format(
			"Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"",
			name,
			filename ) );
		ps.println( String.format(
			"Content-Type: %s",
			toPost.getType() ) );
		ps.println();

		byte[] buf = new byte[ 50000 ];
		int nread;
		//int total = 0;

		// pipe the contents of this stream to the URL output stream
		InputStream in = toPost.getDataStream();

		try
		{
			synchronized (in)
			{
				while (( nread = in.read(
					buf,
					0,
					buf.length ) ) >= 0)
				{
					ps.write(
						buf,
						0,
						nread );
					//total += nread;
				}
			}

			ps.flush();
			buf = null;

			ps.println();
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException ex)
			{
			}
		}

	}

	/**
	 * adds a string parameter to the request
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @throws IOException
	 */
	public void writeFormKeyValuePair( String name, String value )
	{
		writeBoundary();
		ps.println();
		ps.print( "Content-Disposition: form-data; name=\"" );
		try
		{
			ps.print( URLEncoder.encode(
				name,
				"UTF-8" ) );
		}
		catch (UnsupportedEncodingException e)
		{
			ps.print( name );
		}
		ps.println( '"' );
		ps.println();
		try
		{
			ps.println( URLEncoder.encode(
				value,
				"UTF-8" ) );
		}
		catch (UnsupportedEncodingException e)
		{
			ps.println( value );
		}
	}

	/**
	 * Encodes the cookie properties as a single string and sets this as a
	 * request property on the underlying HttpCOnnection.
	 * 
	 * @param cookies
	 */
	public void setCookies( Map< String, String > cookies )
	{
		// encode the cookies as
		// "name=value; name1=value1; name2=value2" etc
		StringBuilder sbCookies = new StringBuilder();
		if (overrideCookies)
		{
			if (cookies == null || cookies.size() == 0)
			{

				return; // nothing to do
			}
			String seperator = ""; // don't need a comma for the first item
			for (String key : cookies.keySet())
			{
				try
				{
					String prop = cookies.get( key );
					sbCookies.append( seperator );
					sbCookies.append( URLEncoder.encode(
						key,
						"UTF-8" ) );
					sbCookies.append( '=' );
					sbCookies.append( prop );
					// for more than 1 property separate with commas
					if (seperator.length() == 0)
					{
						seperator = "; ";
					}

				}
				catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else
		{
			try
			{
				CustomCookieManager.get()
					.get(
						location.toURI(),
						theConnection.getRequestProperties() );
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		theConnection.setRequestProperty(
			"Cookie",
			sbCookies.toString() );
	}

	/**
	 * @see CustomCookieHttpConnection.connect( int timeout )
	 * @return
	 * @throws IOException
	 */
	public URLConnection connect() throws IOException
	{
		return this.connect( 1000000 );
	}

	// Connects to the URL source using our own cookies rather than the
	// ones passed to WebStart.
	// This method temporarily clears the existing CookieManager so it doesn't
	// overwrite our cookies. We then set it back so as not to break any other
	// functionality.
	//
	public URLConnection connect( int timeout ) throws IOException
	{
		this.submit( timeout );
		// now restore the WebStart one so we don't upset the apple cart.
		// CookieHandler.setDefault(webstartCookieManager);

		return theConnection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.net.ConnectionSource#getConnection()
	 */
	@Override
	public URLConnection getConnection()
	{
		if (this.theConnection == null)
		{
			try
			{
				this.theConnection = this.location.openConnection();
				this.theConnection.setDoOutput( true );
				doPropertyPost();

				// Map< String, List< String >> headers =
				// theConnection.getHeaderFields();
				//				
				// for( String eachKey : headers.keySet() )
				// {
				// List<String> strings = headers.get( eachKey );
				//					
				// System.out.println( "Key:" + eachKey + "\n" );
				//					
				// for( String eachString : strings )
				// {
				// System.out.println( eachString+"\n" );
				// }
				// }

			}
			catch (IOException e)
			{
				UnhandledExceptionManager.handleException( e );
			}

		}
		return theConnection;
	}

	private InputStream isCurrent = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.net.ConnectionSource#submit()
	 */

	@Override
	public InputStream submit( int timeout )
	{
		if (isCurrent != null)
		{
			return isCurrent;
		}
		try
		{
			URLConnection conn = this.getConnection();
			conn.setReadTimeout( timeout );
			conn.connect();
			doStreamPost();
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		try
		{
			isCurrent = new BufferedInputStream( theConnection.getInputStream() );
			return isCurrent;
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		return null;
	}

	/**
	 * These are the post items which are physically written to the host as a
	 * stream rather than a simple property/value pair
	 */
	private void doStreamPost()
	{
		if (!theConnection.getDoOutput())
		{
			return; // nothing to do
		}

		try
		{
			if (!debugstream)
			{
				ps = new PrintStream( theConnection.getOutputStream() );
			}
			else
			{
				ps = System.out;
			}
		}
		catch (IOException e1)
		{
			UnhandledExceptionManager.handleException( e1 );
			return;
		}

		if (postProperties == null || postProperties.size() == 0)
		{
			return;
		}

		for (String paramName : postProperties.keySet())
		{
			Object value = postProperties.get( paramName );
			if (value != null && ( value instanceof EncodedItem ))
			{
				try
				{
					this.postItem(
						paramName,
						(EncodedItem) value );
				}
				catch (IOException e)
				{
					UnhandledExceptionManager.handleException( e );
				}
			}
			else
			{
				writeFormKeyValuePair(
					paramName,
					(String) value );
			}
		}

		if (needsClosingBoundary)
		{
			writeBoundary();
		}

	}

	/**
	 * @param overrideCookies
	 *            the overrideCookies to set
	 */
	public static void setOverrideCookies( boolean overrideCookies )
	{
		CustomCookieHttpConnection2.overrideCookies = overrideCookies;
	}

	/**
	 * @return the overrideCookies
	 */
	public static boolean isOverrideCookies()
	{
		return overrideCookies;
	}

	public String getReturnMimeType()
	{
		return getConnection().getHeaderField(
			"Content-type" );
	}

	public String showHeaders()
	{
		StringBuilder sb = new StringBuilder();
		URLConnection uc = getConnection();

		for (String eachHeader : uc.getHeaderFields()
			.keySet())
		{
			String headers = uc.getHeaderField( eachHeader );
			sb.append( String.format(
				"%s=%s\n",
				eachHeader,
				headers ) );

		}

		return sb.toString();
	}

	public InputStream expect( String mimePattern, int timeout )
		throws UnexpectedContentException
	{
		BufferedInputStream bis = (BufferedInputStream) submit( timeout );

		Pattern expected = Pattern.compile( mimePattern );
		String mimeType = getReturnMimeType();
		Matcher m = expected.matcher( mimeType );

		if (!m.find())
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			try
			{

				bos.write( showHeaders().getBytes() );
				bos.write( String.format(
					"ContentType:%s ",
					mimeType )
					.getBytes() );
				if (bis != null)
				{
					StreamPipe.copyInputToOutput(
						bis,
						bos );
				}

			}
			catch (IOException ex)
			{
				Logger.getLogger(
					CustomCookieHttpConnection2.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			throw new UnexpectedContentException( bos.toString() );

		}
		return bis;
	}
}
