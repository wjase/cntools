package com.cybernostics.lib.media.icon;

/**
 * provides a uniform way of passing back images in either an immediate or delayed
 * manner. If the image is immediately available pass this object back.
 * If the image is being loaded
 * @author jasonw
 */
public class LoadedScalableIcon implements ScalableIconLoader
{

	ScalableIcon loaded = null;

	public LoadedScalableIcon( ScalableIcon loaded )
	{
		this.loaded = loaded;
	}

	/**
	 * Because the item is already loaded we can simply call iconLoaded
	 * @param client 
	 */
	@Override
	public void addLoaderClient( final ScalableIconLoaderClient client )
	{
		client.iconLoaded( loaded );
	}
}
