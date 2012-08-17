package com.cybernostics.lib.gui.control;

import javax.swing.DefaultListModel;

/**
 *
 * @author jasonw
 */
public class DefaultListModelAdapter implements GrowableCollection
{
	DefaultListModel dlm = null;

	public DefaultListModelAdapter( DefaultListModel dlm )
	{
		this.dlm = dlm;
	}

	@Override
	public void addElement( Object o )
	{
		dlm.addElement( o );
	}

}
