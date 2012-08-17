package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

public abstract class WhenSelectionChanged
{

	public WhenSelectionChanged( final JComboBox jcb )
	{
		jcb.addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				doThis( e );

			}
		} );

	}

	public abstract void doThis( ActionEvent e );

	public JComponent getCombo( ActionEvent e )
	{
		return (JComboBox) e.getSource();
	}

	public JList getList( ActionEvent e )
	{
		return (JList) e.getSource();
	}
}
