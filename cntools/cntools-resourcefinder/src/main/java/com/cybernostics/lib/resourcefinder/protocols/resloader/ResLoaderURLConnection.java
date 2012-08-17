package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class ResLoaderURLConnection extends URLConnection
{

	/**
	 * Encodes a custom url of the form
	 * resloader:///package.spec.of.relative.class/path/to/item
	 * For absolute paths then double the slash preceding the actual path part ie:
	 * resloader:///package.spec.of.relative.class//absolute.path/to/item
	 * 
	 * @param encoded
	 * @return a URL encoded in the form file://apsoidfuapsodifu or jar:file: for embedded resources
	 * 
	 */
	public static URL toLocalURL( URL encoded ) throws MalformedURLException
	{
		//        String combinedPathPart = encoded.getPath();
		//        int URLpathSlash = combinedPathPart.indexOf( "/", 1 );
		//        String loaderClassPath = combinedPathPart.substring( 1, URLpathSlash );
		//        String pathSpec = combinedPathPart.substring( URLpathSlash + 1 );

		ResloaderURLBits parsed = ResloaderURLBits.create( encoded );
		try
		{
			Class loadClass = getLoaderClass( parsed.getClassComponent() );
			try
			{
				String path = loadClass.getPackage()
					.getName()
					.replaceAll(
						"\\.",
						"/" );

				URL rootURL = loadClass.getResource( "" );
				String rootURLString = rootURL.toString();
				//loadClass = Thread.currentThread().getContextClassLoader().loadClass( parsed.getClassComponent() );
				//
				//                String urlAsString = loadURL.toString();

				if (rootURLString.startsWith( "jar" ))
				{
					if (!rootURLString.endsWith( "/" ))
					{
						rootURLString = rootURLString + "/";
					}
					String tailPath = parsed.getPathComponent();
					if (tailPath.startsWith( "/" ))
					{
						tailPath = tailPath.replaceFirst(
							"^/",
							"" );
					}
					URL tryURL = new URL( rootURLString + tailPath );
					return tryURL;
				}
				else
				{
					URL loadURL = loadClass.getProtectionDomain()
						.getCodeSource()
						.getLocation();
					//System.out.println( loadURL.toString() );
					if (parsed.getPathComponent()
						.length() > 0)
					{
						path = path + parsed.getPathComponent();
					}
					URL tryURL = new URL( loadURL, loadURL.getPath() + path );
					if (tryURL != null)
					{
						return tryURL;
					}

				}

			}
			catch (Exception e)
			{
				Logger.getLogger(
					ResLoaderURLConnection.class.getName() )
					.log(
						Level.SEVERE,
						null,
						e );
			}
			Finder loader = ResourceFinder.getLoaderFromName( parsed.getClassComponent() );
			return loader.getResource( parsed.getPathComponent() );
		}
		catch (ResourceFinderException ex)
		{
			MalformedURLException mf = new MalformedURLException();
			mf.initCause( ex );
			throw mf;
			//Logger.getLogger( ResLoaderURLConnection.class.getName() ).log( Level.SEVERE, null, ex );
		}
		catch (ClassNotFoundException cnf)
		{
			MalformedURLException mf = new MalformedURLException();
			mf.initCause( cnf );
			throw mf;
		}

	}

	private static ConcurrentHashMap< String, Class > loaderClasses = new ConcurrentHashMap< String, Class >();

	private static Class getLoaderClass( String path )
	{
		if (loaderClasses.contains( path ))
		{
			return loaderClasses.get( path );
		}
		Class loadClass;
		try
		{
			loadClass = Thread.currentThread()
				.getContextClassLoader()
				.loadClass(
					path );
			loaderClasses.put(
				path,
				loadClass );
			return loadClass;
		}
		catch (ClassNotFoundException ex)
		{
			Logger.getLogger(
				ResLoaderURLConnection.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return null;

	}

	public static List< URL > getResources( URL encoded )
	{
		ResloaderURLBits parsed = ResloaderURLBits.create( encoded );
		Class loadClass = getLoaderClass( parsed.getClassComponent() );
		try
		{
			Enumeration< URL > urls = loadClass.getClassLoader()
				.getResources(
					"/" );
			ArrayList< URL > urlList = new ArrayList< URL >();
			for (URL url = urls.nextElement(); urls.hasMoreElements(); url = urls.nextElement())
			{
				urlList.add( url );
			}
			return urlList;
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				ResLoaderURLConnection.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return null;

	}

	public ResLoaderURLConnection( URL url ) throws MalformedURLException
	{
		super( url );

		encoded = url;
		localURL = toLocalURL( encoded );
	}

	private URL encoded = null;

	private URL localURL = null;

	private URLConnection localConnection = null;

	private URLConnection getInternalConnection()
	{
		if (localConnection == null)
		{
			try
			{
				localConnection = localURL.openConnection();
			}
			catch (IOException ex)
			{
				Logger.getLogger(
					ResLoaderURLConnection.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
		return localConnection;
	}

	@Override
	public void setUseCaches( boolean usecaches )
	{
		getInternalConnection().setUseCaches(
			usecaches );
	}

	@Override
	public void setRequestProperty( String key, String value )
	{
		getInternalConnection().setRequestProperty(
			key,
			value );
	}

	@Override
	public void setReadTimeout( int timeout )
	{
		getInternalConnection().setReadTimeout(
			timeout );
	}

	@Override
	public void setIfModifiedSince( long ifmodifiedsince )
	{
		getInternalConnection().setIfModifiedSince(
			ifmodifiedsince );
	}

	@Override
	public void setDoOutput( boolean dooutput )
	{
		getInternalConnection().setDoOutput(
			dooutput );
	}

	@Override
	public void setDoInput( boolean doinput )
	{
		getInternalConnection().setDoInput(
			doinput );
	}

	@Override
	public void setDefaultUseCaches( boolean defaultusecaches )
	{
		getInternalConnection().setDefaultUseCaches(
			defaultusecaches );
	}

	@Override
	public void setConnectTimeout( int timeout )
	{
		getInternalConnection().setConnectTimeout(
			timeout );
	}

	@Override
	public void setAllowUserInteraction( boolean allowuserinteraction )
	{
		getInternalConnection().setAllowUserInteraction(
			allowuserinteraction );
	}

	@Override
	public boolean getUseCaches()
	{
		return getInternalConnection().getUseCaches();
	}

	@Override
	public URL getURL()
	{
		return getInternalConnection().getURL();
	}

	@Override
	public String getRequestProperty( String key )
	{
		return getInternalConnection().getRequestProperty(
			key );
	}

	@Override
	public Map< String, List< String >> getRequestProperties()
	{
		return getInternalConnection().getRequestProperties();
	}

	@Override
	public int getReadTimeout()
	{
		return getInternalConnection().getReadTimeout();
	}

	@Override
	public Permission getPermission() throws IOException
	{
		return getInternalConnection().getPermission();
	}

	private static ByteArrayOutputStream outbuf = new ByteArrayOutputStream();

	public static OutputStream getOutputBuffer()
	{
		return outbuf;
	}

	@Override
	public OutputStream getOutputStream() throws IOException
	{
		// to simulate post requests
		return outbuf;
	}

	@Override
	public long getLastModified()
	{
		return getInternalConnection().getLastModified();
	}

	@Override
	public long getIfModifiedSince()
	{
		return getInternalConnection().getIfModifiedSince();
	}

	@Override
	public Map< String, List< String >> getHeaderFields()
	{
		return getInternalConnection().getHeaderFields();
	}

	@Override
	public String getHeaderFieldKey( int n )
	{
		return getInternalConnection().getHeaderFieldKey(
			n );
	}

	@Override
	public int getHeaderFieldInt( String name, int Default )
	{
		return getInternalConnection().getHeaderFieldInt(
			name,
			Default );
	}

	@Override
	public long getHeaderFieldDate( String name, long Default )
	{
		return getInternalConnection().getHeaderFieldDate(
			name,
			Default );
	}

	@Override
	public String getHeaderField( int n )
	{
		return getInternalConnection().getHeaderField(
			n );
	}

	@Override
	public String getHeaderField( String name )
	{
		return getInternalConnection().getHeaderField(
			name );
	}

	@Override
	public long getExpiration()
	{
		return getInternalConnection().getExpiration();
	}

	@Override
	public boolean getDoOutput()
	{
		return getInternalConnection().getDoOutput();
	}

	@Override
	public boolean getDoInput()
	{
		return getInternalConnection().getDoInput();
	}

	@Override
	public boolean getDefaultUseCaches()
	{
		return getInternalConnection().getDefaultUseCaches();
	}

	@Override
	public long getDate()
	{
		return getInternalConnection().getDate();
	}

	@Override
	public String getContentType()
	{
		return getInternalConnection().getContentType();
	}

	@Override
	public int getContentLength()
	{
		return getInternalConnection().getContentLength();
	}

	@Override
	public String getContentEncoding()
	{
		return getInternalConnection().getContentEncoding();
	}

	@Override
	public Object getContent( Class[] classes ) throws IOException
	{
		return getInternalConnection().getContent(
			classes );
	}

	@Override
	public Object getContent() throws IOException
	{
		return getInternalConnection().getContent();
	}

	@Override
	public int getConnectTimeout()
	{
		return getInternalConnection().getConnectTimeout();
	}

	@Override
	public boolean getAllowUserInteraction()
	{
		return getInternalConnection().getAllowUserInteraction();
	}

	@Override
	public void addRequestProperty( String key, String value )
	{
		getInternalConnection().addRequestProperty(
			key,
			value );
	}

	@Override
	public void connect() throws IOException
	{
	}

	@Override
	public InputStream getInputStream() throws IOException
	{
		return getInternalConnection().getInputStream();
	}

}
