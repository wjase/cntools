package com.cybernostics.lib.gui.gallery;

import com.cybernostics.lib.gui.declarative.events.WhenPropertyChanges;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author jasonw
 */
class ComponentGallerySelectionModel implements ListSelectionModel
{

	ComponentGallery gallery = null;

	public ComponentGallerySelectionModel( ComponentGallery gallery )
	{
		this.gallery = gallery;

		new WhenPropertyChanges( ComponentGallery.CURRENT_INDEX, gallery )
		{

			@Override
			public void doThis( PropertyChangeEvent event )
			{
				fireSelectionEvent();
			}
		};
	}

	public void fireSelectionEvent()
	{
		final ListSelectionEvent lse = new ListSelectionEvent( gallery,
			gallery.getSelectedIndex(),
			gallery
				.getSelectedIndex(),
			false );
		for (ListSelectionListener eachListener : listeners)
		{
			eachListener.valueChanged( lse );
		}
	}

	@Override
	public void setSelectionInterval( int index0, int index1 )
	{
		gallery.setSelectedIndex( index0 );
	}

	@Override
	public void addSelectionInterval( int index0, int index1 )
	{
	}

	@Override
	public void removeSelectionInterval( int index0, int index1 )
	{
	}

	@Override
	public int getMinSelectionIndex()
	{
		return gallery.getSelectedIndex();
	}

	@Override
	public int getMaxSelectionIndex()
	{
		return gallery.getSelectedIndex();
	}

	@Override
	public boolean isSelectedIndex( int index )
	{
		return gallery.getSelectedIndex() == index;
	}

	@Override
	public int getAnchorSelectionIndex()
	{
		return gallery.getSelectedIndex();
	}

	@Override
	public void setAnchorSelectionIndex( int index )
	{
		gallery.setSelectedIndex( index );
	}

	@Override
	public int getLeadSelectionIndex()
	{
		return gallery.getSelectedIndex();
	}

	@Override
	public void setLeadSelectionIndex( int index )
	{
		gallery.setSelectedIndex( index );
	}

	@Override
	public void clearSelection()
	{
	}

	@Override
	public boolean isSelectionEmpty()
	{
		return false;
	}

	@Override
	public void insertIndexInterval( int index, int length, boolean before )
	{
	}

	@Override
	public void removeIndexInterval( int index0, int index1 )
	{
	}

	@Override
	public void setValueIsAdjusting( boolean valueIsAdjusting )
	{
	}

	@Override
	public boolean getValueIsAdjusting()
	{
		return false;
	}

	@Override
	public void setSelectionMode( int selectionMode )
	{
	}

	@Override
	public int getSelectionMode()
	{
		return ListSelectionModel.SINGLE_SELECTION;
	}

	ConcurrentLinkedQueue< ListSelectionListener > listeners = new ConcurrentLinkedQueue< ListSelectionListener >();

	@Override
	public void addListSelectionListener( ListSelectionListener x )
	{
		listeners.add( x );
	}

	@Override
	public void removeListSelectionListener( ListSelectionListener x )
	{
		listeners.remove( x );
	}

}
