package com.cybernostics.lib.net;

import com.cybernostics.lib.collections.ArrayUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to create a fake URL source to which you can post request
 * properties and get content back after a programmed delay.
 * 
 * @author jasonw
 */
public class TestURLConnection extends URLConnection
{

	private URL theURL = null;

	public URL getTheURL()
	{
		return theURL;
	}

	public void setTheURL( URL theURL )
	{
		this.theURL = theURL;
	}

	private Map< String, List< String >> request = new HashMap< String, List< String >>();
	private long streamdelay = 1000;

	public long getStreamDelay()
	{
		return streamdelay;
	}

	public void setStreamDelay( long streamdelay )
	{
		this.streamdelay = streamdelay;
	}

	public TestURLConnection()
	{
		super( null );
		theURL = url;
	}

	private String contentText = "";

	public void setContentText( String content )
	{
		this.contentText = content;
	}

	public String getContentText()
	{
		return contentText;
	}

	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public String getInputString()
	{
		return bos.toString();
	}

	@Override
	public void connect() throws IOException
	{
	}

	@Override
	public boolean getDoOutput()
	{
		return true;
	}

	@Override
	public void addRequestProperty( String key, String value )
	{
		if (request.containsKey( key ))
		{
			request.get(
				key )
				.add(
					value );
		}
		else
		{
			ArrayList< String > props = new ArrayList< String >();
			props.add( value );
			request.put(
				key,
				props );
		}

	}

	@Override
	public boolean getDoInput()
	{
		return true;
	}

	@Override
	public String getHeaderField( String name )
	{
		return super.getHeaderField( name );
	}

	@Override
	public Map< String, List< String >> getHeaderFields()
	{
		return super.getHeaderFields();
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream( contentText.getBytes() );
		try
		{
			Thread.sleep( streamdelay );
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				TestURLConnection.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return bis;

	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		return bos;
	}

	@Override
	public Map< String, List< String >> getRequestProperties()
	{
		return request;
	}

	@Override
	public String getRequestProperty( String key )
	{
		return ArrayUtils.implode(
			",",
			request.get( key ) );
	}

	@Override
	public URL getURL()
	{
		return super.getURL();
	}

	@Override
	public void setRequestProperty( String key, String value )
	{
		addRequestProperty(
			key,
			value );
	}
}
