/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public interface ElementStyle
{

	boolean getVisibility();

	float getOpacity();

	void setOpacity( float val );

	void setVisibility( boolean val );

}
