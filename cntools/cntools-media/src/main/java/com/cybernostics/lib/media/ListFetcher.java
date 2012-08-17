package com.cybernostics.lib.media;

/*
 * ImageSearcher.java Copyright 2007 Sun Microsystems, Inc. ALL RIGHTS RESERVED Use of this software
 * is authorized pursuant to the terms of the license found at
 * http://developers.sun.com/berkeley_license.html.
 */

import java.net.URL;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;

/**
 * List model builder. Builds a list of items for a list
 * 
 * @author John O'Conner
 */
public abstract class ListFetcher< ListItemType > extends
	SwingWorker< List< ListItemType >, ListItemType >
{

	protected URL resourcePath;

	public ListFetcher( AsyncListModel< ListItemType > model, URL resourcePath )
	{
		this.resourcePath = resourcePath;
		this.model = model;
		model.setFetcher( this );
	}

	/**
	 * process is called as a result of this worker thread's calling the publish
	 * method.
	 * 
	 * As image thumbnails are retrieved, the worker publishes them to the list
	 * model.
	 * 
	 */
	protected void process( List< ListItemType > infoList )
	{
		for (ListItemType info : infoList)
		{
			if (isCancelled())
			{
				break;
			}
			model.addElement( info );
		}

	}

	@Override
	protected void done()
	{
		// whether we retrieved anything or not
		// we're done, so set the progress indicator accordingly
		setProgress( 100 );
		if (isCancelled())
		{
			return;
		}

	}

	private DefaultListModel model;

}
