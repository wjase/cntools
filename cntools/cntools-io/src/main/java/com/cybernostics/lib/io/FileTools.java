package com.cybernostics.lib.io;

import com.cybernostics.lib.collections.IterableArray;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * Static methods for copying moving and comparing files
 * 
 * @author jasonw
 * 
 */
public class FileTools
{

	public static boolean cleanDir( File dir )
	{
		if (!dir.exists())
		{
			return true;
		}

		if (dir.isDirectory())
		{
			String[] children = dir.list();
			for (String eachChildName : IterableArray.get( children ))
			{
				File child = new File( dir, eachChildName );
				if (child.isDirectory())
				{
					if (!cleanDir( child ))
					{
						return false;
					}
				}
				if (!child.delete())
				{
					return false;
				}

			}
		}

		return true;
	}

	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.

	public static boolean deleteDir( File dir )
	{
		if (!cleanDir( dir ))
		{
			return false;
		}
		return dir.delete();
	}

	public static boolean copy( File source, File dest )
	{
		try
		{
			InputStream in = new FileInputStream( source );

			// For Append the file.
			// OutputStream out = new FileOutputStream(f2,true);

			// For Overwrite the file.
			OutputStream out = new FileOutputStream( dest );

			byte[] buf = new byte[ 1024 ];
			int len;
			while (( len = in.read( buf ) ) > 0)
			{
				out.write(
					buf,
					0,
					len );
			}
			in.close();
			out.close();
			return true;
		}
		catch (FileNotFoundException ex)
		{
			UnhandledExceptionManager.handleException( ex );
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		return false;
	}

	public static boolean rename( File source, File dest )
	{
		if (copy(
			source,
			dest ))
		{
			source.delete();
			return true;
		}
		return false;
	}

	public static boolean move( File source, File dest )
	{
		return rename(
			source,
			new File( dest, source.getName() ) );
	}
}