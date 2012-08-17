package com.cybernostics.lib.gui.declarative.events;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public abstract class WhenEdited
{

	public WhenEdited( JTextComponent jc )
	{
		jc.getDocument()
			.addDocumentListener(
				new DocumentListener()
			{

				@Override
				public void changedUpdate( DocumentEvent e )
			{
				doThis( e );

			}

				@Override
				public void insertUpdate( DocumentEvent e )
			{
				doThis( e );

			}

				@Override
				public void removeUpdate( DocumentEvent e )
			{
				doThis( e );

			}
			} );
	}

	public abstract void doThis( DocumentEvent e );

}
