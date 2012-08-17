/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public interface IBooleanValueAdapter
{
	boolean parse( String input );

	String asString( boolean input );
}
