package com.cybernostics.lib.io.platform;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class HomeFolder
{

	private static SingletonInstance< File > home = new SingletonInstance< File >()
	{

		@Override
		protected File createInstance()
		{
			return new File( System.getProperty( "user.home" ) );
		}
	};

	public static File get()
	{
		return home.get();
	}

	private static final String fmt = "Could not create %s";

	public static File createChild( String childFolder ) throws IOException
	{
		File f = new File( get(), childFolder );
		if (f.exists() || f.mkdirs())
		{
			return f;
		}

		throw new IOException( String.format(
			fmt,
			f.toString() ) );
	}

	public static void main( String[] args )
	{
		System.out.println( get().toURI()
			.toString() );
		try
		{
			System.out.println( createChild(
				"homefolder/create/test" ).toString() );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				HomeFolder.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}
}
