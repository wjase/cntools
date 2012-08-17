/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author jasonw
 */
public class RectangleAttribute extends FloatListAttribute
{

	private Rectangle2D.Float rect = new Rectangle2D.Float();

	private boolean set = false;

	public boolean isSet()
	{
		return set;
	}

	public Rectangle2D.Float getRect()
	{
		return rect;
	}

	public RectangleAttribute( String name )
	{
		super( name );
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		setValue( getFloatListValue() );
	}

	@Override
	public void setValue( float[] value )
	{
		rect.setFrame(
			value[ 0 ],
			value[ 1 ],
			value[ 2 ],
			value[ 3 ] );
		super.setValue( value );
		set = true;
	}

}
