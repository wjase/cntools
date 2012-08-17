package com.cybernostics.lib.gui;

import java.awt.Component;
import java.awt.GraphicsConfiguration;

/**
 *
 * @author jasonw
 */
public class ComponentGraphicsConfigurationSource
	implements
	GraphicsConfigurationSource
{

	public static GraphicsConfigurationSource create( Component c )
	{
		return new ComponentGraphicsConfigurationSource( c );
	}

	Component comp = null;

	private ComponentGraphicsConfigurationSource( Component c )
	{
		this.comp = c;
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return comp.getGraphicsConfiguration();
	}

}
