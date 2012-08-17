package com.cybernostics.lib.resourcefinder;

import java.net.URL;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class RegexURLFilter implements ResourceFilter
{

	Pattern pInclude = null;
	Pattern pExclude = null;

	/**
	 * Creates a filter which controls what URLS are returned from queries
	 * @param excluder - if this matches the url then reject it
	 * @param includer - if not rejected and this is provided then match it
	 * 
	 * @return if excluder provided and matches it will be rejected.
	 * If includer provided and matches return true, else return false;
	 * If no includer provider and rejecter doesn't match then return true.
	 */
	public static ResourceFilter get( String excluder, String includer )
	{
		return new RegexURLFilter( excluder, includer );
	}

	/**
	 * Creates a filter which controls what URLS are returned from queries
	 * @param includer - if not rejected and this is provided then match it
	 * 
	 * @return If includer provided and matches return true, else return false;
	 * If no includer provider and rejecter doesn't match then return true.
	 */
	public static ResourceFilter getIncluder( String includer )
	{
		return new RegexURLFilter( null, includer );
	}

	public static ResourceFilter getExcluder( String excluder )
	{
		return new RegexURLFilter( excluder, null );
	}

	private RegexURLFilter( String excluder, String includer )
	{
		if (includer != null)
		{
			pInclude = Pattern.compile( includer );
		}

		if (excluder != null)
		{
			pExclude = Pattern.compile( excluder );
		}
	}

	@Override
	public boolean include( URL test )
	{
		String toTest = test.getFile();
		// If exclude criteria and matches then return false
		if (pExclude != null)
		{
			if (pExclude.matcher(
				toTest )
				.find())
			{
				return false;
			}
		}
		// If include criteria return true if matches false otherwise
		if (pInclude != null)
		{
			return pInclude.matcher(
				toTest )
				.find();
		}
		// default to accept item
		return true;
	}
}
