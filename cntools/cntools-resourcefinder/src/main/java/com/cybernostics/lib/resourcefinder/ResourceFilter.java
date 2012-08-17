package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.collections.CollectionFilter;
import java.net.URL;

/**
 * Classes implementing this interface can be used to include or exclude URLs
 * from a returned set.
 * 
 * @author jasonw
 */
public interface ResourceFilter extends CollectionFilter< URL >
{

	@Override
	boolean include( URL test );
}
