/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class PolygonAttribute extends PolylineAttribute
{

	public PolygonAttribute( String name )
	{
		super( name );
	}

	@Override
	public String asSVGString()
	{
		return super.asSVGString();
	}

	@Override
	public void setStringValue( String value )
	{
		parsePolyPath( value );
		closePath();
		fireUpdate();
	}

}
