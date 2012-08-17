/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class SimpleBooleanAdapter implements IBooleanValueAdapter
{
	private String trueValue = "true";
	private String falseValue = "false";

	public SimpleBooleanAdapter( String trueValue, String falseValue )
	{
		this.trueValue = trueValue;
		this.falseValue = falseValue;
	}

	SimpleBooleanAdapter()
	{

	}

	@Override
	public boolean parse( String input )
	{
		return input.equals( trueValue );
	}

	@Override
	public String asString( boolean input )
	{
		return input ? trueValue : falseValue;
	}

}
