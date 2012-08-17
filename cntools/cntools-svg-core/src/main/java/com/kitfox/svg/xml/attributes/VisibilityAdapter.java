/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class VisibilityAdapter
{

	private static SimpleBooleanAdapter adapter = new SimpleBooleanAdapter( "visible",
		"hidden" );

	public static IBooleanValueAdapter get()
	{
		return adapter;
	}

}
