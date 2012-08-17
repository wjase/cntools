package com.cybernostics.lib.net;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * This object is a File or in memory chunk of data to be 
 * posted as form data
 * @author jasonw
 *
 */
public class EncodedItem
{

	private InputStream is;
	private String name;
	private String mimetype;

	public EncodedItem( String fileName, InputStream data )
	{
		this.is = data;
		this.name = fileName;
		this.setType( URLConnection.guessContentTypeFromName( this.name ) );
	}

	public EncodedItem( File toSend ) throws MalformedURLException, IOException
	{
		this.is = toSend.toURI()
			.toURL()
			.openStream();
		this.name = toSend.getName();
		this.setType( URLConnection.guessContentTypeFromName( this.name ) );
	}

	public EncodedItem( URL toSend ) throws IOException
	{
		this.is = toSend.openStream();
		this.name = toSend.getFile();
		this.setType( URLConnection.guessContentTypeFromName( this.name ) );
	}

	public EncodedItem( String data, String filename )
	{
		this.is = new ByteArrayInputStream( data.getBytes() );
		this.name = filename;
		this.setType( URLConnection.guessContentTypeFromName( this.name ) );
	}

	/**
	 * @param toUploadAsName
	 * @param toUpLoad
	 */
	public EncodedItem( String toUploadAsName, File toUpLoad )
	{
		try
		{
			this.is = toUpLoad.toURI()
				.toURL()
				.openStream();
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		this.name = toUploadAsName;

	}

	public InputStream getDataStream()
	{
		return this.is;
	}

	public String getName()
	{
		return this.name;
	}

	public void setType( String mimeType )
	{
		this.mimetype = mimeType;
		if (this.mimetype == null)
		{
			this.mimetype = "application/octet-stream";
		}
	}

	public String getType()
	{
		return this.mimetype;
	}
}