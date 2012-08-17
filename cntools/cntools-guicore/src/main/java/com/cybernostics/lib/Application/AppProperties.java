package com.cybernostics.lib.Application;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Properties;
import java.util.Set;

/**
 * JoeyMail property map separate from System.Properties.
 * 
 * @author jasonw
 * 
 */
public class AppProperties
{

	public static Object get( String key )
	{
		return internal.get()
			.getProperty(
				key );
	}

	public static void put( String key, Object value )
	{
		Object old = internal.get()
			.getProperty(
				key );
		internal.get()
			.putProperty(
				key,
				value );
		internal.get()
			.firePropertyChange(
				key,
				old,
				value );
	}

	public static Set< Object > keySet()
	{
		return internal.get()
			.getkeySet();
	}

	public static boolean containsKey( String val )
	{
		return internal.get().myProps.containsKey( val );
	}

	public static void addPropertChangeListener( PropertyChangeListener listener )
	{
		internal.get().changes.addPropertyChangeListener( listener );
	}

	private static SingletonInstance< AppProperties > internal = new SingletonInstance< AppProperties >()
	{

		@Override
		protected AppProperties createInstance()
		{
			return new AppProperties();
		}
	};
	private Properties myProps = new Properties();
	private PropertyChangeSupport changes = new PropertyChangeSupport( this );

	private Object getProperty( String toGet )
	{
		return myProps.get( toGet );
	}

	private void putProperty( String key, Object value )
	{
		myProps.put(
			key,
			value );
	}

	private Set< Object > getkeySet()
	{
		return myProps.keySet();
	}

	private void firePropertyChange( String key, Object old, Object value )
	{
		changes.firePropertyChange(
			key,
			old,
			value );
	}

	public static ActionListener putAction( final String key, final Object obj )
	{
		return new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				AppProperties.put(
					key,
					obj );
			}
		};
	}
}
