package com.cybernostics.lib.gui.panel;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.Finder;
import java.lang.ref.SoftReference;
import java.net.URL;
import javax.swing.ListModel;

/**
 *
 * @author jasonw
 */
public class URLImageCategory implements ImageCategory
{

	Finder loader = null;
	URL root;
	private String name;
	SoftReference< ListModel > imageList = null;

	public URLImageCategory( String name, URL root )
	{
		this( name, root, null );
	}

	public URLImageCategory( String name, URL root, Finder loader )
	{
		this.name = name;
		this.root = root;
		this.loader = loader;
		if (loader == null)
		{
			this.loader = AppResources.getFinder();
		}
	}

	public void setName( String name )
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public ListModel getItems()
	{
		if (imageList == null || imageList.get() == null)
		{
			imageList = new SoftReference< ListModel >( ImageLoader.getThumbIcons(
				root,
				null,
				null ) );
		}
		return imageList.get();

	}
}
