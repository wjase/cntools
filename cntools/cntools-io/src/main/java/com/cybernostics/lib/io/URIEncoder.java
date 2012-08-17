package com.cybernostics.lib.io;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class URIEncoder
{

	public static final String badChars = "%";
	public static final String[] translated =
	{ "" };

	public static URI getURI( String uri )
	{

		try
		{
			return new URI( encodeChars( uri ) );
		}
		catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}
		return null;
	}

	private static final String filePrefix = "file:///";

	/**
	 * @param msgPath
	 *            a filesystem absolute path
	 * @return a URI with a file:// protocol specifier
	 */
	public static URI getFileURI( String filePath )
	{
		return getURI( filePrefix + filePath );
	}

	public static String encodeChars( String uri )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for (int index = 0; index < uri.length(); ++index)
		{
			char nextChar = uri.charAt( index );
			switch (nextChar)
			{
				case '\\':
					bos.write( '/' );
					break;
				case ' ':
				case '"':
				case '<':
				case '>':
				case '#':
				case '%':
				case '{':
				case '}':
				case '|':
				case '^':
				case '~':
				case '[':
				case ']':
				case '`':
					bos.write( '%' );
					try
					{
						bos.write( Integer.toHexString(
							nextChar )
							.getBytes() );
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						UnhandledExceptionManager.handleException( e );
					}
					break;
				default:
					bos.write( nextChar );
			}
		}
		return new String( bos.toByteArray() );
	}
}
