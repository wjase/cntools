/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class BasicBooleanAdapter
{

	private static SimpleBooleanAdapter adapter = new SimpleBooleanAdapter();

	public static IBooleanValueAdapter get()
	{
		return adapter;
	}

}
