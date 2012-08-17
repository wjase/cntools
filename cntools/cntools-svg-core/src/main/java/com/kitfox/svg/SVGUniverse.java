/*
 * SVGUniverse.java
 * 
 * 
 * The Salamander Project - 2D and 3D graphics libraries in Java Copyright (C) 2004 Mark McKay
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * Mark McKay can be contacted at mark@kitfox.com. Salamander and other projects can be found at
 * http://www.kitfox.com
 * 
 * Created on February 18, 2004, 11:43 PM
 */
package com.kitfox.svg;

import com.cybernostics.lib.urlfactory.URLFactory;
import com.kitfox.svg.app.beans.SVGIcon;
import com.kitfox.svg.elements.Font;
import com.kitfox.svg.elements.SVGElement;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.lang.ref.SoftReference;
import java.net.*;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Many SVG files can be loaded at one time. These files will quite likely need
 * to reference one another. The SVG universe provides a container for all these
 * files and the means for them to relate to each other.
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class SVGUniverse implements Serializable
{

	public static final long serialVersionUID = 0;

	transient private PropertyChangeSupport changes = new PropertyChangeSupport( this );

	/**
	 * Maps document URIs to their loaded SVG diagrams. Note that URIs for
	 * documents loaded from URLs will reflect their URLs and URIs for documents
	 * initiated from streams will have the scheme <i>svgSalamander</i>.
	 */
	final ConcurrentHashMap< URI, SVGDiagram > loadedDocs = new ConcurrentHashMap< URI, SVGDiagram >();

	final ConcurrentHashMap< String, Font > loadedFonts = new ConcurrentHashMap< String, Font >();

	private final ConcurrentHashMap< URL, SoftReference< BufferedImage >> loadedImages =
																		new ConcurrentHashMap< URL, SoftReference< BufferedImage >>();

	/**
	 * This is a concurrent queue of items to load. To save deadlocks.
	 */
	private final ConcurrentHashMap< URI, SVGLoaderTask > loaderQueue = new ConcurrentHashMap< URI, SVGLoaderTask >();

	public ConcurrentHashMap< URI, SVGLoaderTask > getLoaderQueue()
	{
		return loaderQueue;
	}

	public static final String INPUTSTREAM_SCHEME = "svgSalamander";

	/**
	 * Current time in this universe. Used for resolving attributes that are
	 * influenced by track information. Time is in milliseconds. Time 0
	 * coresponds to the time of 0 in each member diagram.
	 */
	protected double curTime = 0.0;

	private boolean verbose = false;
	//Cache reader for efficiency

	XMLReader cachedReader;

	/**
	 * Creates a new instance of SVGUniverse
	 */
	public SVGUniverse()
	{
	}

	public void addPropertyChangeListener( PropertyChangeListener l )
	{
		changes.addPropertyChangeListener( l );
	}

	public void removePropertyChangeListener( PropertyChangeListener l )
	{
		changes.removePropertyChangeListener( l );
	}

	/**
	 * Release all loaded SVG document from memory
	 */
	public void clear()
	{
		loadedDocs.clear();
		loadedFonts.clear();
		loadedImages.clear();
	}

	/**
	 * Returns the current animation time in milliseconds.
	 */
	public double getCurTime()
	{
		return curTime;
	}

	public void setCurTime( double curTime )
	{
		double oldTime = this.curTime;
		this.curTime = curTime;
		changes.firePropertyChange(
			"curTime",
			new Double( oldTime ),
			new Double( curTime ) );
	}

	/**
	 * Updates all time influenced style and presentation attributes in all SVG
	 * documents in this universe.
	 */
	public void updateTime() throws SVGException
	{
		for (Iterator it = loadedDocs.values()
			.iterator(); it.hasNext();)
		{
			SVGDiagram dia = (SVGDiagram) it.next();
			dia.updateTime( curTime );
		}
	}

	/**
	 * Called by the Font element to let the universe know that a font has been
	 * loaded and is available.
	 */
	public void registerFont( Font font )
	{
		if (font == null)
		{
			throw new RuntimeException( "null font in registerFont()" );
		}
		loadedFonts.put(
			font.getFontFace()
				.getFontFamily(),
			font );
	}

	public Font getDefaultFont()
	{
		for (Iterator it = loadedFonts.values()
			.iterator(); it.hasNext();)
		{
			return (Font) it.next();
		}
		return null;
	}

	public Font getFont( String fontName )
	{
		return (Font) loadedFonts.get( fontName );
	}

	ConcurrentHashMap< URL, String > inlineData = new ConcurrentHashMap< URL, String >();

	public URL registerImage( URI imageURI )
	{
		String scheme = imageURI.getScheme();
		if (scheme.equals( "data" ))
		{
			String path = imageURI.getRawSchemeSpecificPart();
			int idx = path.indexOf( ';' );
			String mime = path.substring(
				0,
				idx );
			String content = path.substring( idx + 1 );

			if (content.startsWith( "base64" ))
			{
				content = content.substring( 6 );
				try
				{
					byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer( content );
					ByteArrayInputStream bais = new ByteArrayInputStream( buf );
					BufferedImage img = ImageIO.read( bais );

					URL url;
					int urlIdx = 0;
					while (true)
					{
						url = new URL( "file:data:" + "img" + urlIdx );
						if (!loadedImages.containsKey( url ))
						{
							break;
						}
						++urlIdx;
					}

					// save for later
					inlineData.put(
						url,
						content );

					SoftReference ref = new SoftReference( img );
					loadedImages.put(
						url,
						ref );

					return url;
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}

			}
		}
		else
		{
			try
			{
				if (!loaderQueue.containsKey( imageURI ))
				{
					URL url = URLFactory.newURL( imageURI.toASCIIString() );
					registerImage( url );
					return url;
				}
				return URLFactory.newURL( imageURI.toASCIIString() );

			}
			catch (MalformedURLException ex)
			{
				ex.printStackTrace();
			}
			return null;
		}
		return null;
	}

	void bufferNestedImage( URI toBuffer )
	{
		if (toBuffer == null)
		{
			throw new NullPointerException();
		}
		SoftReference refInner;

		if (toBuffer.getScheme()
			.equals(
				"data" ))
		{
			registerImage( toBuffer );
			return;
		}
		else
			if (toBuffer.toASCIIString()
				.endsWith(
					".svg" ))
			{
				SVGIcon icon = new SVGIcon();
				icon.setSvgURI( toBuffer );
				BufferedImage img = new BufferedImage( icon.getIconWidth(),
					icon.getIconHeight(),
													BufferedImage.TYPE_INT_ARGB );
				Graphics2D g = img.createGraphics();
				icon.paintIcon(
					null,
					g,
					0,
					0 );
				g.dispose();
				refInner = new SoftReference( img );
				try
				{
					loadedImages.put(
						URLFactory.newURL( toBuffer.toASCIIString() ),
						refInner );
				}
				catch (MalformedURLException ex)
				{
					Logger.getLogger(
						SVGUniverse.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}

			}
			else
			{
				try
				{
					URL imageURL = URLFactory.newURL( toBuffer.toASCIIString() );
					SoftReference ref;
					InputStream is = getURLStream( imageURL );
					try
					{
						BufferedImage img = ImageIO.read( is );
						ref = new SoftReference( img );
						loadedImages.put(
							imageURL,
							ref );
					}
					catch (IOException ex)
					{
						Logger.getLogger(
							SVGUniverse.class.getName() )
							.log(
								Level.SEVERE,
								null,
								ex );
					}
					finally
					{
						closeStreamQuietly( is );
					}

				}
				catch (MalformedURLException ex)
				{
					Logger.getLogger(
						SVGUniverse.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}
			}

	}

	public void registerImage( final URL imageURL )
	{
		// if it is loaded
		SoftReference< BufferedImage > softimg = loadedImages.get( imageURL );
		if (softimg != null)
		{
			if (softimg.get() != null)
			{
				return;
			}
			try
			{
				// renew the soft reference
				bufferNestedImage( imageURL.toURI() );
			}
			catch (URISyntaxException ex)
			{
				Logger.getLogger(
					SVGUniverse.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			return;

		}

		// or queued for loading
		if (loaderQueue.contains( imageURL ))
		{
			return;
		}

		try
		{
			final String fileName = imageURL.getFile();
			if (".svg".equals( fileName.substring(
				fileName.length() - 4 )
				.toLowerCase() ))
			{
				SVGLoaderTask slt = getLoadertask( imageURL );

				Future< URI > result = slt.getMyResult();
				slt.setDoAfter( new SVGLoaderClient()
				{

					@Override
					public void imageLoaded( URI lodedURI )
					{
						if (lodedURI == null)
						{
							throw new RuntimeException( String.format(
								"SVGLoader failed %s",
								fileName ) );
						}
						bufferNestedImage( lodedURI );
					}

				} );

				if (result == null)
				{

					startLoadTask( slt ); // do the load sometime

				}

			}
			else
			{
				SoftReference ref;
				InputStream is = getURLStream( imageURL );
				try
				{
					BufferedImage img = ImageIO.read( is );
					ref = new SoftReference( img );
				}
				finally
				{
					is.close();
				}
				loadedImages.put(
					imageURL,
					ref );

			}
		}
		catch (Exception e)
		{
			System.err.println( "Could not load image: " + imageURL );
			e.printStackTrace();
		}
	}

	public SVGLoaderTask getLoadertask( URL toLoad )
	{
		try
		{
			final URI asURI = toLoad.toURI();

			SVGLoaderTask slt = getLoaderTask( asURI );
			return slt;
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return null;
	}

	/**
	 * This ensures that jar files are not left locked when an entity within it
	 * is read.
	 *
	 * @param toGet
	 * @return
	 */
	private InputStream getURLStream( URL toGet )
	{
		InputStream is = null;

		if (inlineData.containsKey( toGet ))
		{
			try
			{
				byte[] buf = new sun.misc.BASE64Decoder().decodeBuffer( inlineData.get( toGet ) );
				return new ByteArrayInputStream( buf );
			}
			catch (IOException ex)
			{
			}

		}
		try
		{
			URLConnection uc = toGet.openConnection();
			if (uc instanceof JarURLConnection)
			{
				JarURLConnection jc = (JarURLConnection) uc;
				//jc.setDefaultUseCaches( false );
			}
			is = uc.getInputStream();

			if (is == null)
			{
				throw new NullPointerException( toGet.toString() );
			}

		}
		catch (IOException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return is;

	}

	public BufferedImage getImage( URL imageURL ) throws IOException
	{
		SoftReference ref = (SoftReference) loadedImages.get( imageURL );
		if (ref == null)
		{
			registerImage( imageURL );
			//try again
			ref = (SoftReference) loadedImages.get( imageURL );
			if (ref == null)
			{
				return null;
			}
		}

		BufferedImage img = (BufferedImage) ref.get();
		//If image was cleared from memory, reload it
		if (img == null)
		{
			registerImage( imageURL );
		}

		return img;
	}

	/**
	 * Returns the element of the document at the given URI. If the document is
	 * not already loaded, it will be.
	 */
	public SVGElement getElement( URI path )
	{
		return getElement(
			path,
			true );
	}

	public SVGElement getElement( URL path )
	{
		try
		{
			URI uri = new URI( path.toString() );
			return getElement(
				uri,
				true );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Looks up a href within our universe. If the href refers to a document
	 * that is not loaded, it will be loaded. The URL #target will then be
	 * checked against the SVG diagram's index and the coresponding element
	 * returned. If there is no coresponding index, null is returned.
	 */
	public SVGElement getElement( URI path, boolean loadIfAbsent )
	{
		try
		{
			//Strip fragment from URI
			URI xmlBase = new URI( path.getScheme(),
				path.getSchemeSpecificPart(),
				null );

			SVGDiagram dia = (SVGDiagram) loadedDocs.get( xmlBase );
			if (dia == null && loadIfAbsent)
			{
				//System.err.println("SVGUnivserse: " + xmlBase.toString());
				//javax.swing.JOptionPane.showMessageDialog(null, xmlBase.toString());
				URL url = URLFactory.newURL( xmlBase.toASCIIString() );

				loadSVG(
					url,
					false );
				dia = (SVGDiagram) loadedDocs.get( xmlBase );
				if (dia == null)
				{
					return null;
				}
			}

			String fragment = path.getFragment();
			return fragment == null ? dia.getRoot() : dia.getElement( fragment );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public SVGDiagram getDiagram( URI xmlBase )
	{
		return getDiagram(
			xmlBase,
			true );
	}

	/**
	 * Returns the diagram that has been loaded from this root. If diagram is
	 * not already loaded, returns null.
	 */
	public SVGDiagram getDiagram( URI xmlBase, boolean loadIfAbsent )
	{
		if (xmlBase == null)
		{
			return null;
		}

		SVGDiagram dia = (SVGDiagram) loadedDocs.get( xmlBase );
		if (dia != null || !loadIfAbsent)
		{
			return dia;
		}

		//Load missing diagram
		try
		{
			URL url;
			if ("jar".equals( xmlBase.getScheme() )
				&& xmlBase.getPath() != null && !xmlBase.getPath()
					.contains(
						"!/" ))
			{
				//Workaround for resources stored in jars loaded by Webstart.
				//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6753651
				url = SVGUniverse.class.getResource( "xmlBase.getPath()" );
			}
			else
			{
				url = URLFactory.newURL( xmlBase.toASCIIString() );
			}

			loadSVG(
				url,
				false );
			dia = (SVGDiagram) loadedDocs.get( xmlBase );

			return dia;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Wraps input stream in a BufferedInputStream. If it is detected that this
	 * input stream is GZIPped, also wraps in a GZIPInputStream for inflation.
	 *
	 * @param is Raw input stream
	 * @return Uncompressed stream of SVG data
	 * @throws java.io.IOException
	 */
	private InputStream createDocumentInputStream( InputStream is )
		throws IOException
	{
		BufferedInputStream bin = new BufferedInputStream( is );
		bin.mark( 2 );
		int b0 = bin.read();
		int b1 = bin.read();
		bin.reset();

		//Check for gzip magic number
		if (( b1 << 8 | b0 ) == GZIPInputStream.GZIP_MAGIC)
		{
			GZIPInputStream iis = new GZIPInputStream( bin );
			return iis;
		}
		else
		{
			//Plain text
			return bin;
		}
	}

	public URI loadSVG( URL docRoot )
	{
		return loadSVG(
			docRoot,
			false );
	}

	/**
	 * Loads an SVG file and all the files it references from the URL provided.
	 * If a referenced file already exists in the SVG universe, it is not
	 * reloaded.
	 *
	 * @param docRoot - URL to the location where this SVG file can be found.
	 * @param forceLoad - if true, ignore cached diagram and reload
	 * @return - The URI that refers to the loaded document
	 */
	public URI loadSVG( URL docRoot, boolean forceLoad )
	{
		int attempts = 0;
		do
		{
			++attempts;
			try
			{

				URI uri = new URI( docRoot.toString() );
				if (!needsLoading( uri ) && !forceLoad)
				{
					return uri;
				}

				URI u = loadSVG( uri );
				if (u != null)
				{
					return u;
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
			}

		}
		while (attempts < 10);

		return null;
	}

	public boolean needsLoading( URI toLoad )
	{
		if (toLoad == null)
		{
			return false;
		}
		if (loadedDocs.containsKey( toLoad ))
		{
			return false;
		}
		if (loaderQueue.containsKey( toLoad ))
		{
			return false;
		}
		return true;
	}

	public URI loadSVG( InputStream is, String name ) throws IOException
	{
		try
		{
			return loadSVG(
				is,
				name,
				false );
		}
		finally
		{
			closeStreamQuietly( is );
		}
	}

	public URI loadSVG( InputStream is, String name, boolean forceLoad )
		throws IOException
	{
		try
		{
			URI uri = getStreamBuiltURI( name );
			if (!needsLoading( uri ) && !forceLoad)
			{
				return null;
			}

			return loadSVG(
				uri,
				new InputSource( createDocumentInputStream( is ) ) );
		}
		finally
		{
			closeStreamQuietly( is );
		}
	}

	public URI loadSVG( Reader reader, String name )
	{
		try
		{
			return loadSVG(
				reader,
				name,
				false );
		}
		finally
		{
			closeReaderQuietly( reader );
		}
	}

	public static void closeReaderQuietly( Reader r )
	{
		try
		{
			if (r != null)
			{
				r.close();
			}
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	public static void closeStreamQuietly( InputStream is )
	{
		if (is != null)
		{
			try
			{
				is.close();
			}
			catch (IOException ex)
			{
				Logger.getLogger(
					SVGUniverse.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}

	}

	/**
	 * This routine allows you to create SVG documents from data streams that
	 * may not necessarily have a URL to load from. Since every SVG document
	 * must be identified by a unique URL, Salamander provides a method to fake
	 * this for streams by defining it's own protocol - svgSalamander - for SVG
	 * documents without a formal URL.
	 *
	 * @param reader - A stream containing a valid SVG document
	 * @param name - <p>A unique name for this document. It will be used to
	 * construct a unique URI to refer to this document and perform resolution
	 * with relative URIs within this document.</p> <p>For example, a name of
	 * "/myScene" will produce the URI svgSalamander:/myScene.
	 * "/maps/canada/toronto" will produce svgSalamander:/maps/canada/toronto.
	 * If this second document then contained the href "../uk/london", it would
	 * resolve by default to svgSalamander:/maps/uk/london. That is, SVG
	 * Salamander defines the URI scheme svgSalamander for it's own internal use
	 * and uses it for uniquely identfying documents loaded by stream.</p> <p>If
	 * you need to link to documents outside of this scheme, you can either
	 * supply full hrefs (eg, href="url(http://www.kitfox.com/index.html)") or
	 * put the xml:base attribute in a tag to change the defaultbase URIs are
	 * resolved against</p> <p>If a name does not start with the character '/',
	 * it will be automatically prefixed to it.</p>
	 * @param forceLoad - if true, ignore cached diagram and reload
	 *
	 * @return - The URI that refers to the loaded document
	 */
	public URI loadSVG( Reader reader, String name, boolean forceLoad )
	{
		try
		{
			//System.err.println(url.toString());
			//Synthesize URI for this stream
			URI uri = getStreamBuiltURI( name );
			if (uri == null)
			{
				return null;
			}
			int attempts = 0;
			do
			{
				if (loadedDocs.containsKey( uri ) && !forceLoad)
				{
					return uri;
				}

				URI u = loadSVG(
					uri,
					new InputSource( reader ) );
				if (u != null)
				{
					return u;
				}
			}
			while (attempts < 10);

			return null;
		}
		finally
		{
			closeReaderQuietly( reader );
		}
	}

	/**
	 * Synthesize a URI for an SVGDiagram constructed from a stream.
	 *
	 * @param name - Name given the document constructed from a stream.
	 */
	public URI getStreamBuiltURI( String name )
	{
		if (name == null || name.length() == 0)
		{
			return null;
		}

		if (name.charAt( 0 ) != '/')
		{
			name = '/' + name;
		}

		try
		{
			//Dummy URL for SVG documents built from image streams
			return new URI( INPUTSTREAM_SCHEME, name, null );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	synchronized XMLReader getXMLReaderCached() throws SAXException
	{
		if (cachedReader == null)
		{
			cachedReader = XMLReaderFactory.createXMLReader();
		}
		return cachedReader;
	}

	//added this to prevent multiple reads of the document

	private static ExecutorService pool = null;

	public synchronized static ExecutorService getPool()
	{
		if (pool == null)
		{
			pool = Executors.newFixedThreadPool( 1 );
		}
		return pool;
	}

	public SVGLoaderTask getLoaderTask( URI xmlBase )
	{
		SVGLoaderTask slt = null;
		if (loaderQueue.containsKey( xmlBase ))
		{
			slt = loaderQueue.get( xmlBase );
		}
		else
		{
			slt = new SVGLoaderTask( this, xmlBase );
			loaderQueue.put(
				xmlBase,
				slt );

		}
		return slt;
	}

	public SVGLoaderTask getLoaderTask( URI xmlBase, InputSource in )
	{
		SVGLoaderTask slt = getLoaderTask( xmlBase );
		slt.setInput( in );
		return slt;
	}

	public Future< URI > startLoadTask( SVGLoaderTask toStart )
	{
		Future< URI > loadTask = toStart.start();
		return loadTask;
	}

	/**
	 * Synchronous (wait) load of the svg.
	 *
	 * @param xmlBase
	 * @return
	 */
	protected URI loadSVG( URI xmlBase )
	{
		return loadSVG(
			xmlBase,
			null );
	}

	protected URI loadSVG( URI xmlBase, InputSource is )
	{
		URI loaded = null;
		try
		{
			SVGLoaderTask slt = getLoaderTask( xmlBase );
			if (is != null)
			{
				slt.setInput( is );
			}
			Future< URI > taskresult = slt.getMyResult();
			if (taskresult == null)
			{
				taskresult = startLoadTask( slt );
			}
			loaded = taskresult.get(
				30,
				TimeUnit.SECONDS );

		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		catch (ExecutionException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		catch (TimeoutException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return loaded;
	}

	public static void main( String argv[] )
	{
		try
		{
			URL url = new URL( "svgSalamander", "localhost", -1, "abc.svg",
								new URLStreamHandler()
			{

				protected URLConnection openConnection( URL u )
				{
					return null;
				}

			} );
			//            URL url2 = new URL("svgSalamander", "localhost", -1, "abc.svg");

			//Investigate URI resolution
			URI uriA, uriB, uriC, uriD, uriE;

			uriA = new URI( "svgSalamander", "/names/mySpecialName", null );
			//            uriA = new URI("http://www.kitfox.com/salamander");
			//            uriA = new URI("svgSalamander://mySpecialName/grape");
			System.err.println( uriA.toString() );
			System.err.println( uriA.getScheme() );

			uriB = uriA.resolve( "#begin" );
			System.err.println( uriB.toString() );

			uriC = uriA.resolve( "tree#boing" );
			System.err.println( uriC.toString() );

			uriC = uriA.resolve( "../tree#boing" );
			System.err.println( uriC.toString() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public boolean isVerbose()
	{
		return verbose;
	}

	public void setVerbose( boolean verbose )
	{
		this.verbose = verbose;
	}

	/**
	 * Uses serialization to duplicate this universe.
	 */
	public SVGUniverse duplicate() throws IOException, ClassNotFoundException
	{
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream( bs );
		os.writeObject( this );
		os.close();

		ByteArrayInputStream bin = new ByteArrayInputStream( bs.toByteArray() );
		ObjectInputStream is = new ObjectInputStream( bin );
		SVGUniverse universe = (SVGUniverse) is.readObject();
		is.close();

		return universe;
	}

	public synchronized InputSource getInputSource( URI xmlBase )
	{
		try
		{
			URL toLoad = URLFactory.newURL( xmlBase.toASCIIString() );
			InputStream ist = getURLStream( toLoad );
			return new InputSource( createDocumentInputStream( ist ) );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				SVGUniverse.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return null;
	}

	public static URI resolve( URI toResolve, String fragment )
	{
		if (toResolve.getScheme()
			.equalsIgnoreCase(
				"jar" ))
		{
			try
			{
				String asString = toResolve.toString();
				String resolved = asString.replaceFirst(
					"#.*$",
					"" ) + fragment;
				return new URI( resolved );
			}
			catch (URISyntaxException ex)
			{
				Logger.getLogger(
					SVGUniverse.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
				return null;
			}
		}
		else
		{
			return toResolve.resolve( fragment );
		}
	}

}
