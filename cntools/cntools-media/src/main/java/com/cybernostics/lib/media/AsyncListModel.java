package com.cybernostics.lib.media;

import com.cybernostics.lib.concurrent.CanCheckComplete;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultListModel;

import com.cybernostics.lib.gui.declarative.events.RunLater;

public class AsyncListModel< ListItemTye > extends DefaultListModel
	implements
	CanCheckComplete
{

	ListFetcher< ListItemTye > theFetcher;
	private boolean completed = false;

	@Override
	public boolean isComplete()
	{
		return completed;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6955755973591413814L;

	public AsyncListModel()
	{
	}

	public void setFetcher( ListFetcher< ListItemTye > theFetcher )
	{
		this.theFetcher = theFetcher;

		theFetcher.addPropertyChangeListener( new PropertyChangeListener()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				if (evt.getNewValue()
					.toString()
					.equalsIgnoreCase(
						"done" ))
				{
					completed = true;
					( (ListFetcher< ListItemTye >) evt.getSource() ).removePropertyChangeListener( this );
					removeFetcher();
					new RunLater()
					{

						@Override
						public void run( Object... args )
						{
							fireContentsChanged(
								this,
								-1,
								-1 );

						}
					};

				}
			}
		} );
	}

	public void removeFetcher()
	{
		if (theFetcher != null)
		{

			theFetcher = null;
		}
	}

	public void cancelLoad()
	{
		if (theFetcher != null)
		{
			theFetcher.cancel( true );
		}
	}
}
