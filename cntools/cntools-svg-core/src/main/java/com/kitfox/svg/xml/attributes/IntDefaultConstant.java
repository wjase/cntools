/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class IntDefaultConstant implements IntDefaultValue
{

	int defValue;

	public IntDefaultConstant( int def )
	{
		this.defValue = def;
	}

	@Override
	public int get()
	{
		return defValue;
	}

}
