package com.cybernostics.lib.svg;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class ReferencedURLRewriter implements URLRewriter
{

	Pattern CssUrlPattern = Pattern.compile( "url\\(\\s*(.*)\\s*\\)" );
	Map< String, String > rewrites = new TreeMap< String, String >();

	/**
	 * resloader paths are left unchanged as they should be available to
	 * any up to date joeymail client.
	 * Also skips local references "#tag"
	 * 
	 * @param sToChange
	 * @return 
	 */
	@Override
	public String transformURL( String sToChange )
	{
		if (sToChange.startsWith( "#" ))
		{
			return sToChange;
		}

		// relative url refs need to be extracted from the current home
		// and written to the new stream
		if (sToChange.indexOf( ":" ) == -1)
		{
			rewrites.put(
				sToChange,
				sToChange );
			return sToChange;
		}
		String urlStr = sToChange;
		String finalFormat = "%s";

		try
		{
			// Assume resloader urls will be accessible from any reader of this image.
			// That is assuming you are always using your app (with all its resources available)
			// to open the SVG image.
			if (sToChange.indexOf( "resloader:" ) != -1)
			{
				return sToChange;
			}

			Matcher m = CssUrlPattern.matcher( sToChange );
			if (m.find())
			{
				urlStr = m.group( 1 );
				//finalFormat = "url(%s)";
			}
			URL parsed = new URL( urlStr );
			urlStr = parsed.getPath();
			// clean up any dossy looking slashes
			urlStr = urlStr.replaceAll(
				"\\\\",
				"/" );
			// drive letters - who needs 'em?
			urlStr = urlStr.replaceFirst(
				"/*[a-zA-Z]:/+",
				"" );
			// no root paths thanks
			urlStr = urlStr.replaceFirst(
				"^/",
				"" );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				ReferencedURLRewriter.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

		if (urlStr.startsWith( "#" ))
		{
			return sToChange;
		}

		String result = String.format(
			finalFormat,
			urlStr );
		rewrites.put(
			sToChange,
			result );

		return result;
	}

	@Override
	public Map< String, String > getRewrites()
	{
		return rewrites;
	}
}
