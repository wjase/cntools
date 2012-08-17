/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import java.awt.geom.GeneralPath;

/**
 *
 * @author jasonw
 */
public class WindingRuleAttribute extends IntAttribute
{

	public WindingRuleAttribute( String name )
	{
		super( name );
	}

	private static int parseString( String value )
	{
		return value.equals( "evenodd" ) ? GeneralPath.WIND_EVEN_ODD
			: GeneralPath.WIND_NON_ZERO;

	}

	@Override
	public void setStringValue( String value )
	{
		setValue( parseString( value ) );
	}

}
