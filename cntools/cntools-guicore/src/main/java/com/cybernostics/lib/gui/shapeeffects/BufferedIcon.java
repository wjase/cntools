/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class BufferedIcon implements Icon
{
	private Icon internal = null;
	private IconEffect ie = null;
	private BufferedEffect be = null;
	private Rectangle2D toDraw = new Rectangle2D.Double();

	public BufferedIcon( Icon i )
	{
		setIcon( i );
	}

	public final void setIcon( Icon i )
	{
		internal = i;
		ie = new IconEffect( i );
		be = new BufferedEffect( null, ie );

	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		toDraw.setFrame(
			x,
			y,
			internal.getIconWidth(),
			internal.getIconHeight() );
		be.draw(
			(Graphics2D) g,
			toDraw );

	}

	@Override
	public int getIconWidth()
	{
		return internal.getIconWidth();
	}

	@Override
	public int getIconHeight()
	{
		return internal.getIconHeight();
	}

}
