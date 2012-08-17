package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;

public abstract class WhenSelectionDoubleClicked
{

	public WhenSelectionDoubleClicked( final JList aList )
	{
		aList.addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent e )
			{
				if (e.getClickCount() == 2)
				{
					doThis( e );
				}

			}

		} );

	}

	public abstract void doThis( MouseEvent e );

	public JList getList( MouseEvent e )
	{
		return (JList) e.getSource();
	}
}
