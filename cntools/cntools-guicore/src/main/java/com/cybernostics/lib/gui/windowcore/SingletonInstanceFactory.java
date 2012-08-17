package com.cybernostics.lib.gui.windowcore;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Component;

/**
 * Adapts a SingletonInstance for use as a screen factory
 *
 * @author jasonw
 */
abstract public class SingletonInstanceFactory extends
	SingletonInstance< Component > implements ScreenFactory
{

	@Override
	public Component create()
	{
		return get();
	}

}
