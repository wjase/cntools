package com.cybernostics.lib.resourcefinder.net.customurl;

import com.cybernostics.lib.regex.Regex;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author jasonw
 */
public interface CustomURLResourceChildFinder
{
	/**
	 * Returns a list of resources matching the pathToMatch regex which are located
	 * under the resourcesRoot URL
	 * @param defaultFinder - Finder to use for loading/ finding regular resources
	 * @param pathToMatch
	 * @param resourceRoot
	 * @return 
	 */
	public List< URL > getResources( ResourceFinder defaultFinder,
		String pathToMatch,
		URL resourceRoot,
		ResourceFilter filt ) throws ResourceFinderException;

	public List< URL > getResources( ResourceFinder defaultFinder,
		Regex pathToMatch,
		URL resourceRoot,
		ResourceFilter filt ) throws ResourceFinderException;

}
