package com.cybernostics.lib.io.platform;

/**
 *
 * @author jasonw
 */
public class Slash
{

	private static String c = null;

	/**
	 * Get a platform-independent separator char for file paths
	 * Assumes it is a single char
	 * @return 
	 */
	public static String chr()
	{
		if (c == null)
		{
			String separator = System.getProperty( "file.separator" );
			c = separator;
		}
		return c;
	}
}
