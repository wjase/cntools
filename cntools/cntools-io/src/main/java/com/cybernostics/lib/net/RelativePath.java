package com.cybernostics.lib.net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author jasonw
 */
public class RelativePath
{

	// static methods only... move along move along
	private RelativePath()
	{
	}

	/**
	 * Does a simple relative path calculation - NOT RECURSIVE ie it will only
	 * find if the root is a substring of the child or the parent of the root is
	 * one.
	 *
	 * @param childString - child path to relativise
	 * @param rootString - root for relative path
	 * @return given: /var/test as root and /var/test/stinky.txt as the child,
	 * will return stinky.txt
	 */
	public static String get( String childString, String rootString )
	{

		if (childString.startsWith( rootString ))
		{
			childString = childString.substring( rootString.length() );
		}
		else
		{
			String parent = getFolder( rootString );
			if (childString.startsWith( parent ))
			{
				childString = childString.substring( parent.length() );
			}
		}

		if (childString.startsWith( "/" ))
		{
			return childString.substring( 1 );
		}

		return childString;

	}

	/**
	 *
	 * @see get(String, String) - this is the same except with URIs
	 */
	public static URI get( URI child, URI root ) throws URISyntaxException
	{
		URI rel = root.relativize( child );
		if (rel.equals( child ))
		{
			rel = relativise(
				child,
				root );
			if (rel.equals( child ))
			{
				URI newRoot = getFolder( root );
				rel = newRoot.relativize( child );
				if (rel.equals( child ))
				{
					rel = relativise(
						child,
						newRoot );
				}
			}
		}

		return rel;
	}

	public static URI relativise( URI child, URI root )
		throws URISyntaxException
	{
		if (child.equals( root ))
		{
			return child;
		}

		return new URI( get(
			child.toASCIIString(),
			root.toASCIIString() ) );

	}

	public static URI getFolder( URI aFile ) throws URISyntaxException
	{
		return new URI( getFolder( aFile.toASCIIString() ) );
	}

	/**
	 * get the parent folder of the path
	 *
	 * @param path - get the parent for this path
	 * @return
	 */
	public static String getFolder( String path )
	{
		int pos = path.lastIndexOf( '/' );
		return path.substring(
			0,
			pos );
	}

}
