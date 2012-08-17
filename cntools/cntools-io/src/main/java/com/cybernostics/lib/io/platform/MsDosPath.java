package com.cybernostics.lib.io.platform;

import com.cybernostics.lib.regex.Regex;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public class MsDosPath
{

	private static final Regex dosPathPattern = new Regex( "^.:" );

	private static final String dosFileUrl = "file:///%s";

	public static boolean isDosFileSpec( String toCheck )
	{
		return dosPathPattern.find( toCheck );
	}

	public static URL getDosFileSpec( String toCheck )
		throws MalformedURLException
	{
		if (isDosFileSpec( toCheck ))
		{
			return new URL( String.format(
				dosFileUrl,
				toCheck ) );
		}

		return null;

	}

}
