package com.cybernostics.lib.media.icon;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGLoaderClient;
import com.kitfox.svg.SVGLoaderTask;
import com.kitfox.svg.SVGUniverse;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Future;

/**
 *
 * @author jasonw
 */
public class SVGIconLoader implements ScalableIconLoader
{

	/**
	 * Starts loading the specified icon resource and will call back when done
	 * @param toLoad
	 * @param afterLoadAction
	 * @return 
	 */
	public static Future< URI > load( URL toLoad,
		final IconLoadClient afterLoadAction )
	{
		SVGUniverse svgUniverse = SVGCache.getSVGUniverse();
		SVGLoaderTask slt = svgUniverse.getLoadertask( toLoad );
		slt.setDoAfter( new SVGLoaderClient()
		{

			@Override
			public void imageLoaded( URI lodedURI )
			{
				ScalableSVGIcon svgi = new ScalableSVGIcon( lodedURI );
				afterLoadAction.iconLoaded( svgi );
			}
		} );

		return slt.start();

	}

	@Override
	public void addLoaderClient( ScalableIconLoaderClient client )
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}
}
