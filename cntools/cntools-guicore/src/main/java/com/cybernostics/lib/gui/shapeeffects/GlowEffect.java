package com.cybernostics.lib.gui.shapeeffects;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 * Filename : GlowText.java Created By : jason wraxall Description: see class description below
 * 
 * Copyright Cybernostics Australia 2009
 */
/**
 * Static method to draw glowing shapes and text of a given font
 */
public class GlowEffect
{

	public enum Position
	{

		NORTH, WEST, EAST, SOUTH, CENTER
	};

	FontRenderContext fontContext = null;

	/**
	 * This method draws a glowing text
	 *
	 * @param g2 - the context on which to draw
	 * @param text - the text to draw
	 * @param font - the font to draw it in
	 * @param bounds
	 * @param pos
	 * @param glowColor
	 * @param glowWidth
	 */
	public static void drawText( Graphics2D g2,
		String text,
		Rectangle bounds,
		Position pos,
		Color glowColor,
		int glowWidth,
		double glowAlpha )
	{
		Font drawFont = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout( text, drawFont, frc );
		Shape outline = textLayout.getOutline( null );

		GlowEffect.drawGlowingShape(
			g2,
			outline,
			bounds,
			pos,
			glowColor,
			glowWidth,
			glowAlpha );

	}

	/**
	 * This method draws a glowing text
	 *
	 * @param g2 - the context on which to draw
	 * @param text - the text to draw
	 * @param font - the font to draw it in
	 * @param bounds
	 * @param pos
	 * @param glowColor
	 * @param glowWidth
	 */
	public static void drawText( Graphics2D g2,
		String text,
		int x,
		int y,
		Color glowColor,
		int glowWidth,
		double glowAlpha )
	{
		Font drawFont = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		TextLayout textLayout = new TextLayout( text, drawFont, frc );
		textLayout.getDescent();
		Shape outline = textLayout.getOutline( null );

		GlowEffect.drawGlowingShape(
			g2,
			outline,
			x,
			y,
			glowColor,
			glowWidth,
			glowAlpha );

	}

	public static void drawGlowingShape( Graphics2D g2,
		Shape toDraw,
		Rectangle bounds,
		Position pos,
		Color glowColor,
		int glowWidth )
	{
		drawGlowingShape(
			g2,
			toDraw,
			bounds,
			pos,
			glowColor,
			glowWidth,
			1.0f );
	}

	/**
	 * Draws a glow with a variable alpha (used for animating the glow)
	 *
	 * @see drawGlowingShape
	 * @param alpha
	 */
	public static void drawGlowingShape( Graphics2D g2,
		Shape toDraw,
		Rectangle bounds,
		Position pos,
		Color glowColor,
		int glowWidth,
		double alpha )
	{
		// Calculate the position of the text based on the desired position
		// enum
		double x = bounds.x;
		double y = bounds.y;

		if (pos != null)
		{
			Rectangle shapeBounds = toDraw.getBounds();

			shapeBounds.height = Math.min(
				g2.getFontMetrics()
					.getMaxAscent(),
				shapeBounds.height );

			switch (pos)
			{
				case CENTER:
					x += ( bounds.width - shapeBounds.getWidth() ) / 2;
					y += ( bounds.height + shapeBounds.getHeight() ) / 2;
					break;
				case EAST:
					x += ( bounds.width - shapeBounds.getWidth() );
					y += ( bounds.height + shapeBounds.getHeight() ) / 2;
					break;
				case NORTH:
					x += ( bounds.width - shapeBounds.getWidth() ) / 2;
					// the text anchor is on the bottom of the text???
					y += ( shapeBounds.getHeight() + ( glowWidth / 2 ) );
					break;
				case SOUTH:
					x += ( bounds.width - shapeBounds.getWidth() ) / 2;
					y += ( bounds.height + shapeBounds.getHeight() );
					break;
				case WEST:
					y += ( bounds.height + shapeBounds.getHeight() ) / 2;
					break;
			}

		}

		drawGlowingShape(
			g2,
			toDraw,
			x,
			y,
			glowColor,
			glowWidth,
			alpha );
	}

	public static void drawGlowingShape( Graphics2D g2,
		Shape toDraw,
		double x,
		double y,
		Color glowColor,
		int glowWidth,
		double alpha )
	{
		AffineTransform at = AffineTransform.getTranslateInstance(
			x,
			y );
		Shape outline = at.createTransformedShape( toDraw );

		// if an anchor is specified then use it to move the shape witin the
		// bounding rectangle
		// otherwise just draw the shape where it is
		Stroke defaultStroke = g2.getStroke();

		// only draw the glow if we are glowing
		if (glowWidth > 0)
		{
			Composite outerComposite = (Composite) AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER,
				0.4f * (float) alpha );
			Composite innerComposite = (Composite) AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER,
				0.7f * (float) alpha );

			Stroke outerStroke = new BasicStroke( glowWidth,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND );
			Stroke innerStroke = new BasicStroke( (float) ( glowWidth * .8 ),
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND );

			// save the current Graphics values
			Paint p = g2.getPaint();
			Composite savedComposite = g2.getComposite();

			// set the glow color
			g2.setPaint( glowColor );

			// Draw a partially transparent outline in a wide stroke
			g2.setStroke( outerStroke );
			g2.setComposite( outerComposite );
			g2.draw( outline );

			// Draw a less transparent outline in a less wide stroke
			g2.setStroke( innerStroke );
			g2.setComposite( innerComposite );
			g2.draw( outline );

			// restore the current paint value
			g2.setPaint( p );
			g2.setComposite( savedComposite );

		}
		g2.fill( outline );

		// restore Graphics values
		g2.setStroke( defaultStroke );

	}

}
