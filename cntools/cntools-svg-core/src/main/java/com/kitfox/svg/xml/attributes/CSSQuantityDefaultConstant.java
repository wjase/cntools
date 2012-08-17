/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.CSSUnits;

/**
 *
 * @author jasonw
 */
public class CSSQuantityDefaultConstant implements CSSQuantityDefaultValue
{

	CSSQuantity defValue;

	public CSSQuantityDefaultConstant( CSSQuantity def )
	{
		this.defValue = def;
	}

	public CSSQuantityDefaultConstant( float f, CSSUnits units )
	{
		this( new CSSQuantity( f, units ) );
	}

	public CSSQuantityDefaultConstant( float f )
	{
		this( new CSSQuantity( f ) );
	}

	@Override
	public CSSQuantity get()
	{
		return defValue;
	}

}
