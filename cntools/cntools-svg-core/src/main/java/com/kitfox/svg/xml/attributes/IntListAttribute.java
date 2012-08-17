/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.XMLParseUtil;

/**
 *
 * @author jasonw
 */
public class IntListAttribute extends StringAttribute
{

	private int[] valueB;

	public IntListAttribute( String name )
	{
		super( name );
	}

	public int[] getIntListValue()
	{
		return valueB;
	}

	public void setValue( int[] value )
	{
		this.valueB = value;
		isSet( true );
	}

	@Override
	public void setStringValue( String value )
	{
		setValue( XMLParseUtil.parseIntList( value ) );
	}

}
