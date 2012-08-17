package com.cybernostics.lib.gui.panel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;

import org.xhtmlrenderer.util.ImageUtil;

import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.svg.SVGUtil;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.app.beans.SVGPanel;

/**
 * Once the SVG is rendered it is cached as a bitmap
 * 
 * @author jasonw
 * 
 */
public class StaticSVGPanel extends SVGPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3874166397565671160L;
	URI diagramURI = null;

	public StaticSVGPanel( URL svgSource )
	{
		setScaleToFit( true );
		diagramURI = getSvgUniverse().loadSVG(
			svgSource );
		setSvgURI( diagramURI );
		setAntiAlias( true );

		new WhenResized( this )
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cybernostics.lib.gui.declarative.events.WhenResized#doThis
			 * (java.awt.event.ComponentEvent)
			 */
			@Override
			public void doThis( ComponentEvent e )
			{
				background = null;
			}
		};

	}

	/**
	 * Returns the Rectangle enclosing the specified object with the id= itemId.
	 * The returned rectangle is scaled to the size of this container
	 * 
	 * @param itemId
	 *            - svg element id of element for which to retrieve the
	 *            rectangle
	 * @return a Rectangle enclosing the object or null if the object is not
	 *         found
	 */
	public Rectangle2D getItemRectangle( String itemId )
	{
		SVGDiagram dia = getSvgUniverse().getDiagram(
			diagramURI );
		return SVGUtil.getSubItemRectangle(
			itemId,
			dia );

	}

	BufferedImage background = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kitfox.svg.app.beans.SVGPanel#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		if (background == null)
		{
			background = ImageUtil.createCompatibleBufferedImage(
				getWidth(),
				getHeight() );
			Graphics gBitMap = background.getGraphics();
			gBitMap.setClip(
				0,
				0,
				getWidth(),
				getHeight() );
			super.paintComponent( gBitMap );
			repaint();
		}

		g.drawImage(
			background,
			0,
			0,
			this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#imageUpdate(java.awt.Image, int, int, int, int,
	 * int)
	 */
	@Override
	public boolean imageUpdate( Image img,
		int infoflags,
		int x,
		int y,
		int w,
		int h )
	{
		repaint();
		return super.imageUpdate(
			img,
			infoflags,
			x,
			y,
			w,
			h );
	}
}
