/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class FloatDefaultConstant implements FloatDefaultValue
{

	float defValue;

	public FloatDefaultConstant( float def )
	{
		this.defValue = def;
	}

	@Override
	public float get()
	{
		return defValue;
	}

}
