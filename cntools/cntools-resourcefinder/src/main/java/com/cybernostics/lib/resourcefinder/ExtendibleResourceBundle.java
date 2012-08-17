package com.cybernostics.lib.resourcefinder;

import com.cybernostics.lib.collections.ReverseIterableList;
import com.cybernostics.lib.io.URLTools;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.net.URL;
import java.util.Map.Entry;
import java.util.*;

/**
 * Allows you to add resources to a downloadable app without having to reload
 * existing resources
 *
 * Usage: In a separate jar, create a BlahManager with a static
 * SingletonInstance of this class;
 *
 * Add static methods which delegate to the static instance: void URL get(
 * String resName ); when the
 *
 * @author jasonw
 */
abstract public class ExtendibleResourceBundle implements Finder
{

	@Override
	public URL getResource( String path ) throws ResourceFinderException
	{
		return get( path );
	}

	private static final SingletonInstance< ResourceFilter > matchAll = new SingletonInstance< ResourceFilter >()
	{

		@Override
		protected ResourceFilter createInstance()
		{
			return new ResourceFilter()
			{

				@Override
				public boolean include( URL test )
				{
					return true;
				}

			};
		}

	};

	@Override
	public Collection< URL > getResources( String pathToMatch )
		throws ResourceFinderException
	{
		return getResources(
			pathToMatch,
			matchAll.get() );
	}

	@Override
	public Collection< URL > getResources( String pathToMatch,
		ResourceFilter filter ) throws ResourceFinderException
	{
		ArrayList< URL > collection = new ArrayList< URL >();
		for (Entry< String, URL > eachEntry : combinedResources.get()
			.entrySet())
		{
			String urlString = eachEntry.getKey()
				.toString();
			// Don't return the parent folder in results
			if (urlString.startsWith( pathToMatch )
				&& ( !urlString.equals( pathToMatch ) )
				&& filter.include( eachEntry.
					getValue() ))
			{
				collection.add( eachEntry.getValue() );
			}
		}

		return collection;
	}

	/**
	 *
	 */
	public List< Finder > loaderList = new ArrayList< Finder >();

	/**
	 * Update this method every time you add a resource bundle to the list of
	 * dependencies. Add your new bundle to the TOP of the list. It must be
	 * newest first. This is important because it allows you to override
	 * resources without having to download an existing bundle. Instead, define
	 * a new bundle containing the updated resource eg "image/myimage.png" with
	 * the same path as in the existing bundle. Then calls to
	 * getResource("image/myimage.png") will find the new resource.
	 *
	 * @return
	 */
	public List< Finder > getLoaderList()
	{
		return loaderList;
	}

	public void addFinder( Finder toAdd )
	{
		loaderList.add( toAdd );
	}

	//    {
	//        if ( loaderList == null )
	//        {
	//            loaderList = new ArrayList<ResourceFinder>();
	//            loaderList.add( ResourceFinder.get( com.cybernostics.game.resources.spotdiff1.SpotDiff1.class ) );
	//            loaderList.add( ResourceFinder.get( GameResources.class ) );
	//   //         loaderList.add( ResourceFinder.getLoader( com.cybernostics.game.resources.testmod1.TestMod1.class ) );
	//        }
	//
	//        return loaderList;
	//
	//    }
	//    /**
	//     * Quick test method for resource testing 
	//     * @param args
	//     */
	//    public static void main( String[] args )
	//    {
	//        try
	//        {
	//            System.out.println( String.format("get result: %s" , GameResources.get( "text/test.txt" ))  );
	//            Collection<URL> items = GameResources.getAll( "text/" );
	//
	//            for ( URL eachItem : items )
	//            {
	//                System.out.println( eachItem.toString() );
	//            }
	//        }
	//        catch ( ResourceFinderException ex )
	//        {
	//            Logger.getLogger( GameResources.class.getName() ).log( Level.SEVERE, null, ex );
	//        }
	//    }
	/**
	 * Gets a single resource from the specified URL. It returns the first one
	 * found. So paths must be unique unless you want to hide older resources.
	 *
	 * @param sUrl
	 * @return
	 */
	public URL get( String sUrl ) throws ResourceFinderException
	{

		List< Finder > finders = getLoaderList();
		URL result;
		for (Finder eachLoader : finders)
		{
			try
			{
				result = eachLoader.getResource( sUrl );
				if (result != null)
				{
					return result;
				}

			}
			catch (Throwable t)
			{
				throw new RuntimeException( t );
			}
		}
		return null;
	}

