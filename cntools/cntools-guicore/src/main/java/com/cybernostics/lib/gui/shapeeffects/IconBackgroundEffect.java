package com.cybernostics.lib.gui.shapeeffects;

import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jasonw
 */
public class IconBackgroundEffect implements ShapeEffect
{

	ScalableIcon iconToPaint = null;

	public IconBackgroundEffect( ScalableIcon icon )
	{
		this.iconToPaint = icon;
	}

	@Override
	public void draw( Graphics2D g2, Shape s )
	{
		Shape old = g2.getClip();
		try
		{
			g2.setClip( s );
			int w = iconToPaint.getIconWidth();
			int h = iconToPaint.getIconHeight();
			Rectangle2D bounds = s.getBounds2D();
			g2.translate(
				bounds.getMinX(),
				bounds.getMinX() );
			g2.scale(
				bounds.getWidth() / w,
				bounds.getHeight() / h );
			//iconToPaint.setSize( new Dimension( (int) bounds.getWidth(), (int) bounds.getHeight() ) );
			iconToPaint.paintIcon(
				null,
				g2,
				0,
				0 );
		}
		finally
		{
			g2.setClip( old );
		}

	}
}
