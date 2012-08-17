package com.cybernostics.lib.gui.declarative.events;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author jasonw
 */
abstract public class WhenListSelectionChanges
{

	public WhenListSelectionChanges( ListSelectionModel lsm )
	{
		lsm.addListSelectionListener( new ListSelectionListener()
		{

			@Override
			public void valueChanged( ListSelectionEvent e )
			{
				doThis( e );
			}

		} );
	}

	public WhenListSelectionChanges( JList list )
	{
		this( list.getSelectionModel() );
	}

	abstract public void doThis( ListSelectionEvent e );

}
