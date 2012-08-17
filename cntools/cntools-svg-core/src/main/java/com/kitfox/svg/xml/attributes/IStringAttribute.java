/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public interface IStringAttribute
{

	boolean isSet();

	String getStringValue();

	void setStringValue( String value );

	void setName( String name );

	String getName();

	String asSVGString();

}
