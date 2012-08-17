package com.cybernostics.lib.net;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CancellationException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.persist.xml.XMLPropertyMapEncoder;

/**
 * Makes an HTTP post request and parses the output to return an XML
 * InputStream.
 * 
 * It inherits from SwingWorker to allow it to occur asynchronously in a gui
 * environment
 * 
 * @author jasonw
 * 
 */
public class RESTQuery extends SwingWorker< InputStream, Integer >
{

	public static void main( String[] args ) throws MalformedURLException
	{

		// Create a frame with a progress bar button to initiate a query and
		// text pane to display the query results.
		JFrame jf = new JFrame( "TEST Test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setSize(
			600,
			400 );
		JPanel thePanel = new JPanel();

		//DesignGridLayout dgl = new DesignGridLayout( thePanel );
		final JProgressBar urlProgress = new JProgressBar();
		thePanel.add( urlProgress );

		final JButton jbut = new JButton( "Fetch" );
		thePanel.add( jbut );
		final JTextPane jtp = new JTextPane();
		jtp.setEditable( false );
		thePanel.add( jtp );
		jf.getContentPane()
			.add(
				thePanel );
		jf.setVisible( true );

		final PropertyChangeListener taskDone = new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				if (evt.getPropertyName()
					.equals(
						"state" ) && evt.getNewValue()
					.toString()
					.equalsIgnoreCase(
						"done" ))
				{
					RESTQuery udf = (RESTQuery) evt.getSource();
					if (udf.isDone())
					{

						try
						{
							StringBuffer sb = new StringBuffer();
							try
							{
								Map< String, String > props = XMLPropertyMapEncoder.getXMLProperties( udf.get() );

								for (String eachKey : props.keySet())
								{
									sb.append( eachKey + "="
										+ props.get( eachKey ) + "\n" );
								}

							}
							catch (CancellationException ce)
							{
								sb.append( "Cancelled" );
							}

							jtp.setText( sb.toString() );
							urlProgress.setIndeterminate( false );
							jbut.setEnabled( true );
						}
						catch (Exception e)
						{
							UnhandledExceptionManager.handleException( e );
						}

					}
				}
			}
		};

		jbut.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				final JButton jb = (JButton) e.getSource();
				jb.setEnabled( false );
				urlProgress.setIndeterminate( true );

				// Create a new REST query
				RESTQuery udf = null;
				try
				{
					Map< String, String > sessionCookies = CookiePropertyParser.getCookies();
					udf = new RESTQuery( new CustomCookieHttpConnection2( new URL(
						"http://127.0.0.1/joeymain/webstart/sessinit.php" ),
						sessionCookies,
						null ) );

				}
				catch (MalformedURLException e1)
				{
					UnhandledExceptionManager.handleException( e1 );
				}
				if (udf != null)
				{
					udf.execute();
					udf.addPropertyChangeListener( taskDone );

				}

			}
		} );
	}

	/**
	 * The arguments provided to the remote service
	 * 
	 */
	Map< String, String > queryArguments;

	/**
	 * The address of the service to query
	 */
	ConnectionSource connection;

	public RESTQuery(
		ConnectionSource connSource,
		Map< String, String > properties )
	{
		this.queryArguments = properties;
		connection = connSource;
	}

	public RESTQuery( ConnectionSource connSource, String... properties )
	{
		this.queryArguments = new TreeMap< String, String >();
		boolean addNow = false;
		String propName = "";
		for (String eachVal : properties)
		{
			if (!addNow)
			{
				propName = eachVal;
			}
			else
			{
				this.queryArguments.put(
					propName,
					eachVal );
			}
			addNow = !addNow;
		}
		this.connection = connSource;
	}

	@Override
	protected InputStream doInBackground() throws Exception
	{
		try
		{
			URLConnection yc = this.connection.getConnection();

			StringBuffer sb = new StringBuffer();
			boolean firstOne = true;
			for (String eachKey : queryArguments.keySet())
			{
				if (!firstOne)
				{
					sb.append( "&" );
				}
				sb.append( eachKey + "=" + queryArguments.get( eachKey ) );
				firstOne = false;
			}
			String data = sb.toString();

			yc.setDoOutput( true );
			OutputStreamWriter writer = new OutputStreamWriter( yc.getOutputStream() );

			// write parameters
			writer.write( data );
			writer.flush();

			yc.setReadTimeout( 15000 );

			return yc.getInputStream();

		}
		catch (Exception e)
		{

			// If there is an error or exception code it in XML and return the
			// inputstream
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter( sw );
			e.printStackTrace( pw );
			Map< String, String > errorMap = new TreeMap< String, String >();
			errorMap.put(
				"errortext",
				sw.toString() );
			return new ByteArrayInputStream( XMLPropertyMapEncoder.encodeMap(
				errorMap )
				.getBytes() );

		}

	}

	@Override
	protected void done()
	{
		super.done();
	}

	@Override
	protected void process( List< Integer > chunks )
	{
		super.process( chunks );
	}

}
