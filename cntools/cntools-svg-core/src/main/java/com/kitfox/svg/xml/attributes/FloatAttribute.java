/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class FloatAttribute extends StringAttribute
{
	private float valueB = 0;

	private FloatDefaultValue def = new FloatDefaultValue()
	{

		@Override
		public float get()
		{
			return valueB;
		}
	};

	public FloatAttribute( String name )
	{
		super( name );
	}

	public void setDefaultSource( FloatDefaultValue def )
	{
		this.def = def;
	}

	public Float getFloatValue()
	{
		if (isSet())
		{
			return valueB;
		}
		return def.get();
	}

	public void setValue( Float value )
	{
		this.valueB = value;
		set = true;
		fireUpdate();

	}

	@Override
	public void setStringValue( String value )
	{
		set = true;
		super.setStringValue( value );
		setValue( Float.parseFloat( value ) );
	}
}
