package com.cybernostics.lib.gui.declarative.events;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

public abstract class WhenSelectedChanges
{

	public WhenSelectedChanges( ButtonGroup bg )
	{
		Enumeration< AbstractButton > buttons = bg.getElements();
		while (buttons.hasMoreElements())
		{
			buttons.nextElement()
				.getModel()
				.addItemListener(
					getItemListener() );
		}
	}

	ItemListener theListener = null;

	private ItemListener getItemListener()
	{
		if (theListener == null)
		{
			theListener = new ItemListener()
			{

				@Override
				public void itemStateChanged( ItemEvent e )
				{
					doThis(
						(ButtonModel) e.getSource(),
						e.getStateChange() == ItemEvent.SELECTED );

				}
			};

		}
		return theListener;
	}

	public WhenSelectedChanges( final AbstractButton but )
	{
		but.getModel()
			.addItemListener(
				getItemListener() );
	}

	abstract public void doThis( ButtonModel but, boolean bSelected );
}
