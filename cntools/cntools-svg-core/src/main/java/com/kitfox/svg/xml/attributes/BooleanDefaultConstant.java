/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class BooleanDefaultConstant implements BooleanDefaultValue
{

	boolean defValue;

	public BooleanDefaultConstant( boolean def )
	{
		this.defValue = def;
	}

	@Override
	public boolean get()
	{
		return defValue;
	}

}
