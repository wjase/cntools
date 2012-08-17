package com.cybernostics.lib.gui.gallery;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * @author jasonw
 *
 */
public class ArrayListModel implements ListModel
{

	protected List< Object > internalList = new ArrayList< Object >();

	public ArrayListModel( List< ? extends Object > theList )
	{
		internalList.addAll( theList );
	}

	ConcurrentLinkedQueue< ListDataListener > listeners = new ConcurrentLinkedQueue< ListDataListener >();

	@Override
	public void addListDataListener( ListDataListener l )
	{
		listeners.add( l );
	}

	public void add( Object o )
	{
		internalList.add( o );
		int index = internalList.size() - 1;
		fireItemUpdated( new ListDataEvent( this,
			ListDataEvent.INTERVAL_ADDED,
			index,
			index ) );
	}

	public void removeElementAt( int index )
	{
		internalList.remove( index );
		fireItemUpdated( new ListDataEvent( this,
			ListDataEvent.INTERVAL_REMOVED,
			index,
			index ) );

	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt( int index )
	{
		return internalList.get( index );
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	@Override
	public int getSize()
	{
		return internalList.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
	 */
	@Override
	public void removeListDataListener( ListDataListener l )
	{
		listeners.remove( l );
	}

	protected void fireItemUpdated( ListDataEvent evt )
	{
		for (ListDataListener eachListener : listeners)
		{
			eachListener.contentsChanged( evt );
		}
	}
}
