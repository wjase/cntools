package com.cybernostics.lib.gui.panel;

import javax.swing.ListModel;

/**
 * Represents a category of images
 * @author jasonw
 */
public interface ImageCategory
{

	public String getName();

	public ListModel getItems();
}
