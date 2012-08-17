
package com.cybernostics.examples.lib;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jasonw
 *
 */
public class ExampleResources
{

    private static SingletonInstance<Finder> finder = new SingletonInstance<Finder>()
    {

        @Override
        protected Finder createInstance()
        {
            return ResourceFinder.get( ExampleResources.class );
        }
    };

    public static URL getResource( String toLoad )
    {
        try
        {
            return finder.get().getResource( toLoad );
        }
        catch ( ResourceFinderException ex )
        {
            Logger.getLogger( ExampleResources.class.getName() ).log( Level.SEVERE, null, ex );
        }

        return null;
    }

    public static Finder getFinder()
    {
        return finder.get();
    }
}
