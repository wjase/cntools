package com.cybernostics.lib.io;

import com.cybernostics.lib.regex.Regex;

/**
 *
 * @author jasonw
 */
public class FileExtension
{

	private static final Regex extensionPattern = new Regex( "\\.([^\\.]+)$" );

	public static String get( String filename )
	{
		if (extensionPattern.find( filename ))
		{
			return extensionPattern.group( 1 );
		}
		return "";
	}

}
