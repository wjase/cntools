package com.cybernostics.lib.gui.border;

/*
 * Copyright 2002 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED
 * CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT
 * WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS
 * OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for use in the design,
 * construction, operation or maintenance of any nuclear facility.
 */

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.cybernostics.lib.gui.border.sun.Fill;
import com.cybernostics.lib.gui.border.sun.ImageFill;
import com.cybernostics.lib.gui.border.sun.SVGFill;
import com.cybernostics.lib.gui.border.sun.TiledFill;
import com.cybernostics.lib.resourcefinder.ResourceFinder;

/**
 * Creates a border whose sides are filled with specified images
 * 
 * @author jasonw
 */
public class BorderFill extends Fill implements Border
{

	public static final int TOP = 0;
	public static final int TOP_RIGHT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM_RIGHT = 3;
	public static final int BOTTOM = 4;
	public static final int BOTTOM_LEFT = 5;
	public static final int LEFT = 6;
	public static final int TOP_LEFT = 7;
	public static final int NUM_TILES = 8;

	private Fill[] fills;
	private Dimension[] sizes = null;

	public BorderFill( String patternPath )
	{
		setFills(
			new SVGFill( String.format(
				patternPath,
				"top" ) ),
			new SVGFill( String.format(
				patternPath,
				"topright" ) ),
			new SVGFill( String.format(
				patternPath,
				"right" ) ),
			new SVGFill( String.format(
				patternPath,
				"bottomright" ) ),
			new SVGFill( String.format(
				patternPath,
				"bottom" ) ),
			new SVGFill( String
				.format(
					patternPath,
					"bottomleft" ) ),
			new SVGFill( String.format(
				patternPath,
				"left" ) ),
			new SVGFill(
				String.format(
					patternPath,
					"topleft" ) ) );
	}

	private void setFills( SVGFill... fillImages )
	{
		assert fillImages.length == NUM_TILES;
		// assert that both arrays are length 8
		this.fills = new Fill[ NUM_TILES ];
		sizes = new Dimension[ NUM_TILES ];
		for (int index = 0; index < NUM_TILES; ++index)
		{
			fills[ index ] = fillImages[ index ];
			sizes[ index ] = fillImages[ index ].getIcon()
				.getPreferredSize();
		}
	}

	public BorderFill( Fill[] fills, Dimension[] sizes )
	{
		// assert that both arrays are length 8
		this.fills = (Fill[]) ( fills.clone() );
		this.sizes = (Dimension[]) ( sizes.clone() );
	}

	public BorderFill(
		BufferedImage image,
		Rectangle[] rectangles,
		boolean isTile[] )
	{
		assert ( rectangles.length == NUM_TILES );

		// assert that rectangles is length 8
		// assert that isTile is length 4
		sizes = new Dimension[ NUM_TILES ];
		Fill fills[] = new Fill[ NUM_TILES ];
		for (int i = 0; i < fills.length; i++)
		{
			Rectangle r = rectangles[ i ];
			BufferedImage sample = image.getSubimage(
				r.x,
				r.y,
				r.width,
				r.height );
			ImageFill fill = new ImageFill( sample );
			if (( i % 2 == 0 ) && isTile[ i / 2 ])
			{
				fills[ i ] = new TiledFill( fill, r.width, r.height );
			}
			else
			{
				fills[ i ] = fill;
			}
			sizes[ i ] = new Dimension( r.width, r.height );
		}
		this.fills = (Fill[]) ( fills.clone() );
	}

	public BorderFill( BufferedImage image, Rectangle[] rectangles )
	{
		this( image, rectangles, new boolean[]
		{ true, true, true, true } );
	}

	public Fill[] getFills()
	{
		return fills;
	}

	public void setFills( Fill[] fills )
	{
		// assert fills.length == 8
		System.arraycopy(
			fills,
			0,
			this.fills,
			0,
			8 );
	}

	public Dimension[] getSizes()
	{
		return sizes;
	}

	public void setSizes( Dimension[] sizes )
	{
		// assert sizes.length == 8
		System.arraycopy(
			sizes,
			0,
			this.sizes,
			0,
			8 );
	}

