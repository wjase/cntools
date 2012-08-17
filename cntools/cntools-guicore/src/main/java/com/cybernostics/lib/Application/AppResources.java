/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.Application;

import com.cybernostics.lib.io.JarUrlFix;
import com.cybernostics.lib.media.Assets;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.ExtendibleResourceBundle;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author jasonw
 */
public class AppResources
{

	private static SingletonInstance< ExtendibleResourceBundle > resources =
																new SingletonInstance< ExtendibleResourceBundle >()
	{

		public static final String APP_RESOURCES_ID = "2b96cc10-422d-11e1-b86c-0800200c9a66";

		@Override
		protected ExtendibleResourceBundle createInstance()
		{
			return new ExtendibleResourceBundle()
			{

				@Override
				public String getId()
				{
					return APP_RESOURCES_ID;
				}

			};
		}

	};

	public static void registerFinder( Finder toAdd )
	{
		resources.get()
			.addFinder(
				toAdd );
	}

	/**
	 * Returns the Finder which has the stated ID or the resources object if
	 * not found or id=default or id=null;
	 *
	 * @param id
	 * @return
	 */
	public static Finder getFinderById( String id )
	{
		if (null == id || ( !id.equals( "default" ) ))
		{
			for (Finder eachFinder : resources.get()
				.getLoaderList())
			{
				if (id.equals( eachFinder.getId() ))
				{
					return eachFinder;
				}
			}
		}
		return resources.get();
	}

	private static boolean assetsRegistered = false;

	/**
	 * reads the xml property file and builds a list of application "assets"
	 */
	public static void registerAssets()
	{
		if (assetsRegistered)
		{
			return;
		}
		assetsRegistered = true;
		try
		{
			Finder f = resources.get();

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler()
			{

				private Finder currentFinder = null;

				@Override
				public void startElement( String uri,
											String localName,
											String qName,
											Attributes attributes )
						throws SAXException
				{
					if (qName.equals( "finder" ))
					{
						String id = attributes.getValue( "id" );
						currentFinder = getFinderById( id );
						return;
					}
					if (qName.equals( "asset" ))
					{
						try
						{
							Assets.put(
									attributes.getValue( "id" ),
									currentFinder.getResource( attributes.getValue( "path" ) ) );
						}
						catch (ResourceFinderException ex)
						{
							throw new RuntimeException( ex );
						}
					}
					super.startElement(
							uri,
							localName,
							qName,
							attributes );
				}

			};

			URL toLoad = f.getResource( "assets/assets.xml" );
			saxParser.parse(
					JarUrlFix.getURLStream( toLoad ),
					handler );
		}
		catch (Exception ex)
		{
			throw new RuntimeException( ex );
		}

	}

	/**
	 * Gets a single resource from the specified URL.
	 *
	 * @param sUrl
	 * @return
	 */
	public static URL get( String sUrl ) throws ResourceFinderException
	{
		return resources.get()
			.get(
				sUrl );
	}

	/*
	 * Searches all loaders for the specified path and combines the results
	 * into one list.
	 *
	 * Where results are duplicated i.e two bundles contain /images/mypic.gif
	 * then the latest one is selected. This allows you to pulish changes to
	 * resources in existing bundles without having to update or download all the
	 * previously downloaded resources.
	 */
	/**
	 *
	 * @param sUrl
	 * @return
	 */
	public static Collection< URL > getAll( String sUrl )
			throws ResourceFinderException
	{
		return resources.get()
			.getResources(
				sUrl );

	}

	public static Finder getFinder()
	{
		return resources.get();
	}

	public static URL getResource( String path ) throws ResourceFinderException
	{
		Finder f = getFinder();
		return f.getResource(
				path );
	}

}
