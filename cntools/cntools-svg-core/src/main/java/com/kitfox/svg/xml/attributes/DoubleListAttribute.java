/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.XMLParseUtil;

/**
 *
 * @author jasonw
 */
public class DoubleListAttribute extends StringAttribute
{

	private double[] valueB;

	public DoubleListAttribute( String name )
	{
		super( name );
	}

	public double[] getDoubleListValue()
	{
		return valueB;
	}

	public void setValue( double[] value )
	{
		this.valueB = value;
		isSet( true );
		fireUpdate();

	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( XMLParseUtil.parseDoubleList( value ) );

	}

}
