package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class ComponentPaint implements ShapeEffect
{

	private JComponent toPaint;

	public ComponentPaint( JComponent toPaint )
	{
		this.toPaint = toPaint;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		toPaint.paint( g2 );
	}

}
