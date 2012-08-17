package com.cybernostics.lib.gui.dialogs;

import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Splash window for your application.
 * 
 * @author jasonw
 * 
 */
public class SplashDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6984923553769085108L;

	public SplashDialog( JFrame owner, JPanel contents )
	{
		super( owner );
		getContentPane().setLayout(
			new BorderLayout() );
		getContentPane().add(
			contents,
			BorderLayout.CENTER );
		setUndecorated( true );
		setAlwaysOnTop( true );
		pack();
		setLocationRelativeTo( null );
		setResizable( false );
		setVisible( true );

	}

	public void closeAfter( long millis )
	{
		Timer t = new Timer();

		TimerTask myTask = new TimerTask()
		{

			@Override
			public void run()
			{
				SplashDialog.this.dispose();
			}
		};
		t.schedule(
			myTask,
			millis );

	}
}
