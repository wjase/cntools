/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class IntAttribute extends StringAttribute
{

	private int valueB;

	private IntDefaultValue def = new IntDefaultValue()
	{

		@Override
		public int get()
		{
			return valueB;
		}
	};

	public void setDefaultSource( IntDefaultValue def )
	{
		this.def = def;
	}

	public IntAttribute( String name )
	{
		super( name );
	}

	public int getIntValue()
	{
		if (isSet())
		{
			return valueB;
		}

		return def.get();
	}

	public void setValue( int value )
	{
		this.valueB = value;
		isSet( true );

	}

	@Override
	public void setStringValue( String value )
	{
		setValue( Integer.parseInt( value ) );
	}

	public void setDefaultSource( int defValue )
	{
		setDefaultSource( new IntDefaultConstant( defValue ) );
	}

}
