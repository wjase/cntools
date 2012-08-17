package com.cybernostics.lib.persist.xml;

import java.beans.ExceptionListener;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.SAXParseException;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * Object for saving and loading from compressed XML
 * 
 * The XML object stream is saved in the jar as "doc"
 * 
 * 2 different schemes are supported:
 * 
 * i) Object serialisation based on the XMLEncoder ii) Attribute-based
 * serialisation JAXB
 * 
 * @author jasonw
 * 
 */
public class ObjectSerialiser
{

	public static final String DOCNAME = "doc";
	private static ArrayList< Class< ? >> classes = new ArrayList< Class< ? >>();
	private static ArrayList< PersistenceDelegate > delegates = new ArrayList< PersistenceDelegate >();

	public static void addDelegate( Class< ? > theClass,
		PersistenceDelegate theDelegate )
	{
		classes.add( theClass );
		delegates.add( theDelegate );
	}

	public static Object jaxbLoad( Class< ? > rootClass, String savedPath )
		throws IOException
	{
		Object ObjectAsRead = null;
		JarFile theJar = null;
		try
		{
			if (savedPath == null)
			{
				throw new IOException( "savedPath was Null" );
			}
			File testExists = new File( savedPath );
			if (!testExists.exists())
			{
				return null;
			}

			theJar = new JarFile( savedPath );

			JarEntry theXMLFile = theJar.getJarEntry( DOCNAME );
			ObjectAsRead = jaxbReadObjectAsXML(
				rootClass,
				theJar.getInputStream( theXMLFile ) );

		}
		finally
		{
			if (theJar != null)
			{
				theJar.close();
			}

		}

		return ObjectAsRead;
	}

	public static Object jaxbReadObjectAsXML( Class< ? > rootClass,
		InputStream is ) throws IOException
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance( rootClass );
			Unmarshaller um = jc.createUnmarshaller();
			return um.unmarshal( is );
		}
		catch (JAXBException ex)
		{
			throw new IOException( ex );
		}

	}

	public static void jaxbSave( Object objToSave, String pathLocation )
		throws IOException
	{
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		JarOutputStream jos = null;
		try
		{
			fos = new FileOutputStream( pathLocation );

			bos = new BufferedOutputStream( fos );

			jos = new JarOutputStream( bos );

			jos.putNextEntry( new ZipEntry( DOCNAME ) );

			jaxbWriteObjectAsXML(
				objToSave,
				jos );
		}
		finally
		{
			if (jos != null)
			{
				jos.close();
			}
			if (bos != null)
			{
				bos.close();
			}
			if (fos != null)
			{
				fos.close();
			}
		}
	}

	public static String jaxbAsXMLStringNoHeader( Object objToSave )
	{
		StringBuilder xmlString = new StringBuilder( jaxbAsXMLString( objToSave ) );

		if (xmlString.substring(
			0,
			5 )
			.equals(
				"<?xml" ))
		{
			int closingBit = xmlString.indexOf( "?>" );
			xmlString.delete(
				0,
				closingBit + 2 );
		}
		return xmlString.toString();
	}

	public static String jaxbAsXMLString( Object objToSave )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			ObjectSerialiser.jaxbWriteObjectAsXML(
				objToSave,
				bos );
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
			return null;
		}

		return bos.toString();
	}

	public static Object jaxbFromXMLString( Class< ? > rootClass, String xmlData )
		throws IOException
	{
		return jaxbReadObjectAsXML(
			rootClass,
			new ByteArrayInputStream( xmlData.getBytes() ) );

	}

	public static void jaxbWriteObjectAsXML( Object objToSave, OutputStream jos )
		throws IOException
	{
		JAXBContext jc;
		try
		{
			jc = JAXBContext.newInstance( objToSave.getClass() );
			Marshaller m = jc.createMarshaller();
			m.setProperty(
				"jaxb.formatted.output",
				true );
			m.marshal(
				objToSave,
				jos );
			jos.flush();
		}
		catch (JAXBException e)
		{
			UnhandledExceptionManager.handleException( e );

			IOException ex = new IOException( e );
			throw ex;

		}

	}

	public static Object load( String savedPath ) throws IOException
	{
		Object ObjectAsRead = null;
		JarFile theJar = null;
		try
		{
			File testExists = new File( savedPath );
			if (!testExists.exists())
			{
				return null;
			}

			theJar = new JarFile( savedPath );

			JarEntry theXMLFile = theJar.getJarEntry( DOCNAME );
			ObjectAsRead = readObjectAsXML( theJar.getInputStream( theXMLFile ) );
		}
		finally
		{
			if (theJar != null)
			{
				theJar.close();
			}
		}

		return ObjectAsRead;
	}

	public static Object readObjectAsXML( InputStream is )
	{
		XMLDecoder xdec = new XMLDecoder( is, null, new ExceptionListener()
		{

			@Override
			public void exceptionThrown( Exception e )
			{
				if (e instanceof SAXParseException)
				{
					// no such method will be thrown for public attributes
					// which have been removed from the format.
					if (!( e instanceof NoSuchMethodException ))
					{
						System.err.println( "Exception while reading xml" );
						UnhandledExceptionManager.handleException( e );
						SAXParseException saxE = (SAXParseException) e;
						System.out.print( "Line: " + saxE.getLineNumber() );
						System.out.print( "Column:" + saxE.getColumnNumber() );
					}
				}
			}
		} );

		return xdec.readObject();

	}

	public static void save( Object objToSave, String pathLocation )
		throws IOException
	{
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		JarOutputStream jos = null;
		try
		{
			fos = new FileOutputStream( pathLocation );

			bos = new BufferedOutputStream( fos );

			jos = new JarOutputStream( bos );

			jos.putNextEntry( new ZipEntry( DOCNAME ) );

			writeObjectAsXML(
				objToSave,
				jos );
		}
		finally
		{
			if (jos != null)
			{
				jos.close();
			}
			if (bos != null)
			{
				bos.close();
			}
			if (fos != null)
			{
				fos.close();
			}
		}
	}

	/**
	 * @param objToSave
	 * @param jos
	 */
	public static void writeObjectAsXML( Object objToSave, OutputStream jos )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// // Create XML encoder.
		XMLEncoder xenc = new XMLEncoder( bos );

		for (int index = 0; index < classes.size(); ++index)
		{
			xenc.setPersistenceDelegate(
				classes.get( index ),
				delegates.get( index ) );
		}

		xenc.setExceptionListener( new ExceptionListener()
		{

			@Override
			public void exceptionThrown( Exception e )
			{
				UnhandledExceptionManager.handleException( e );
			}
		} );

		//
		xenc.writeObject( objToSave );

		//
		xenc.flush();
		xenc.close();

		try
		{
			jos.write(
				bos.toByteArray(),
				0,
				bos.size() );
			jos.flush();
		}
		catch (IOException e1)
		{
			UnhandledExceptionManager.handleException( e1 );
		}
	}
}
