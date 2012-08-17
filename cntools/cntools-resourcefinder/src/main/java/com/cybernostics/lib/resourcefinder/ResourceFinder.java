package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.io.URLTools;
import com.cybernostics.lib.io.file.RecursiveURLProcessor;
import com.cybernostics.lib.io.file.URLVisitor;
import com.cybernostics.lib.net.RelativePath;
import com.cybernostics.lib.regex.Regex;
import com.cybernostics.lib.resourcefinder.net.customurl.CustomURLResourceChildFinder;
import com.cybernostics.lib.resourcefinder.net.customurl.CustomURLScheme;
import com.cybernostics.lib.resourcefinder.protocols.resloader.ResloaderURLScheme;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jasonw
 *
 */
public class ResourceFinder implements Finder
{

	@Override
	public String getId()
	{
		return loaderClass.getCanonicalName();
	}

	public static void registerResloader()
	{
		ResloaderURLScheme.register();
	}

	// register the resourceLoader custom URL protocol handler
	static
	{
		registerResloader();
	}

	public static Finder get( Class< ? > instanceRoot )
	{
		ResourceFinder loader = new ResourceFinder( instanceRoot );
		return loader;
	}

	public static Finder get( Object instanceRoot )
	{
		return get( instanceRoot.getClass() );
	}

	public static Finder getLoaderFromName( String instanceRoot )
		throws ClassNotFoundException
	{
		return get( Class.forName( instanceRoot ) );
	}

	Class< ? > loaderClass = null;

	URL resourceRootURL = null;

	protected Map< String, URL > loaderRoots = new TreeMap< String, URL >();

	protected ResourceFinder( Class< ? > instanceRoot )
	{
		loaderClass = instanceRoot;

		String pathName = loaderClass.getCanonicalName();
		pathName = pathName.replaceAll(
			"\\.",
			"/" ) + ".class";

		resourceRootURL = Thread.currentThread()
			.getContextClassLoader()
			.getResource(
				pathName );
		try
		{
			resourceRootURL = new URL( RelativePath.getFolder(
				resourceRootURL.toURI() )
				.toString() + "/" );
		}
		catch (Exception ex)
		{
			throw new RuntimeException( ex );
		}

	}

	/**
	 * Extracts the loader root path from the path specified, giving a relative
	 * path.
	 *
	 * The root url with either be a file:// url or a file:jar:// url depending
	 * on where the program is run from.
	 *
	 * @param path
	 * @return
	 */
	public String getRelativePath( URL path )
	{
		String fullPath = path.getPath();

		// find a root which matches the path parameter, then chop off the root part
		for (URL eachRoot : loaderRoots.values())
		{
			String eachRootPath = eachRoot.getPath();
			if (fullPath.toString()
				.indexOf(
					eachRootPath ) == 0)
			{
				return fullPath.substring( eachRootPath.length() );
			}
		}
		return "";
	}

	/**
	 * Reads all the text from the specified stream into a string
	 *
	 * @param is - the stream containing the text to read
	 * @return a string containing all text read.
	 */
	public static String readAllFrom( InputStream is ) throws IOException
	{
		InputStreamReader myReader = new InputStreamReader( is );
		BufferedReader readBuff = new BufferedReader( myReader );
		StringBuilder textFromFile = new StringBuilder();

		while (readBuff.ready())
		{
			textFromFile.append( readBuff.readLine() );
			textFromFile.append( "\r" );
		}
		is.close();

		return textFromFile.toString();

	}

	@Override
	public URL getResource( String path ) throws ResourceFinderException
	{
		if (path.isEmpty())
		{
			return getRoot();
		}

		try
		{
			URL candidate = new URL( getRoot(), path );
			if (URLTools.existsLocally( candidate ))
			{
				return candidate;
			}
		}
		catch (MalformedURLException ex)
		{
			throw new ResourceFinderException( ex );
		}

		return null;

	}

	public static String getPackagePath( Class< Object > classObj )
	{
		return classObj.getPackage()
			.getName()
			.replaceAll(
				"\\.",
				"/" ) + "/";
	}

	private static Pattern dosPath = Pattern.compile( "^.:" );

