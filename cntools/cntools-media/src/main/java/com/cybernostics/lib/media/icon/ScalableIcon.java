package com.cybernostics.lib.media.icon;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

/**
 * An icon which scales to a prescribed size
 * @author jasonw
 * 
 */
public interface ScalableIcon extends Icon
{

	public void setSize( Dimension2D d );

	// no getSize - call getIconWidth and getIconHeight

	public Dimension getPreferredSize();

	/**
	 * This is the smallest permissable dimension for setSize
	 * If it is null then the icon can shrink to zero size
	 * 
	 * @param d 
	 */
	public void setMinimumSize( Dimension d );

	public ScalableIcon copy();

	public void addPreferredSizeListener( PreferredSizeListener listener );

	public BufferedImage getImage();

	public void addPropertyChangeListener( PropertyChangeListener listener );

}
