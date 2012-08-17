package com.cybernostics.lib.media.icon;

import java.awt.Component;

/**
 * Adapter class for components
 * @author jasonw
 */
public class ComponentRepaintListener implements RepaintListener
{

	Component toRepaint = null;

	public ComponentRepaintListener( Component toRepaint )
	{
		this.toRepaint = toRepaint;
	}

	@Override
	public void repaint()
	{
		toRepaint.repaint();
	}
}
