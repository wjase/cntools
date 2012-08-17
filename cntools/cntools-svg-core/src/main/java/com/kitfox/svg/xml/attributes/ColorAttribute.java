/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.ColorTable;
import java.awt.Color;

/**
 *
 * @author jasonw
 */
public class ColorAttribute extends StringAttribute
{

	private Color valueB;

	public ColorAttribute( String name )
	{
		super( name );
	}

	public Color getColorValue()
	{
		return valueB;
	}

	public void setValue( Color value )
	{
		this.valueB = value;
		isSet( set );
		fireUpdate();
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( ColorTable.parseColor( value ) );

	}

}