	public static URL getCommonRoot( Collection< URL > children )
	{
		if (children.isEmpty())
		{
			return null;
		}

		URL tryParent = null;
		for (URL eachOne : children)
		{
			if (tryParent == null)
			{
				tryParent = URLTools.getParent( eachOne );
			}
		}

		return tryParent;
	}

	protected SingletonInstance< Map< String, URL >> combinedResources = new SingletonInstance< Map< String, URL >>()
	{

		@Override
		protected Map< String, URL > createInstance()
		{
			HashMap< String, URL > combinedResources = new HashMap< String, URL >();

			// go oldest first so they get overwritten
			for (Finder eachLoader : ReverseIterableList.get( getLoaderList() ))
			{
				if (eachLoader instanceof ExtendibleResourceBundle)
				{
					combinedResources.putAll( ( (ExtendibleResourceBundle) eachLoader ).combinedResources.get() );
					continue;
				}

				Collection< URL > results = eachLoader.getResources( "" );

				if (results != null)
				{
					// first find the common root folder for all the URLs in this loader
					URL commonRoot = null;
					for (URL eachResult : results)
					{
						if (commonRoot == null)
						{
							commonRoot = eachResult;
							continue;
						}

						// if the common root isn't rooty enough try the parent
						while (!eachResult.toString()
							.startsWith(
								commonRoot.toString() ))
						{
							try
							{
								commonRoot = URLTools.getParent( commonRoot );
							}
							catch (NullPointerException npe)
							{
								throw new NullPointerException( "Couldn't find common root for "
									+ eachResult.toString()
									+ " and "
									+ commonRoot.
										toString() );
							}

						}

					}
					String sUrl = commonRoot.toString();
					for (URL eachResult : results)
					{
						try
						{
							String urlPath = eachResult.toString();

							if (urlPath.startsWith( sUrl ))
							{
								if (!urlPath.endsWith( "/" ))
								{
									String relPath = urlPath.substring( sUrl.length() );
									combinedResources.put(
										relPath,
										eachResult );
								}
							}
							else
							{
								throw new Exception( urlPath
									+ "doesn't contain " + sUrl );
							}
						}
						catch (Throwable ex)
						{
							System.out.printf(
								"%s %s\n",
								ex.getMessage(),
								eachResult.toString() );
						}

					}
				}

			}

			return combinedResources;
		}

	};

	/*
	 * Searches all loaders for the specified path and combines the results into
	 * one list.
	 *
	 * Where results are duplicated i.e two bundles contain /images/mypic.gif
	 * then the latest one is selected. This allows you to pulish changes to
	 * resources in existing bundles without having to update or download all
	 * the previously downloaded resources.
	 */
	/**
	 *
	 * @param sUrl
	 * @return
	 */
	@Deprecated
	private Collection< URL > getAll( String sUrl )
		throws ResourceFinderException
	{
		Map< String, URL > allResults = new TreeMap< String, URL >();

		for (Finder eachLoader : getLoaderList())
		{
			Collection< URL > results = eachLoader.getResources( sUrl );
			if (results != null)
			{
				for (URL eachResult : results)
				{
					String urlPath = eachResult.toString();
					String relPath = urlPath.substring( urlPath.lastIndexOf( sUrl ) );

					if (!allResults.containsKey( relPath ))
					{
						allResults.put(
							relPath,
							eachResult );
					}
				}
			}

		}

		return allResults.values();
	}

}