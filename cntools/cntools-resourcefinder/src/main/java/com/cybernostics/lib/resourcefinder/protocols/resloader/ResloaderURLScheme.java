/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.net.customurl.CustomURLScheme;
import com.cybernostics.lib.urlfactory.URLFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for resloader:// URLs
 * You must call register on this class before creating any URLs with the
 * "resloader:// scheme or you will get a MalformedURLException"
 * By calling the create methods instead of the URL constructor directly, this
 * will be done for you.
 *
 * @author jasonw
 */
public class ResloaderURLScheme
{

	private ResloaderURLScheme()
	{
	}

	private static final String prefix = "resloader://";

	private static final String urlFmt = prefix + "%s/%s";

	private static boolean resloaderRegistered = false;

	public static String createString( Class< ? > objClass, String relPath )
	{
		String urlStr = String.format(
			urlFmt,
										objClass.getCanonicalName(),
										relPath != null ? relPath : "" );
		return urlStr;
	}

	public static URL create( Class< ? > objClass )
	{
		return create(
			objClass,
			"" );
	}

	public static URL create( Class< ? > objClass, String relPath )
	{
		//register();
		try
		{
			return new URL( "resloader",
				objClass.getCanonicalName(),
				-1,
				relPath,
				Handler.get() );
		}
		catch (MalformedURLException ex)
		{
			throw new RuntimeException( ex );
		}

	}

	public static URL create( Object objRoot )
	{
		return create(
			objRoot.getClass(),
			"" );
	}

	public static URL create( Object objRoot, String relPath )
	{
		return create(
			objRoot.getClass(),
			relPath );
	}

	public static void register()
	{
		if (resloaderRegistered)
		{
			return; // already done
		}
		try
		{
			CustomURLScheme.add(
				Handler.get(),
				new ResloaderChildFinder() );
			resloaderRegistered = true;
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				ResourceFinder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}

}
