/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.XMLParseUtil;

/**
 *
 * @author jasonw
 */
public class PolylineAttribute extends Path2DAttribute
{

	public PolylineAttribute( String name )
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
		fireUpdate();
	}

	protected void parsePolyPath( String pathString )
	{
		float[] points = XMLParseUtil.parseFloatList( pathString );

		moveTo(
			points[ 0 ],
			points[ 1 ] );
		for (int i = 2; i < points.length; i += 2)
		{
			lineTo(
				points[ i ],
				points[ i + 1 ] );
		}

	}

}
