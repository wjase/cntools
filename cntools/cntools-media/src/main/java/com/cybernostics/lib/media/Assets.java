package com.cybernostics.lib.media;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class acts as a registry of common resources, so that they can be "Skinned"
 * and updated.
 * 
 * Each URL is given an Id which is used in code to retrieve it.
 * 
 * eg. instead of doing this:
 *      ResourceFinder myFinder = ...
 *      ...
 *      ScalableIcon buttonIcon = new ScalableIcon(myFinder.getResource("somepath/myButtonImage.gif"));
 * 
 * do this:
 *      // register the resource with an independant id 
 *      Assets.put("ui.button.buttonImage", myFinder.getResource("somepath/myButtonImage.gif"));
 *      ...
 *      ScalableIcon buttonIcon = new ScalableIcon(Assets.get("ui.button.buttonImage"));
 *
 * This way, the resource can be later redirected and the ui item updated, without updating
 * all the code which uses it.
 * 
 * @author jasonw
 */
public class Assets
{

	private static ConcurrentHashMap< String, URL > urls = new ConcurrentHashMap< String, URL >();

	public static void put( String id, URL toStore )
	{
		urls.put(
			id,
			toStore );
	}

	public static URL get( String id )
	{
		return urls.get( id );

	}

}
