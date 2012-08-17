package com.cybernostics.lib.gui.control;

import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jasonw
 */
public class ComboListModelAdapter implements GrowableCollection
{
	DefaultComboBoxModel dlm = null;

	public ComboListModelAdapter( DefaultComboBoxModel dlm )
	{
		this.dlm = dlm;
	}

	@Override
	public void addElement( Object o )
	{
		dlm.addElement( o );
	}

}
