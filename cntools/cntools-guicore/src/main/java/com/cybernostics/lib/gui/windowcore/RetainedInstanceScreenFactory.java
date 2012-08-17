package com.cybernostics.lib.gui.windowcore;

import java.awt.Component;

/**
 * for simple ui elements which just "hang around" like backgrounds
 *
 * @author jasonw
 */
public class RetainedInstanceScreenFactory implements ScreenFactory
{

	private Component instance = null;

	public RetainedInstanceScreenFactory( Component instance )
	{
		this.instance = instance;
	}

	@Override
	public Component create()
	{
		return instance;
	}

}
