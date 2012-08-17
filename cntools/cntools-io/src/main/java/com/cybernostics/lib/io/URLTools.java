package com.cybernostics.lib.io;

import com.cybernostics.lib.regex.Regex;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class URLTools
{

	private static final Regex tailJarBit = new Regex( "!/(.+)$" );

	public static boolean existsLocally( URL toTest )
	{
		if (toTest == null)
		{
			return false;
		}

		if (toTest.getPath()
			.indexOf(
				"!" ) != -1)
		{
			try
			{
				JarURLConnection conn = (JarURLConnection) toTest.openConnection();
				tailJarBit.find( toTest.getPath() );
				String entryName = tailJarBit.group( 1 );
				if (entryName != null)
				{
					return conn.getJarFile()
						.getEntry(
							entryName ) != null;
				}
			}
			catch (IOException ex)
			{
				return false;
			}
		}
		else
		{
			File test = null;
			try
			{
				test = new File( toTest.toURI() );
				return test.exists();
			}
			catch (URISyntaxException ex)
			{
				Logger.getLogger(
					URLTools.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}

		return false;
	}

	public static URL getParent( URL child )
	{
		String parentPath = child.toString();
		if (parentPath.endsWith( "/" ))
		{
			parentPath = parentPath.substring(
				0,
				parentPath.length() - 1 );
		}
		int lastSlash = parentPath.lastIndexOf( '/' );
		if (lastSlash != -1)
		{
			try
			{
				return new URL( parentPath.substring(
					0,
					lastSlash + 1 ) );
			}
			catch (MalformedURLException ex)
			{
			}
		}
		else
		{
			try
			{
				return new URL( "/" );
			}
			catch (MalformedURLException ex)
			{
			}
		}
		throw new NullPointerException( "Returning null parent for "
			+ child.toString() );
	}

}