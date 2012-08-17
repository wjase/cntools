package com.cybernostics.lib.io.file;

import com.cybernostics.lib.collections.IterableArray;
import com.cybernostics.lib.io.platform.MsDosPath;
import com.cybernostics.lib.regex.Regex;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class whirls through
 * @author jasonw
 */
public class RecursiveURLProcessor
{

	public static void process( URL rootURL, URLVisitor visitor )
		throws IOException
	{
		RecursiveURLProcessor processor = new RecursiveURLProcessor( visitor,
			rootURL );
		processor.process();
	}

	public static void process( String rootURL, URLVisitor visitor )
		throws IOException
	{
		URL root = null;
		if (MsDosPath.isDosFileSpec( rootURL ))
		{
			root = MsDosPath.getDosFileSpec( rootURL );
		}
		else
		{
			root = new URL( rootURL );
		}

		process(
			root,
			visitor );
	}

	URLVisitor theVisitor = null;

	URL root = null;

	protected RecursiveURLProcessor( URLVisitor visitor, URL rootURL )
	{
		this.theVisitor = visitor;
		this.root = rootURL;

	}

	public void process() throws IOException
	{
		if (root.getPath()
			.indexOf(
				"!" ) != -1)
		{
			processJarURL( root );
		}
		else
		{
			process( root );
		}

	}

	private static final Regex tailJarBit = new Regex( "!/(.+)$" );

	protected void processJarURL( URL currentRoot ) throws IOException
	{
		String rootMatch = "";
		if (tailJarBit.find( currentRoot.getPath() ))
		{
			rootMatch = tailJarBit.group( 1 );
		}

		// Its a jar resource
		JarURLConnection jarConnection;
		JarFile jf;
		jarConnection = (JarURLConnection) currentRoot.openConnection();
		jf = jarConnection.getJarFile();

		if (( jarConnection != null ) && ( jf != null ))
		{
			String jarUrl = jarConnection.getURL()
				.toExternalForm();
			String jarFilename = "";
			int pos = jarUrl.indexOf( '!' );
			if (pos != -1)
			{
				jarFilename = jarUrl.substring(
					0,
					pos );
			}

			List< JarEntry > entries = Collections.list( jf.entries() );
			for (JarEntry entry : entries)
			{
				String entryName = entry.getName();
				if (entryName.startsWith( rootMatch ))
				{
					String newURL = jarFilename + "!/" + entry.getName();
					try
					{
						theVisitor.visit( new URL( newURL ) );
					}
					catch (MalformedURLException ex)
					{
						throw new RuntimeException( ex );
					}

				}
			}
		}

	}

	protected void process( URL currentRoot )
	{

		File theRoot = new File( currentRoot.getFile() );

		String[] filenames = theRoot.list();
		if (filenames != null)
		{
			for (String eachFile : IterableArray.get( filenames ))
			{
				URL childURL = null;
				File child = new File( theRoot, eachFile );

				try
				{
					childURL = child.toURL();
				}
				catch (MalformedURLException e)
				{
					throw new RuntimeException( e );
				}

				theVisitor.visit( childURL );
				if (child.isDirectory())
				{
					process( childURL );
				}
			}
		}
	}

}