package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.exceptions.AppExceptionManager;
import com.cybernostics.lib.io.JarUrlFix;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Map to create indirection lookup for resource location
 * An id can correspond to one or more resources
 * @author jasonw
 */
class ResourceCatalogue
{

	private final Map< String, String > ids = new HashMap< String, String >();

	private ResourceFinder finder = null;

	private static final String defaultPropertyFile = "text/idmap.txt";

	public URL getById( String id ) throws ResourceFinderException
	{
		return finder.getResource( ids.get( id ) );
	}

	public List< URL > getAllById( String id ) throws ResourceFinderException
	{
		return finder.getResources( ids.get( id ) );
	}

	public static ResourceCatalogue get( ResourceFinder finder )
	{
		return get(
			finder,
			defaultPropertyFile );
	}

	public static ResourceCatalogue get( ResourceFinder finder,
		String propertFile )
	{
		ResourceCatalogue res = new ResourceCatalogue();
		try
		{
			res.finder = finder;
			Properties prop = new Properties();
			prop.load( JarUrlFix.getURLStream( finder.getResource( propertFile ) ) );

			for (Entry< Object, Object > eachPair : prop.entrySet())
			{
				res.ids.put(
					eachPair.getKey()
						.toString(),
					eachPair.getValue()
						.toString() );

			}
			return res;
		}
		catch (Exception ex)
		{
			AppExceptionManager.handleException(
				ex,
				ResourceCatalogue.class );
		}
		return res;
	}

}