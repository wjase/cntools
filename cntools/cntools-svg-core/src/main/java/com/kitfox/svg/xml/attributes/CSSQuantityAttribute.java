/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.CSSUnits;

/**
 *
 * @author jasonw
 */
public class CSSQuantityAttribute extends StringAttribute
{

	private CSSQuantity value = new CSSQuantity();

	// used to return a default if value not set.
	// by providing a class rather than just a constant we provide
	// for defaults calculated on other parameters
	CSSQuantityDefaultValue def = new CSSQuantityDefaultConstant( new CSSQuantity() );

	public CSSUnits getUnits()
	{
		return value.getUnits();
	}

	public void setDefault( float f )
	{
		setDefault( new CSSQuantityDefaultConstant( f ) );
	}

	public void setDefault( CSSQuantityDefaultValue def )
	{
		this.def = def;
	}

	public CSSQuantityAttribute( String name )
	{
		super( name );
	}

	public float getNormalisedValue()
	{
		return getCSSValue().getNormalisedValue();
	}

	/**
	 * Returns the normalised value using unit conversions.
	 * If the value is a percentage it returns the value as a fraction of that value
	 *
	 * @param parent
	 * @return
	 */
	public float getNormalisedValue( float parentValue )
	{
		return getCSSValue().getNormalisedValue(
			parentValue );
	}

	public void setUnits( CSSUnits units )
	{
		value.setUnits( units );
		isSet( true );
	}

	public void setQuantity( float value )
	{
		this.value.setValue( value );
		isSet( true );
		fireUpdate();
	}

	public float getQuantity()
	{
		return getCSSValue().getValue();
	}

	public float getFloatValue()
	{
		return getQuantity();
	}

	@Override
	public void setStringValue( String value )
	{
		if (this.value.parse( value ))
		{
			setOriginalString( value );
			isSet( true );
			fireUpdate();
		}
	}

	public void setValue( CSSQuantity value )
	{
		this.value = value;
		isSet( true );
		fireUpdate();
	}

	public CSSQuantity getCSSValue()
	{
		if (isSet())
		{
			return this.value;
		}
		return def.get();
	}

	public void setValue( float value, CSSUnits units )
	{
		this.value.setValue(
			value,
			units );
		isSet( true );
		fireUpdate();
	}

	@Override
	public String getStringValue()
	{
		return value.asValueWithUnits();
	}

}
