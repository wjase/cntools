package com.cybernostics.lib.gui.autocomplete;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

/**
 *
 * @author jasonw
 */
public class ComboUIArrowExtractor extends BasicComboBoxUI
{
	BasicComboBoxUI ci;

	public ComboUIArrowExtractor( BasicComboBoxUI ci )
	{
		this.ci = ci;
	}

	@Override
	protected JButton createArrowButton()
	{
		return super.createArrowButton();
	}

}
