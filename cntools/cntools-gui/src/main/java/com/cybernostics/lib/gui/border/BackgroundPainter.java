package com.cybernostics.lib.gui.border;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 * @author jasonw
 *
 */
public class BackgroundPainter
{

	private static AlphaComposite translucent = AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER,
		0.5f );

	public static void paintTranslucentBackground( JComponent comp, Graphics g )
	{
		Border b = comp.getBorder();
		if (b != null && b instanceof RendersBackgroundBorder)
		{
			Graphics2D g2 = (Graphics2D) g;
			Composite c = g2.getComposite();
			g2.setComposite( translucent );
			RendersBackgroundBorder rb = (RendersBackgroundBorder) comp.getBorder();
			rb.paintBackground(
				comp,
				g,
				0,
				0,
				comp.getWidth(),
				comp.getHeight() );
			g2.setComposite( c );
		}
	}

	public static void paintBackground( JComponent comp, Graphics g )
	{
		Border b = comp.getBorder();
		if (b != null && b instanceof RendersBackgroundBorder)
		{
			RendersBackgroundBorder rb = (RendersBackgroundBorder) comp.getBorder();
			rb.paintBackground(
				comp,
				g,
				0,
				0,
				comp.getWidth(),
				comp.getHeight() );
		}
	}

}
