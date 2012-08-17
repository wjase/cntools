package com.cybernostics.lib.persist.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.stream.ConsoleEchoInputStream;
import com.cybernostics.lib.io.stream.StreamPipe;

/**
 * The XMLPropertyMap Encoded manages the serialisation of a Map into and out of
 * XML.
 * 
 * @author jasonw
 * 
 */
public class XMLPropertyMapEncoder
{
	public enum EntityType
	{
		COMPLETE, // write the xml with the xml header
		FRAGMENT
		// don't write the xml header
	}

	public static String encodeMap( Map< String, String > props )
	{
		return encodeMap(
			props,
			EntityType.COMPLETE );
	}

	public static String encodeMap( Map< String, String > props, EntityType type )
	{
		StringBuilder sb = new StringBuilder();
		if (type == EntityType.COMPLETE)
		{
			sb.append( "<?xml version = \"1.0\" ?>\n" );
		}
		sb.append( "<properties>\n" );
		for (String eachKey : props.keySet())
		{
			sb.append( "<property>\n" );
			sb.append( "<name>" );
			sb.append( eachKey );
			sb.append( "</name>\n" );
			sb.append( "<value>" );
			sb.append( props.get( eachKey ) );
			sb.append( "</value>\n" );
			sb.append( "</property>\n" );

		}
		sb.append( "</properties>\n" );

		return sb.toString();

	}

	public static Map< String, String > getXMLProperties( final InputStream is )
	{
		final StringBuffer bytesRead = new StringBuffer();

		// Uncomment this line (and comment out the following to get the stream 
		// echoed as it is read...
		//InputStream resultStream = new ConsoleEchoInputStream(new BufferedInputStream( is ));
		InputStream resultStream = new BufferedInputStream( is );

		resultStream.mark( 4096 );

		Map< String, String > properties = new TreeMap< String, String >();

		if (resultStream != null)
		{
			// ConsoleEchoInputStream cis = new ConsoleEchoInputStream(is);

			// Create a factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware( true );

			// Turn on validation
			// factory.setValidating( true );

			// Use the factory to create a builder
			DocumentBuilder builder;
			try
			{
				builder = factory.newDocumentBuilder();
				Document doc = null;

				doc = builder.parse( StreamPipe.getDebuggableStream( resultStream ) );
				//displayInformation( doc );

				// Get a list of all <property> elements in the document
				NodeList list = doc.getElementsByTagName( "property" );
				if (list != null && ( list.getLength() > 0 ))
				{
					for (int i = 0; i < list.getLength(); ++i)
					{
						// Get name and value elements
						Element propertyElement = (Element) list.item( i );
						Node nameNode = propertyElement.getElementsByTagName(
							"name" )
							.item(
								0 );
						Text nameText = (Text) nameNode.getFirstChild();

						Node valueNode = propertyElement.getElementsByTagName(
							"value" )
							.item(
								0 );

						Node valueNodeContents = valueNode.getFirstChild();

						if (( valueNode.getChildNodes()
							.getLength() == 1 )
							&& ( valueNodeContents instanceof Text ))
						{
							Text valueText = (Text) valueNodeContents;
							// Add them to the map
							properties.put(
								nameText.getData(),
								valueText.getData() );
						}
						else
						{
							properties.put(
								nameText.getData(),
								nodeAsString( valueNode.getChildNodes() ) );
						}
					}
				}
				else
				{
					resultStream.reset();
					bytesRead.append( ConsoleEchoInputStream.dumpToString( resultStream ) );
					properties.put(
						"Error",
						"Unexpected content:\nStream:\n" + bytesRead.toString() );

				}

			}
			catch (Exception e)
			{
				try
				{
					resultStream.reset();
				}
				catch (IOException ex)
				{
					Logger.getLogger(
						XMLPropertyMapEncoder.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}
				// UnhandledExceptionManager.handleException( e );
				bytesRead.append( ConsoleEchoInputStream.dumpToString( resultStream ) );
				properties.put(
					"Error",
					String.format(
						"ErrorMessage:%s\nStream Data:%s\n",
						e.getLocalizedMessage(),
						bytesRead.toString() ) );
			}

		}
		else
		{
			properties.put(
				"Error",
				"Null stream!" );
		}

		return properties;
	}

	private static Transformer myXFormer = null;

	private static Transformer getTransformer()
	{
		if (myXFormer == null)
		{
			TransformerFactory tFactory = TransformerFactory.newInstance();
			try
			{
				myXFormer = tFactory.newTransformer();
			}
			catch (TransformerConfigurationException e)
			{
				UnhandledExceptionManager.handleException( e );
			}
		}
		return myXFormer;
	}

	private static String nodeAsString( NodeList toConvert )
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < toConvert.getLength(); ++i)
		{
			sb.append( nodeAsString( toConvert.item( i ) ) );
		}

		return sb.toString();
	}

	private static String xmlheader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

	private static String nodeAsString( Node toConvert )
	{
		// Use a Transformer for output
		Transformer transformer = getTransformer();

		DOMSource source = new DOMSource( toConvert ); // assumes
		// a
		// single
		// child or
		// list
		// wrapper

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		StreamResult result = new StreamResult( bos );
		try
		{
			transformer.transform(
				source,
				result );
		}
		catch (TransformerException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		int headerLength = xmlheader.length();

		StringBuilder sb = new StringBuilder( bos.toString() );

		int pos = -1;
		if (( pos = sb.indexOf( xmlheader ) ) >= 0)
		{
			sb.delete(
				pos,
				pos + headerLength );
			//trim whitespace at start
			while (( sb.length() > 0 ) && sb.charAt( 0 ) == ' ')
			{
				sb.deleteCharAt( 0 );
			}
		}
		return sb.toString();

	}

	private static void showNode( Element theElement, String prefix )
	{
		System.out.println( prefix + "Tag Name = " + theElement.getTagName() );
		System.out.println( prefix + "Node Type = " + theElement.getNodeType() );
		System.out.println( prefix + "Element has Attributes = "
			+ theElement.hasAttributes() );

		NodeList nodeList = theElement.getChildNodes();

		System.out.println( prefix + "There are " + nodeList.getLength()
			+ " child nodes \n" );

		for (int i = 0; i < nodeList.getLength(); i++)
		{
			System.out.println( prefix + "Node " + i + ":\n" );
			Node node = nodeList.item( i ); // Get the next node

			if (node instanceof Element)
			{
				System.out.println( prefix + "NodeName = " + node.getNodeName() );
				showNode(
					(Element) node,
					prefix + "   " );
			}

			if (node instanceof Text)
			{
				Text txtNode = (Text) node;
				System.out.println( prefix + "Text content = "
					+ txtNode.getData() );
			}

		}

	}

	@SuppressWarnings("unused")
	private static void displayInformation( Document document )
	{
		System.out.println( "Displaying Document Details...." );

		showNode(
			document.getDocumentElement(),
			"" );

	}

}