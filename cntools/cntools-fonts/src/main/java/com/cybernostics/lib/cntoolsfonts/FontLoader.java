package com.cybernostics.lib.cntoolsfonts;

/*
 * @(#)DemoFonts.java 1.17 06/08/29
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, this list of conditions
 * and the following disclaimer in the documentation and/or other materials provided with the
 * distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED
 * CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC.
 * ("SUN") AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF
 * LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for use in the design,
 * construction, operation or maintenance of any nuclear facility.
 */

/*
 * @(#)DemoFonts.java 1.17 06/08/29
 */
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of the dynamically loaded fonts found in the fonts directory.
 */
public class FontLoader
{

	private static Map< String, SoftReference< Font >> fontcache = new ConcurrentHashMap< String, SoftReference< Font >>();
	private static Map< String, URL > urlcache = new ConcurrentHashMap< String, URL >();

	public static Set< String > getFontNames()
	{
		return urlcache.keySet();
	}

	public static void loadFont( URL toRead ) throws FontLoaderException
	{
		try
		{
			Font font = Font.createFont(
				Font.TRUETYPE_FONT,
				toRead.openStream() );
			String fontName = font.getName();
			fontcache.put(
				font.getName(),
				new SoftReference< Font >( font ) );
			urlcache.put(
				fontName,
				toRead );

			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont( font );
			System.setProperty(
				font.getFontName(),
				font.getFontName() );
		}
		catch (Exception e)
		{
			throw new FontLoaderException( e );
		}

	}

	public static Font getFont( String name, int sizeRequested )
		throws FontLoaderException
	{
		Font font = getFont( name );
		if (font.getSize() != sizeRequested)
		{
			if (font != null)
			{
				font = font.deriveFont(
					Font.PLAIN,
					sizeRequested );
			}
		}
		return font;
	}

	public static Font getFont( String name ) throws FontLoaderException
	{
		Font font = null;
		if (fontcache.containsKey( name ))
		{
			SoftReference< Font > ref = fontcache.get( name );
			font = ref.get();
		}
		if (font != null)
		{
			if (urlcache.containsKey( name ))
			{
				try
				{
					loadFont( urlcache.get( name ) );
					SoftReference< Font > ref = fontcache.get( name );
					font = ref.get();

				}
				catch (Exception ex)
				{
					throw new FontLoaderException( ex );
				}
			}
		}
		return font;
	}
}