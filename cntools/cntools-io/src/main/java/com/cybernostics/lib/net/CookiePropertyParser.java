package com.cybernostics.lib.net;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.cybernostics.lib.process.CommandArguments;

/**
 * Contains static methods for extracting cookie properties from the System property set.
 * The cookies need to be encoded using a specific prefix. The default is (COOKIE_)
 * 
 * So a cookie for the PHP session ID PHPSESSID=123456789 would be encoded as
 * COOKIE_PHPSESSID=123456789
 * 
 * @author jasonw
 * 
 */
public class CookiePropertyParser
{

	public static final String cookiePrefix = "COOKIE_";

	/**
	 * Get the map of cookies prefixed with the default prefix (COOKIE_)
	 * from the system properties.
	 * 
	 * @return a map of properties with the COOKIE_ bit stripped from the 
	 * property names.
	 */
	public static Map< String, String > getCookies()
	{
		return getCookies( cookiePrefix );
	}

	public static Map< String, String > getCookies( Map< String, String > requestResult )
	{
		return getCookies(
			requestResult,
			cookiePrefix );
	}

	public static void setCookies( Map< String, String > requestResult )
	{
		setCookies(
			requestResult,
			cookiePrefix );
	}

	public static void setCookies( Map< String, String > requestResult,
		String prefix )
	{
		Properties props = System.getProperties();

		for (String keyName : requestResult.keySet())
		{
			if (keyName.startsWith( prefix ))
			{
				// chop the prefix bits off
				String prop = requestResult.get( keyName );
				props.put(
					keyName,
					prop );
				//System.out.println( String.format( "%s = %s", keyName, prop ) );
			}
		}

		//
		List< String > args = CommandArguments.get();
		for (String eachArg : args)
		{
			if (eachArg.startsWith( prefix ))
			{
				int equalsPos = eachArg.indexOf( "=" );
				if (equalsPos > 0)
				{
					// eg COOKIE_PHPSESSID=98769876987698769786
					String name = eachArg.substring(
						prefix.length(),
						equalsPos );
					String value = eachArg.substring( equalsPos + 1 );
					//System.out.println( String.format( "Arg: %s = %s", name, value ) );
					props.put(
						name,
						value );
				}
			}
		}

	}

	public static Map< String, String > getCookies( Map< String, String > requestResult,
		String prefix )
	{
		Map< String, String > cookiePairs = new TreeMap< String, String >();

		int prefixLength = prefix.length();

		for (String keyName : requestResult.keySet())
		{
			if (keyName.startsWith( prefix ))
			{
				// chop the prefix bits off
				String originalName = keyName.substring( prefixLength );
				String prop = requestResult.get( keyName );
				cookiePairs.put(
					originalName,
					prop );
				System.out.println( String.format(
					"%s = %s",
					originalName,
					prop ) );
			}
		}

		return cookiePairs;

	}

	/**
	 * Get the map of cookies prefixed with the specified prefix 
	 * from the system properties.
	 * 
	 * @param prefix - string to mark a cookie property. (Use this for multiple
	 * sessions eg SESSION1_ SESSION2_ etc.
	 * 
	 * @return a map of properties with the COOKIE_ bit stripped from the 
	 * property names.
	 */
	public static Map< String, String > getCookies( String prefix )
	{
		Map< String, String > cookiePairs = new TreeMap< String, String >();

		Properties props = System.getProperties();

		int prefixLength = prefix.length();

		for (Object key : props.keySet())
		{
			String keyName = key.toString();
			//System.out.println( "Checking:" + keyName );

			if (keyName.startsWith( prefix ))
			{
				// chop the prefix bits off
				String originalName = keyName.substring( prefixLength );
				String prop = props.getProperty( keyName );
				cookiePairs.put(
					originalName,
					prop );
				System.out.println( keyName + "=" + prop );
			}
		}

		return cookiePairs;
	}
}
