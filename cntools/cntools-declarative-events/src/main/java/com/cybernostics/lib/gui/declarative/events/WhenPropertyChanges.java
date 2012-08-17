package com.cybernostics.lib.gui.declarative.events;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;

public abstract class WhenPropertyChanges
{

	PropertyChangeListener myListener = new PropertyChangeListener()
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			doThis( evt );

		}

	};

	public WhenPropertyChanges( JComponent source )
	{
		source.addPropertyChangeListener( getListener() );
	}

	public WhenPropertyChanges( String propname, SupportsPropertyChanges source )
	{
		source.addPropertyChangeListener(
			propname,
			getListener() );
	}

	public WhenPropertyChanges( SupportsPropertyChanges source )
	{
		source.addPropertyChangeListener( getListener() );
	}

	public WhenPropertyChanges( String propname, JComponent source )
	{
		source.addPropertyChangeListener(
			propname,
			getListener() );
	}

	public WhenPropertyChanges(
		String propname,
		PropertyChangeSupport propertySupport )
	{
		propertySupport.addPropertyChangeListener(
			propname,
			getListener() );
	}

	public WhenPropertyChanges( PropertyChangeSupport propertySupport )
	{
		propertySupport.addPropertyChangeListener( getListener() );
	}

	private PropertyChangeListener getListener()
	{
		return myListener;
	}

	public abstract void doThis( PropertyChangeEvent event );

}
