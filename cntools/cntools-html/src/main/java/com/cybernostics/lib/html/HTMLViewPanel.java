package com.cybernostics.lib.html;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.io.JarUrlFix;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xml.sax.SAXException;

public class HTMLViewPanel extends JPanel
{

	private static Document load( URI uRI )
	{
		try
		{
			String urlStr = uRI.toASCIIString();

			XMLResource res = XMLResource.load( JarUrlFix.getURLStream( uRI.toURL() ) );
			Document doc = res.getDocument();
			doc.setDocumentURI( urlStr );
			return doc;
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				HTMLViewPanel.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
			throw new RuntimeException( ex );
		}

	}

	public static void setStyleAttr( Document doc,
		String id,
		String styleAttr,
		String val )
	{
		Element toUpdate = doc.getElementById( id );
		String attr = toUpdate.getAttribute( "style" );
		String newStyle = attr.replaceAll(
			styleAttr + ":[^;]+;",
			"" ) + styleAttr + ":" + val + ";";// + ;
		toUpdate.setAttribute(
			"style",
			newStyle );

	}

	private DocumentDOMFilter filter = null;

	public void setFilter( DocumentDOMFilter filter )
	{
		this.filter = filter;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 5211795199555153780L;

	JScrollPane jsp;

	Dimension dLastKnownSize = null;

	private boolean fitWidth = true;

	public boolean isFitWidth()
	{
		return fitWidth;
	}

	public void setFitWidth( boolean fitWidth )
	{
		this.fitWidth = fitWidth;
		jsp.setHorizontalScrollBarPolicy( fitWidth ? ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
			: ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
	}

	public HTMLViewPanel()
	{
		jsp = new FSScrollPane( xmlPanel );
		jsp.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		jsp.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		// setLayout( new GridLayout() );
		// add( jsp );

		setLayout( new GridLayout() );
		add( jsp );

		// This was to force images with relative sizes to be resized
		// TODO Remove when no longer necessary
		xmlPanel.addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentResized( ComponentEvent e )
			{
				refresh();
			}

		} );
		xmlPanel.setUI( null );
	}

	private void panelSizeChanged()
	{
		GUIEventThread.runLater( new Runnable()
		{

			@Override
			public void run()
			{
				if (currentDoc != null)
				{
					setDocument(
						currentDoc,
						null );
				}
			}

		} );

	}

	protected XHTMLPanel xmlPanel = new XHTMLPanel();

	private XhtmlNamespaceHandler nsh = new XhtmlNamespaceHandler();

	// Applies the document to the underlying panel having been preprocessed
	// by a document filter if one is set. This can be used to show elements,
	// or hide elements - or perform whatever DOM actions are required.
	public void setDocument( Document doc, String root )
	{
		if (this.filter != null)
		{
			this.filter.process( doc );
		}

		if (root == null)
		{
			root = doc.getDocumentURI();
		}
		currentDoc = doc;
		xmlPanel.setDocument(
			doc,
			root,
			nsh );
	}

	public void setDocument( String str, String root )
		throws ParserConfigurationException, SAXException, IOException
	{

		ByteArrayInputStream bis = new ByteArrayInputStream( str.getBytes() );
		Document dom = XMLResource.load(
			HTMLCleaner.get()
				.getFilteredXMTMLStream(
					bis ) )
			.getDocument();
		setDocument(
			dom,
			root );

	}

	public void setDocument( URI doc ) throws ParserConfigurationException,
		SAXException, IOException
	{
		setDocument(
			HTMLViewPanel.load( doc ),
			doc.toString() );
	}

	public void refresh()
	{
		if (currentDoc != null)
		{
			setDocument(
				currentDoc,
				null );
		}
	}

	public void reload()
	{
		try
		{
			setDocument(
				HTMLViewPanel.load( new URI( currentDoc.getDocumentURI() ) ),
				null );
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				HTMLViewPanel.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	Document currentDoc = null;

	public XHTMLPanel getHtmlPanel()
	{
		return xmlPanel;
	}

	@Override
	public void setSize( Dimension d )
	{
		super.setSize( d );
	}

	@Override
	public void setSize( int width, int height )
	{
		super.setSize(
			width,
			height );
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		super.setBounds(
			x,
			y,
			width,
			height );
	}

	@Override
	public void setBounds( Rectangle r )
	{
		super.setBounds( r );
	}

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "HTML ViewPanel Example Usage" );
		jf.setSize(
			640,
			480 );
		jf.getContentPane()
			.setLayout(
				new BorderLayout() );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		final HTMLViewPanel viewerPanel = new HTMLViewPanel();
		jf.getContentPane()
			.add(
				viewerPanel,
				BorderLayout.CENTER );

		JButton refresh = new JButton( "Refresh" );
		jf.getContentPane()
			.add(
				refresh,
				BorderLayout.SOUTH );

		refresh.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				viewerPanel.reload();
			}
		} );

		try
		{
			URI base = new URI( "file:///c:/temp/fairy/fairy.html" );
			Document doc = HTMLViewPanel.load( base );
			DomManipulator.setStyleAttr(
				DomManipulator.byClass(
					doc,
					"span",
					"tohide" ),
				"display",
				"none" );
			viewerPanel.setDocument(
				doc,
				base.toString() );
			//viewerPanel.setDocument( new URI( "file:///c:/temp/old2.html" ) );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		jf.setVisible( true );

	}

	public void setUserCallback( UserAgentCallback callbackAgent )
	{
		xmlPanel.getSharedContext()
			.setUserAgentCallback(
				callbackAgent );
	}

	private void updateAttr( String id, String styleAttr, String val )
	{
		Element toUpdate = xmlPanel.getDocument()
			.getElementById(
				id );
		String attr = toUpdate.getAttribute( "style" );
		String newStyle = attr.replaceAll(
			styleAttr + ":[^;]+;",
			"" ) + styleAttr + ":" + val + ";";// + ;

		toUpdate.setAttribute(
			"style",
			newStyle );
		xmlPanel.revalidate();
	}

}
