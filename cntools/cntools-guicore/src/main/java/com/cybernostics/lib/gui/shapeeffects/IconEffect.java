package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Graphics2D;
import java.awt.Shape;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class IconEffect implements ShapeEffect
{

	Icon toPaint = null;

	public IconEffect( Icon toPaint )
	{
		this.toPaint = toPaint;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		toPaint.paintIcon(
			null,
			g2,
			0,
			0 );
	}

}
