package com.cybernostics.lib.gui.gallery;

import java.awt.Dimension;

import javax.swing.JComponent;

import com.cybernostics.lib.gui.declarative.events.SupportsPropertyChanges;

public interface GalleryCellRenderer extends SupportsPropertyChanges
{

	/**
	 * Called when a component is no longer used for rendering becuase it has
	 * moved out of the visible viewport. This can be recycled for new calls to
	 * getRenderer()
	 * 
	 * @param comp
	 */
	public void finishedWith( JComponent comp );

	public JComponent getRenderer( Object list, int index, Object value );

	public JComponent getBlankComponent();

	public Dimension getItemSize();

	/**
	 * @param dimension
	 */
	public void setItemSize( Dimension dimension );

}
