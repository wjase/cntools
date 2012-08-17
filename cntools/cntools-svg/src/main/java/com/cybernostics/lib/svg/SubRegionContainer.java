package com.cybernostics.lib.svg;

import java.awt.geom.Rectangle2D;

/**
 * @author jasonw
 *
 */
public interface SubRegionContainer
{
	/**
	 * 
	 * @param regionName
	 * @return
	 */
	public Rectangle2D getItemRectangle( String regionName );
}
