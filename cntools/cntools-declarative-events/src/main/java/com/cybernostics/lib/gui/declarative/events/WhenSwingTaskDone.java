package com.cybernostics.lib.gui.declarative.events;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingWorker;

public abstract class WhenSwingTaskDone< InterimType, FinalType >
{

	public WhenSwingTaskDone( final PropertyChangeSupport propertySupport )
	{
		new WhenPropertyChanges( "state", propertySupport )
		{

			@SuppressWarnings("unchecked")
			@Override
			public void doThis( PropertyChangeEvent evt )
			{
				SwingWorker< InterimType, FinalType > worker = (SwingWorker< InterimType, FinalType >) evt.getSource();
				if (worker.isDone())
				{
					dothis( evt );
				}

			}

		};
	}

	protected abstract void dothis( PropertyChangeEvent evt );
}
