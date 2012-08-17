package com.cybernostics.lib.gui;

import java.awt.Component;
import java.awt.Rectangle;

/**
 *
 * @author jasonw
 */
public class ComponentRepainter implements RepaintListener
{

	Component client = null;

	public ComponentRepainter( Component toPaint )
	{
		client = toPaint;
	}

	@Override
	public void repaint()
	{
		client.repaint();
	}

	@Override
	public void repaint( Rectangle r )
	{
		client.repaint(
			r.x,
			r.y,
			r.width,
			r.height );
	}

}
