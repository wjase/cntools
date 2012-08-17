package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.lib.gui.ScreenRelativeDimension;
import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.dialogs.SplashDialog;

/**
 * Splash window for your application.
 * 
 * @author jasonw
 * 
 */
public class SplashDialogExample
{


	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "SplashTestDummy" );

		System.out.println( System.getProperty( "user.dir" ) );

		JPanel jp = new JPanel( new BorderLayout() );

		jp.add( new JButton( "<html><h1>My Splash window!</h1><html>" ), BorderLayout.CENTER );

		jp.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ),
			BorderFactory.createEmptyBorder( 30, 30, 30, 30 ) ) );
		SplashDialog sd = new SplashDialog( jf, jp );
		sd.closeAfter( 5000 );

		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		jf.setSize( new ScreenRelativeDimension( 0.5f, 0.5f ) );

		try
		{
			Thread.sleep( 5000 );
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}

		jf.setVisible( true );

	}

}
