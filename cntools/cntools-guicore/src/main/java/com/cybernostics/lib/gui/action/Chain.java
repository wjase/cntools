/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.gui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author jasonw
 */
public class Chain
{
	/**
	 * Static only
	 */
	private Chain()
	{

	}

	public static ActionListener connect( final ActionListener... listeners )
	{
		return new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				for (ActionListener eachListener : listeners)
				{
					eachListener.actionPerformed( e );
				}
			}
		};
	}
}
