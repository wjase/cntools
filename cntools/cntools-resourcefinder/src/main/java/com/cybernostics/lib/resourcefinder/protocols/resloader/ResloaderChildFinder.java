package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.regex.Regex;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import com.cybernostics.lib.resourcefinder.net.customurl.CustomURLResourceChildFinder;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jasonw
 */
public class ResloaderChildFinder implements CustomURLResourceChildFinder
{

	@Override
	public List< URL > getResources( ResourceFinder defaultFinder,
		Regex pathToMatch,
		URL resourceRoot,
		ResourceFilter filt ) throws ResourceFinderException
	{
		try
		{
			URL realRoot = ResLoaderURLConnection.toLocalURL( resourceRoot );

			List< URL > underlyingResList = defaultFinder.getURLChildResources(
				pathToMatch,
				realRoot,
				filt );

			ResloaderURLBits parsed = ResloaderURLBits.create( resourceRoot );
			String pathPrefix = parsed.getPathComponent();

			if (!pathPrefix.endsWith( "/" ))
			{
				pathPrefix = pathPrefix + "/";
			}

			String rootPath = resourceRoot.getPath();
			if (!rootPath.endsWith( "/" ))
			{
				rootPath = rootPath + "/";
			}

			URI realRootURI = new URI( realRoot.toString() );
			String realRootStr = realRootURI.toASCIIString();
			int RelPathStart = realRootStr.length();
			ArrayList< URL > resLoadURLS = new ArrayList< URL >();
			for (URL eachURL : underlyingResList)
			{
				String childUrl = eachURL.toString();
				if (childUrl.length() >= RelPathStart)
				{
					String childBit = childUrl.substring( RelPathStart );
					childBit = childBit.replaceAll(
						"^/",
						"" );
					URI relURI = new URI( childBit );
					resLoadURLS.add( new URL( resourceRoot, rootPath
						+ relURI.getPath() ) );

				}
			}
			return resLoadURLS;
		}
		catch (Exception ex)
		{
			throw new ResourceFinderException( ex );
		}

	}

	@Override
	public List< URL > getResources( ResourceFinder aThis,
		String toMatch,
		URL resourceRoot,
		ResourceFilter filter )
		throws ResourceFinderException
	{
		return getResources(
			aThis,
			new Regex( toMatch ),
			resourceRoot,
			filter );
	}
}
