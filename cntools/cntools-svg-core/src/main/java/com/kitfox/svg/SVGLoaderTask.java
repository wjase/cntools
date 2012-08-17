/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class SVGLoaderTask implements Callable< URI >
{

	URI xmlBase;
	SVGUniverse univ;
	InputSource input = null;
	private Future< URI > myResult = null;

	public Future< URI > getMyResult()
	{
		return myResult;
	}

	ConcurrentLinkedQueue< SVGLoaderClient > doAfter = new ConcurrentLinkedQueue< SVGLoaderClient >();

	public void setDoAfter( SVGLoaderClient doAfter )
	{
		if (myResult != null && myResult.isDone())
		{
			try
			{
				doAfter.imageLoaded( myResult.get() );
			}
			catch (Exception ex)
			{
				Logger.getLogger(
					SVGLoaderTask.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
		else
		{
			this.doAfter.add( doAfter );
		}

	}

	public SVGLoaderTask( SVGUniverse univ, URI xmlBase )
	{
		if (xmlBase == null)
		{
			throw new RuntimeException();
		}
		this.xmlBase = xmlBase;
		this.univ = univ;
	}

	@Override
	public URI call() throws Exception
	{
		//System.out.println( "SVGLoad start"+xmlBase.toString());
		SVGLoader handler = new SVGLoader( xmlBase, univ, false );

		//Place this docment in the universe before it is completely loaded
		// so that the load process can refer to references within it's current
		// document
		univ.loadedDocs.put(
			xmlBase,
			handler.getLoadedDiagram() );
		XMLReader reader = null;
		InputSource is = null;
		try
		{
			is = input == null ? univ.getInputSource( xmlBase ) : input;
			// Parse the input
			reader = univ.getXMLReaderCached();
			reader.setEntityResolver(
					new EntityResolver()
					{

						@Override
						public InputSource resolveEntity( String publicId,
							String systemId )
						{
							//Ignore all DTDs
							return new InputSource( new ByteArrayInputStream( new byte[ 0 ] ) );
						}
					} );
			reader.setContentHandler( handler );
			reader.parse( is );

			for (SVGLoaderClient eachRunnable : doAfter)
			{
				eachRunnable.imageLoaded( xmlBase );
			}

			//            SAXParser saxParser = factory.newSAXParser();
			//            saxParser.parse(new InputSource(new BufferedReader(is)), handler);
			//System.out.println( "SVGLoad end"+xmlBase.toString());
			return xmlBase;
		}
		catch (SAXParseException sex)
		{
			System.err.println( "Error processing " + xmlBase );
			System.err.println( sex.getMessage() );
			sex.printStackTrace();

			univ.loadedDocs.remove( xmlBase );
			return null;
		}
		catch (Throwable t)
		{
			System.err.println( xmlBase.toASCIIString() );
			t.printStackTrace();
		}
		finally
		{

			SVGUniverse.closeStreamQuietly( is.getByteStream() );
			SVGUniverse.closeReaderQuietly( is.getCharacterStream() );
		}
		return null;
	}

	public synchronized Future< URI > start()
	{
		if (myResult == null)
		{
			myResult = SVGUniverse.getPool()
				.submit(
					this );
		}
		return myResult;
	}

	/**
	 * This is used when the URI is a fake one for a Reader or InputStream.
	 * For regular URLs, we should leave it to the thread task so we minimise 
	 * thread closures and race conditions.
	 * @param in 
	 */
	void setInput( InputSource in )
	{
		input = in;
	}
}
