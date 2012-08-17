package com.cybernostics.lib.resourcefinder;

import java.net.URL;
import java.util.Collection;

/**
 * Finder attempts to remove some of the ambiguity surrounding loading resources.
 * Each finder corresponds to a "marker" class which sits in a jar above a bundle
 * of related resources.
 * 
 * @author jasonw
 */
public interface Finder
{

	// get the unique identifier for a given finder
	public String getId();

	public URL getResource( String path ) throws ResourceFinderException;

	public Collection< URL > getResources( String pathToMatch )
		throws ResourceFinderException;

	/**
	 * @param pathToMatch
	 * = wildcard match to find resources either in filesystem or jar
	 * @param InstanceRoot
	 * = class which is located immediately above the desired resources
	 * @return The list of URLs for the matching resources
	 */
	public Collection< URL > getResources( String pathToMatch,
		ResourceFilter filter ) throws ResourceFinderException;

}
