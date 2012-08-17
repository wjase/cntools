/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class BooleanAttribute extends StringAttribute
{

	private boolean valueB;

	IBooleanValueAdapter adapter = BasicBooleanAdapter.get();

	private static final BooleanDefaultValue defTrue = new BooleanDefaultConstant( true );

	private static final BooleanDefaultValue defFalse = new BooleanDefaultConstant( false );

	private BooleanDefaultValue def = defTrue;

	public BooleanAttribute( String name, IBooleanValueAdapter adapter )
	{
		super( name );
		this.adapter = adapter;
	}

	public BooleanAttribute( String name )
	{
		super( name );
	}

	public boolean getBooleanValue()
	{
		if (isSet())
		{
			return valueB;
		}
		return def.get();
	}

	public void setDefaultSource( BooleanDefaultValue def )
	{
		this.def = def;
	}

	public void setDefault( boolean def )
	{
		this.def = def ? defTrue : defFalse;
	}

	public void setValue( boolean value )
	{
		this.valueB = value;
		isSet( true );
		fireUpdate();

	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( adapter.parse( value ) );
	}

	@Override
	public String getStringValue()
	{
		return adapter.asString( valueB );
	}

}
