package com.cybernostics.lib.gui.panel;

import com.cybernostics.lib.gui.CNButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

class CustomComboBoxUI extends BasicComboBoxUI
{
	ComboBoxUI delegate;

	private Icon comboArrowButton = null;

	public Icon getCategoryButtonIcon()
	{
		return comboArrowButton;
	}

	public void setCategoryButtonIcon( Icon categoryButtonIcon )
	{
		this.comboArrowButton = categoryButtonIcon;
	}

	public CustomComboBoxUI( ComboBoxUI wrapped )
	{
		this.delegate = wrapped;
	}

	public static ComponentUI createUI( JComponent c )
	{
		JComboBox jcb = (JComboBox) c;
		return new CustomComboBoxUI( jcb.getUI() );
	}

	@Override
	protected JButton createArrowButton()
	{
		JButton button = new CNButton( comboArrowButton, null );

		button.setContentAreaFilled( false );
		button.setOpaque( false );
		button.setBorder( BorderFactory.createEmptyBorder(
			5,
			5,
			5,
			5 ) );
		return button;
	}

	@Override
	public void setPopupVisible( JComboBox c, boolean v )
	{
		delegate.setPopupVisible(
			c,
			v );
	}

	@Override
	public boolean isPopupVisible( JComboBox c )
	{
		return delegate.isPopupVisible( c );
	}

	@Override
	public boolean isFocusTraversable( JComboBox c )
	{
		return delegate.isFocusTraversable( c );
	}
}