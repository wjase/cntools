package com.cybernostics.lib.cntoolsakbarfont;

import com.cybernostics.lib.cntoolsfonts.FontLoader;
import com.cybernostics.lib.cntoolsfonts.FontLoaderException;
import java.awt.Font;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public class AkbarFont
{

	public static Font get( int size ) throws FontLoaderException
	{
		URL fontURL = AkbarFont.class.getResource( "font/akbar.ttf" );
		FontLoader.loadFont( fontURL );
		return FontLoader.getFont(
			"Akbar",
			size );

	}
}