	public static URL checkRawDosFileSpec( String toCheck )
	{
		try
		{
			Matcher m = dosPath.matcher( toCheck );
			if (m.find())
			{
				return new URL( "file:///" + toCheck );
			}

			URI tryAbs;
			tryAbs = new URI( toCheck );
			if (tryAbs.isAbsolute())
			{
				return tryAbs.toURL();
			}
		}
		catch (URISyntaxException ex)
		{
			Logger.getLogger(
				ResourceFinder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				ResourceFinder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

		return null;
	}

	@Override
	public List< URL > getResources( String pathToMatch )
		throws ResourceFinderException
	{
		return getResources(
			pathToMatch,
			(ResourceFilter) null );
	}

	/**
	 * @param pathToMatch = wildcard match to find resources either in
	 * filesystem or jar
	 * @return The list of URLs for the matching resources
	 */
	@Override
	public List< URL > getResources( String pathToMatch, ResourceFilter filter )
		throws ResourceFinderException
	{
		// first try absolute
		List< URL > resources = null;

		URL tryAbs = checkRawDosFileSpec( pathToMatch );
		if (tryAbs != null)
		{
			resources = getURLChildResources(
				"",
				tryAbs,
				filter );
		}

		if (resources == null)
		{
			URL rootURL = getResource( pathToMatch );

			if (rootURL == null)
			{
				rootURL = getResource( "" );

			}

			resources = getURLChildResources(
				pathToMatch,
				rootURL,
				filter );

			if (( resources != null ) && ( resources.size() > 0 ))
			{
				return resources;
			}
		}

		return resources;
	}

	public static List< URL > getChildren( URL resourceRoot,
		final ResourceFilter filter ) throws ResourceFinderException
	{
		final List< URL > theList = new ArrayList< URL >();
		try
		{
			RecursiveURLProcessor.process(
				resourceRoot,
				new URLVisitor()
			{

				@Override
				public void visit( URL eachURL )
				{
					if (filter == null || filter.include( eachURL ))
					{
						theList.add( eachURL );
					}
				}

			} );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				ResourceFinder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return theList;

	}

	public List< URL > getURLChildResources( String pathToMatch,
		URL resourceRoot,
		ResourceFilter filter )
			throws ResourceFinderException
	{
		if (pathToMatch.indexOf( '\\' ) != -1)
		{
			Pattern pSlash = Pattern.compile( "\\\\" );
			Matcher mSlash = pSlash.matcher( pathToMatch );
			pathToMatch = mSlash.replaceAll( "/" );
		}

		Regex pPathMatch = null;
		try
		{
			String patternMatch = pathToMatch;
			if (patternMatch.startsWith( "/" ) && ( patternMatch.length() > 1 ))
			{
				patternMatch = "^" + patternMatch.substring( 1 );
			}

			if (patternMatch.endsWith( "/" ) || patternMatch.length() == 0)
			{
				patternMatch = patternMatch + ".";
			}

			pPathMatch = new Regex( patternMatch );
		}
		catch (Exception e)
		{
			throw new ResourceFinderException( e );

		}

		return getURLChildResources(
			pPathMatch,
			resourceRoot,
			filter );

	}

	public List< URL > getURLChildResources( final Regex toMatch,
		URL resourceRoot,
		final ResourceFilter filter )
			throws ResourceFinderException
	{

		// deal with resloader roots specially
		// 1. get the real file or jar ref
		// 2. get the resources from that
		// 3. stitch together resloader urls for each one found
		// 4. return the list

		String protocolName = resourceRoot.getProtocol();
		if (CustomURLScheme.isRegistered( protocolName ))
		{
			CustomURLResourceChildFinder finder = CustomURLScheme.getFinder( protocolName );
			if (finder != null)
			{
				return finder.getResources(
					this,
					toMatch,
					resourceRoot,
					filter );
			}
			else
			{
				return new ArrayList< URL >();
			}
		}

		final List< URL > theList = new ArrayList< URL >();
		try
		{
			RecursiveURLProcessor.process(
				resourceRoot,
				new URLVisitor()
			{

				@Override
				public void visit( URL eachURL )
				{
					if (toMatch.find( eachURL.toString() )
						&& ( filter == null || filter.include( eachURL ) ))
					{
						theList.add( eachURL );
					}
				}

			} );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				ResourceFinder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return theList;

	}

	// Will return the first loader's root URL which should be the primary one.
	public URL getRoot() throws ResourceFinderException
	{
		return resourceRootURL;
	}

}
