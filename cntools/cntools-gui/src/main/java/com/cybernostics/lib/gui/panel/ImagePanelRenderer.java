package com.cybernostics.lib.gui.panel;

import javax.swing.AbstractButton;

/**
 *
 * @author jasonw
 */
public interface ImagePanelRenderer
{
	/**
	 * 
	 */
	public static final String OBJ_PROPERTY = "OBJ";

	AbstractButton getImagePaneButton( Object value );

	Object getObjectPayload( AbstractButton ab );

}
