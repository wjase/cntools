package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.concurrent.CallableWorkerTask;
import com.cybernostics.lib.concurrent.GUIEventThread;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jasonw
 */
abstract public class ListFillerTask extends CallableWorkerTask
{

	private GrowableCollection dlm;

	public void setCollection( GrowableCollection dlm )
	{
		this.dlm = dlm;
	}

	public ListFillerTask( String name )
	{
		super( name );
	}

	public ListFillerTask( String name, GrowableCollection mod )
	{
		super( name );

		this.dlm = mod;
	}

	public void addItem( final Object o )
	{
		GUIEventThread.run( new Runnable()
		{

			@Override
			public void run()
			{
				dlm.addElement( o );
			}
		} );
	}
}
