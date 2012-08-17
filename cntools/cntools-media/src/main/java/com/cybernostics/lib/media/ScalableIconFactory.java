package com.cybernostics.lib.media;

import java.net.URL;

import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.ScalableIcon;

/**
 * @author jasonw
 * 
 */
public class ScalableIconFactory
{

	public static ScalableIcon loadIcon( URL toLoad )
	{
		return AttributedScalableIcon.create( toLoad );
	}
}
