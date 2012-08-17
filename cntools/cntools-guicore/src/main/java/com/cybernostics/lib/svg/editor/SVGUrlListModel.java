package com.cybernostics.lib.svg.editor;

import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 * @author jasonw
 *
 */
public class SVGUrlListModel implements ListModel
{

	ArrayList< URL > internalList = new ArrayList< URL >();
	Map< Integer, SoftReference< ScalableSVGIcon >> iconList = new TreeMap< Integer, SoftReference< ScalableSVGIcon >>();

	public SVGUrlListModel( ArrayList< ? extends Object > theList )
	{
		for (Object eachObject : theList)
		{
			internalList.add( (URL) eachObject );

		}

	}

	@Override
	public void addListDataListener( ListDataListener l )
	{
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	@Override
	public Object getElementAt( int index )
	{
		URL iconURL = internalList.get( index );
		if (iconList.containsKey( index ))
		{
			SoftReference< ScalableSVGIcon > toShow = iconList.get( index );

			ScalableSVGIcon icon = toShow.get();
			if (icon == null)
			{
				icon = new ScalableSVGIcon( iconURL );
			}
			return icon;
		}

		ScalableSVGIcon icon = new ScalableSVGIcon( iconURL );
		SoftReference< ScalableSVGIcon > toStore = new SoftReference< ScalableSVGIcon >( icon );
		iconList.put(
			index,
			toStore );
		return icon;
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
	}
}
