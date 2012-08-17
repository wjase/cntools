package com.cybernostics.lib.gui.declarative.events;

import java.beans.PropertyChangeListener;

/**
 * Things inherit this if they have property change support wired in.
 * @author jasonw
 *
 */
public interface SupportsPropertyChanges
{
	public void addPropertyChangeListener( final PropertyChangeListener listener );

	public void addPropertyChangeListener( final String propertyName,
		final PropertyChangeListener listener );

	public void removePropertyChangeListener( final PropertyChangeListener listener );

}
