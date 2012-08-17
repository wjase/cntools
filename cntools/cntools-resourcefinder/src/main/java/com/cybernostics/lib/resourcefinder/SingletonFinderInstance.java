package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.net.URL;

/**
 * A one per instance or one per class Finder instance.
 * 
 * Usage:
 * Declare an instance of this class for one per instance - or make it static
 * for once per class.
 * 
 * @author jasonw
 */
public class SingletonFinderInstance extends SingletonInstance< Finder >
{

	Class finderRoot;

	public SingletonFinderInstance( Class root )
	{
		this.finderRoot = root;
	}

	@Override
	protected Finder createInstance()
	{
		return ResourceFinder.get( finderRoot );
	}

	public URL getResource( String toFind ) throws ResourceFinderException
	{
		return get().getResource(
			toFind );
	}
}
