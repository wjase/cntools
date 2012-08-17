package com.cybernostics.lib.svg;

import com.cybernostics.lib.io.stream.AtomicUrlInputStreamProcessor;
import com.cybernostics.lib.io.stream.InputStreamOperation;
import com.cybernostics.lib.io.stream.StreamPipe;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.elements.*;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.*;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

/**
 *
 * @author jasonw
 */
public class SVGWriter
{

	private static Map< Class< ? >, String > nameMap = new HashMap< Class< ? >, String >();

	/**
	 * This maps Salamander class names to svg tag names, where the
	 * class name doesn't match the SVG format tag.
	 *
	 * @param element
	 * @return
	 */
	public static String getElementTag( Class< ? > element )
	{
		if (nameMap.isEmpty())
		{
			// we only include the ones which arent simply
			// lowercase versions of the class name
			nameMap.put(
				SVGRoot.class,
				"svg" );
			nameMap.put(
				Group.class,
				"g" );
			nameMap.put(
				ImageSVG.class,
				"image" );
		}
		String className = nameMap.get( element );
		if (className == null)
		{
			String simpleName = element.getSimpleName()
				.toLowerCase();
			nameMap.put(
				element,
				simpleName );
			return simpleName;
		}
		return className;
	}

	static Pattern filePat = Pattern.compile( "([^/]+)\\..+$" );

