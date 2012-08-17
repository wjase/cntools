package com.cybernostics.examples.lib.gui;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.panel.BoxPanel;
import com.cybernostics.lib.html.HTMLViewPanel;
import com.cybernostics.lib.html.TidyHtmlUserAgent;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


/**
 *
 * @author jasonw
 */
public class ShowHtml extends JPanel
{

	HTMLViewPanel viewer = null;

	JTextField jtf = null;

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "html test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setContentPane( new ShowHtml() );
		GUIEventThread.show( jf );
	}

	public ShowHtml()
	{
		setLayout( new BorderLayout() );
		viewer = new HTMLViewPanel();
		viewer.setUserCallback( new TidyHtmlUserAgent() );
		add( viewer, BorderLayout.CENTER );
		BoxPanel buttons = BoxPanel.get( BoxPanel.Direction.HORIZONTAL );
        
		jtf = new JTextField();
		JButton jb = new JButton( "Browse" );
		buttons.addStrut( 15 );
		buttons.add( jtf );
		buttons.addStrut( 15 );
		buttons.add( jb );
		buttons.addStrut( 15 );
		buttons.setBorder( BorderFactory.createEmptyBorder( 15, 15, 15, 15 ) );
		add( buttons, BorderLayout.SOUTH );
		jb.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				JFileChooser jfc = new JFileChooser();

				if (jfc.showOpenDialog( ShowHtml.this ) == JFileChooser.APPROVE_OPTION)
				{
					{
						try
						{
							URI selected = jfc.getSelectedFile().toURI();
							viewer.setDocument( selected );
							jtf.setText( selected.toString() );
						}
						catch (Exception ex)
						{
							Logger.getLogger( ShowHtml.class.getName() ).log( Level.SEVERE, null, ex );
						}
					}
				}
			}

		} );

		jtf.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				try
				{
					URI selected = new URI( jtf.getText() );
					viewer.setDocument( selected );
				}
				catch (Exception ex)
				{
					Logger.getLogger( ShowHtml.class.getName() ).log( Level.SEVERE, null, ex );
				}
			}

		} );
	}

}
