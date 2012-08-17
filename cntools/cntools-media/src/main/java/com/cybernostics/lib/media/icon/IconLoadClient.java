package com.cybernostics.lib.media.icon;

/**
 * Callback interface when an IconLoader is finished
 * @author jasonw
 */
public interface IconLoadClient
{
	public void iconLoaded( ScalableSVGIcon svgsi );
}
