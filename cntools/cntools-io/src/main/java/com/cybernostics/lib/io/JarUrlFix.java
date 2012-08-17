package com.cybernostics.lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class JarUrlFix
{

	/**
	 * This ensures that jar files are not left locked when an entity within it is read.
	 * @param toGet
	 * @return 
	 */
	public static InputStream getURLStream( URL toGet )
	{
		InputStream is = null;

		try
		{
			URLConnection uc = toGet.openConnection();
			if (uc instanceof JarURLConnection)
			{
				JarURLConnection jc = (JarURLConnection) uc;
				jc.setDefaultUseCaches( false );
			}
			is = uc.getInputStream();

		}
		catch (IOException ex)
		{
			Logger.getLogger(
				JarUrlFix.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return is;

	}

}
