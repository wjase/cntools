
package com.cybernostics.lib;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.URL;

/**
 * @author jasonw
 *
 */
public class ResourcesRoot
{

	public static Finder getFinder()
	{
		return finder.get();
	}

	private static SingletonInstance<Finder> finder = new SingletonInstance<Finder>()
	{

		@Override
		protected Finder createInstance()
		{
			return ResourceFinder.get( ResourcesRoot.class );
		}
	};

	public static URL getResource( String path )
	{
		return finder.get().getResource( path );
	}
}
