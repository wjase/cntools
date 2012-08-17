package com.cybernostics.lib.media;

import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.net.URL;

/**
 * @author jasonw
 * 
 */
public class SVGIconFactory
{

	public static ScalableIcon loadIcon( URL toLoad )
	{
		//		InputStream is;
		//		SVGIcon icon = null;
		//		try
		//		{
		return new ScalableSVGIcon( toLoad );
		//			is = toLoad.openStream();
		//			URI uriSvg = SVGCache.getSVGUniverse().loadSVG( is, toLoad.toString() );
		//			icon = new SVGIcon();
		//			icon.setSvgURI( uriSvg );
		//			icon.setAntiAlias( true );
		//			icon.setScaleToFit( true );
		//		}
		//		catch ( IOException e )
		//		{
		//			UnhandledExceptionManager.handleException( e );
		//            return NoImageIcon.get();
		//		}

	}
}
