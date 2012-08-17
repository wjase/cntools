/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class DoubleAttribute extends StringAttribute
{

	private double valueB;

	public DoubleAttribute( String name )
	{
		super( name );
	}

	public Double getDoubleValue()
	{
		return valueB;
	}

	public void setValue( Double value )
	{
		this.valueB = value;
		isSet( true );
		fireUpdate();
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( Double.parseDouble( value ) );

	}

}
