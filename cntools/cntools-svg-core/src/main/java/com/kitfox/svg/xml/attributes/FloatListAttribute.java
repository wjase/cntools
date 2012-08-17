/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.XMLParseUtil;

/**
 *
 * @author jasonw
 */
public class FloatListAttribute extends StringAttribute
{

	private float[] valueB;

	public FloatListAttribute( String name )
	{
		super( name );
	}

	public float[] getFloatListValue()
	{
		return valueB;
	}

	public void setValue( float[] value )
	{
		this.valueB = value;
		isSet( true );
		fireUpdate();
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( XMLParseUtil.parseFloatList( value ) );
	}

	public int length()
	{
		return valueB.length;
	}
}
