package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.gui.GraphicsConfigurationSource;

/**
 *
 * @author jasonw
 */
public interface IBufferedEffect extends ShapeEffect
{

	public ShapeEffect getInternal();

	public void clear();

	public GraphicsConfigurationSource getGcSource();

	public void setGcSource( GraphicsConfigurationSource src );

}
