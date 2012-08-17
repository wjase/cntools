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
public class SystemLook
{

	public static void set()
	{
		try
		{
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			UnhandledExceptionManager.handleException( e );
		}

	}
}
