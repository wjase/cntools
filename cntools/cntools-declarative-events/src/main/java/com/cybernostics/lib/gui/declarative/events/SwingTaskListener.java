package com.cybernostics.lib.gui.declarative.events;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Override taskDone to act when a SwingWorker finishes
 * @author jasonw
 *
 */
abstract public class SwingTaskListener implements PropertyChangeListener
{

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		if (evt.getPropertyName()
			.equalsIgnoreCase(
				"state" ))
		{
			if (evt.getNewValue()
				.toString()
				.equalsIgnoreCase(
					"done" ))
			{
				taskDone( evt.getSource() );
			}

		}

	}

	abstract public void taskDone( Object task );

}
