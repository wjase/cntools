/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class FloatFractionAttribute extends FloatAttribute
{

	public FloatFractionAttribute( String name )
	{
		super( name );
	}

	@Override
	public void setValue( Float value )
	{
		if (value > 1)
		{
			value = 1f;
		}
		if (value < 0)
		{
			value = 0f;
		}
		super.setValue( value );
		fireUpdate();
	}

}
