package com.cybernostics.lib.gui.lookandfeel;

import javax.swing.UIManager;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * Convenience class for setting the system look and feel with a single call to
 * the static method set().
 * 
 * @author jasonw
 * 
 */
public class NimbusLook
{

	public static void set()
	{
		try
		{
			UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );

		}
		catch (Exception e)
		{
			try
			{
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
			}
			catch (Exception e1)
			{
				UnhandledExceptionManager.handleException( e1 );
			}
		}

	}
}