	/**
	 * <pre>
	 *  x,y   xt1                  xt2
	 *     +----+--------------------+-------+
	 *     | 7  |        0           |   1   |
	 *     |    |                    |       |
	 *     | xl +--------------------+       |
	 * yl1 +-+--+                    | xr    |
	 *     | |                       +-+-----+	yr1
	 *     |6|                         |  2  |
	 *     | |                         |     |
	 * yl2 +---+                    +--+-----+	yr2
	 *     | 5 |                    |        |
	 *     |   +-------- 4 ---------+yb   3  |
	 *     +---+--------------------+--------+
	 *         xb1                  xb2     x+w,y+h
	 * </pre>
	 */
	public void paintFill( Component c, Graphics g, Rectangle r )
	{
		int x = r.x, y = r.y, w = r.width, h = r.height;
		int xt1 = x + sizes[ TOP_LEFT ].width;
		int xt2 = ( x + w ) - sizes[ TOP_RIGHT ].width;
		int xb1 = x + sizes[ BOTTOM_LEFT ].width;
		int xb2 = ( x + w ) - sizes[ BOTTOM_RIGHT ].width;
		int xr = ( x + w ) - sizes[ RIGHT ].width;
		int yl1 = y + sizes[ TOP_LEFT ].height;
		int yl2 = ( y + h ) - sizes[ BOTTOM_LEFT ].height;
		int yr1 = y + sizes[ TOP_RIGHT ].height;
		int yr2 = ( y + h ) - sizes[ BOTTOM_RIGHT ].height;
		int yb = ( y + h ) - sizes[ BOTTOM ].height;

		// shift the sides to ensure they overlap the ends
		final int OFFSET = 2;
		final int OVERLAP = 4;

		fills[ TOP ].paintFill(
			c,
			g,
			xt1 - OFFSET,
			y,
			xt2 - xt1 + OVERLAP,
			sizes[ TOP ].height );
		fills[ TOP_RIGHT ].paintFill(
			c,
			g,
			xt2,
			y,
			sizes[ TOP_RIGHT ].width,
			sizes[ TOP_RIGHT ].height );
		fills[ RIGHT ].paintFill(
			c,
			g,
			xr,
			yr1 - OFFSET,
			sizes[ RIGHT ].width,
			yr2 - yr1 + OVERLAP );
		fills[ BOTTOM_RIGHT ].paintFill(
			c,
			g,
			xb2,
			yr2,
			( x + w ) - xb2,
			sizes[ BOTTOM_RIGHT ].height );
		fills[ BOTTOM ].paintFill(
			c,
			g,
			xb1 - OFFSET,
			yb,
			xb2 - xb1 + OVERLAP,
			sizes[ BOTTOM ].height );
		fills[ BOTTOM_LEFT ].paintFill(
			c,
			g,
			x,
			yl2,
			sizes[ BOTTOM_RIGHT ].width,
			( y + h ) - yl2 );
		fills[ LEFT ].paintFill(
			c,
			g,
			x,
			yl1 - OFFSET,
			sizes[ LEFT ].width,
			yl2 - yl1 + OVERLAP );
		fills[ TOP_LEFT ].paintFill(
			c,
			g,
			x,
			y,
			sizes[ TOP_LEFT ].width,
			yl1 - y );
	}

	public Insets getBorderInsets( Component c )
	{
		return new Insets( sizes[ TOP ].height, /* top */
			sizes[ LEFT ].width, /* left */
			sizes[ BOTTOM ].height, /* bottom */
			sizes[ RIGHT ].width ); /* right */
	}

	public boolean isBorderOpaque()
	{
		return true;
	}

	public void paintBorder( Component c, Graphics g, int x, int y, int w, int h )
	{
		Graphics2D g2d = (Graphics2D) g;
		Composite oldComposite = g2d.getComposite();
		g2d.setComposite( AlphaComposite.SrcOver );
		paintFill(
			c,
			g,
			new Rectangle( x, y, w, h ) );
		g2d.setComposite( oldComposite );
	}

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "svg fill" );
		jf.setSize(
			400,
			400 );
		jf.getContentPane()
			.setLayout(
				new GridLayout() );

		// SVGFill svgf = new SVGFill();
		JPanel p = new JPanel();
		p.setBorder( new BorderFill( "images/pictureframe_%s.svg" ) );

		jf.getContentPane()
			.add(
				p );

		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setVisible( true );
	}
}