	/**
	 * Writes a zip file together with all the associated bits and pieces it
	 * refers to. (images)
	 *
	 * @param toWrite
	 * @param drawingName
	 */
	public static void writeToJarStream( SVGDiagram d,
		OutputStream toWrite,
		String drawingName,
		URLRewriter xformer )
	{
		Matcher m = filePat.matcher( drawingName );
		m.find();
		String fileOnly = m.group( 1 ) + ".svg";
		JarOutputStream jos = null;
		try
		{
			jos = new JarOutputStream( toWrite );
			jos.putNextEntry( new ZipEntry( fileOnly ) );
			OutputStreamWriter osw = new OutputStreamWriter( jos );
			writeSVGFIle(
				d,
				new PrintWriter( osw ),
				xformer );
			osw.flush();
			jos.closeEntry();
			writeImagesToJarStream(
				jos,
				xformer,
				d.getXMLBase() );

		}
		catch (IOException ex)
		{
			Logger.getLogger(
				SVGWriter.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		finally
		{
			if (jos != null)
			{
				try
				{
					jos.flush();
					jos.close();
				}
				catch (IOException ex)
				{
					Logger.getLogger(
						SVGWriter.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}
			}

		}
	}

	/**
	 * For each of the pairs in the URLRewriter, it reads from the source location
	 * and packs it into our destination zip.
	 *
	 * @param toWrite
	 * @param xformer
	 * @param sourceBase
	 */
	public static void writeImagesToJarStream( final JarOutputStream toWrite,
		URLRewriter xformer,
		URI sourceBase )
	{
		Map< String, String > urls = xformer.getRewrites();
		for (Entry< String, String > eachUrlPair : urls.entrySet())
		{
			try
			{
				toWrite.putNextEntry( new ZipEntry( eachUrlPair.getValue() ) );

				String fromURL = eachUrlPair.getKey();
				URL readURL = null;
				if (fromURL.indexOf( ":" ) == -1)
				{
					readURL = new URL( sourceBase.toURL(), eachUrlPair.getKey() );
				}
				else
				{
					readURL = new URL( eachUrlPair.getKey() );
				}

				AtomicUrlInputStreamProcessor.execute(
					readURL,
					new InputStreamOperation()
				{

					@Override
					public void process( InputStream is ) throws IOException
					{
						// read the data into the new entry
						StreamPipe.copyInputToOutput(
							is,
							toWrite );
						// close the jar entry
						toWrite.closeEntry();
					}

				} );

			}
			catch (MalformedURLException ex)
			{
				Logger.getLogger(
					SVGWriter.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			catch (IOException ex1)
			{
				Logger.getLogger(
					SVGWriter.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex1 );
			}

		}
	}

	public static URL addImageToJar( URL image,
		boolean includePath,
		JarOutputStream jos,
		URLRewriter xformer )
	{
		return null;

	}

	/**
	 * Writes out the SVG element as a file which can be read by loadSVG.
	 *
	 * @param e - root element
	 * @param pw
	 */
	public static void writeSVGFIle( SVGDiagram d,
		PrintWriter pw,
		URLRewriter xformer )
	{
		pw.println( "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" );
		writeElement(
			d.getRoot(),
			pw,
			xformer );
		pw.flush();
	}

	static Pattern pFileBaseName = Pattern.compile( "([^/]+)\\.\\w+$" );

	/**
	 * Assumes the svg is in a jar file
	 *
	 * @param jarURI - a url pointing to the jar
	 * eg file:///var/usr/my/path/to/jar/MyDrawing.svgz
	 * @param entryName
	 * @return
	 */
	public static URI getDrawingEntry( URI jarURI )
	{
		try
		{
			String jarURIString = jarURI.toASCIIString();
			Matcher m = pFileBaseName.matcher( jarURI.getPath() );
			m.find();
			String drawingBaseName = m.group( 1 );
			URI jarEntry = new URI( "jar:" + jarURIString + "!/"
				+ drawingBaseName + ".svg" );

			return jarEntry;
		}
		catch (URISyntaxException ex)
		{
			Logger.getLogger(
				SVGWriter.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return null;

	}

	public static void writeElement( SVGElement e,
		PrintWriter pw,
		URLRewriter xformer )
	{
		String tag = getElementTag( e.getClass() );
		pw.printf(
			"<%s ",
			tag );

		if (tag.equalsIgnoreCase( "svg" ))
		{
			pw.print( " xmlns:xlink=\"http://www.w3.org/1999/xlink\" " );
		}

		Set< IStringAttribute > toSort = new TreeSet< IStringAttribute >( new Comparator< IStringAttribute >()
		{

			@Override
			public int compare( IStringAttribute o1, IStringAttribute o2 )
			{
				return o1.getName()
					.compareTo(
						o2.getName() );
			}
		} );
		toSort.addAll( e.getAttributes() );// <IStringAttribute>();

		for (IStringAttribute eachAttribute : toSort)
		{
			if (eachAttribute.getName()
				.startsWith(
					"xlink" ))
			{
				pw.print( String.format(
					" %s=\"%s\" ",
					eachAttribute.getName(),
														xformer.transformURL( eachAttribute.getStringValue() ) ) );
			}
			else
			{
				pw.print( eachAttribute.asSVGString() );
			}

		}

		//        StringBuilder styleString = new StringBuilder();
		//        Set<?> attrs = e.getInlineAttributes();
		//        for ( Object eachAttr : new ArrayList<Object>( new TreeSet<Object>( attrs ) ) )
		//        {
		//            IStringAttribute sa = e.getStyleAbsolute( eachAttr.toString() );
		//            if ( sa != null && sa.isSet() )
		//            {
		//                styleString.append( String.format( "%s:%s;", sa.getName(), sa.getStringValue() ) );
		//            }
		//        }
		//
		//        Set<?> PresAttrs = e.getPresentationAttributes();
		//        for ( Object eachAttr : new ArrayList<Object>( new TreeSet<Object>( PresAttrs ) ) )
		//        {
		//            IStringAttribute sa = e.getPresAbsolute( eachAttr.toString() );
		//            if ( sa != null )
		//            {
		//                String name = sa.getName();
		//                if ( !name.equals( "style" ) ) // already done style
		//                {
		//                    String value = sa.getStringValue();
		//                    if ( value != null )
		//                    {
		//                        if ( name.startsWith( "xlink" ) )
		//                        {
		//                            value = xformer.transformURL( value );
		//                        }
		//                        pw.print( String.format( "%s=\"%s\" ", sa.getName(), value ) );
		//
		//                    }
		//                }
		//            }
		//        }
		//
		//        if ( styleString.length() > 0 )
		//        {
		//            pw.printf( "style=\"%s\" ", styleString.toString() );
		//        }

		if (e instanceof Tspan)
		{
			Tspan ts = (Tspan) e;
			pw.printf(
				">%s</%s>\n",
				ts.getText(),
				tag );
		}
		else
		{
			List< ? > children = new ArrayList< Object >();
			if (e.getChildren(
				children )
				.isEmpty())
			{
				pw.printf( "/>\n" );
			}
			else
			{
				pw.printf( ">" );
				for (Object eachChild : children)
				{
					writeElement(
						(SVGElement) eachChild,
						pw,
						xformer );
				}
				pw.printf(
					"</%s>\n",
					tag );
			}

		}

	}

}
