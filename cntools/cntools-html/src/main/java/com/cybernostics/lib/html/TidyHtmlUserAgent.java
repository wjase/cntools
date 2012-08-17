package com.cybernostics.lib.html;

import com.cybernostics.lib.io.stream.ConsoleEchoInputStream;
import com.cybernostics.lib.io.stream.StringToStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.NaiveUserAgent;

/**
 *
 * @author jasonw
 */
public class TidyHtmlUserAgent extends NaiveUserAgent
{

	@Override
	public XMLResource getXMLResource( String uri )
	{
		if (!uri.endsWith( "html" ))
		{
			return super.getXMLResource( uri );
		}
		XMLResource retVal = null;
		String preTidy = null;
		try
		{
			preTidy = StringToStream.contentsAsString( new URL( uri ) );
			InputStream tidyOutput = HTMLCleaner.get()
				.getFilteredXMTMLStream(
					preTidy.getBytes() );
			retVal = XMLResource.load( ConsoleEchoInputStream.pipe( tidyOutput ) );
			return retVal;
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				TidyHtmlUserAgent.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
			Logger.getLogger(
				TidyHtmlUserAgent.class.getName() )
				.log(
					Level.SEVERE,
					preTidy );
		}

		return retVal;
	}

}
