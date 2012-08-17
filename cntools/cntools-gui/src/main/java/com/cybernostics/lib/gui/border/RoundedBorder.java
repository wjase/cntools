package com.cybernostics.lib.gui.border;

/*
 * Copyright 2008 Sun Microsystems, Inc. All Rights Reserved. Redistribution and use in source and
 * binary forms, with or without modification, are permitted provided that the following conditions
 * are met: - Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. - Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. - Neither the name of Sun Microsystems nor
 * the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.cybernostics.lib.gui.ColorUtilities;

/**
 * 
 * @author Administrator
 */
public class RoundedBorder implements Border, RendersBackgroundBorder
{

	private final int cornerRadius;

	public RoundedBorder()
	{
		this( 10 );
	}

	public RoundedBorder( int cornerRadius )
	{
		this.cornerRadius = cornerRadius;
	}

	public Insets getBorderInsets( Component c )
	{
		return getBorderInsets(
			c,
			new Insets( cornerRadius, cornerRadius, cornerRadius, cornerRadius ) );
	}

	public Insets getBorderInsets( Component c, Insets insets )
	{
		return new Insets( cornerRadius,
			cornerRadius,
			cornerRadius,
			cornerRadius );

		// insets.top = insets.bottom = cornerRadius / 2;
		// insets.left = insets.right = 1;
		// return insets;
	}

	public boolean isBorderOpaque()
	{
		return false;
	}

	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON );

		Color color = ColorUtilities.deriveColorHSB(
			c.getBackground(),
			0.0f,
			0.0f,
			-.3f );

		g2.setColor( ColorUtilities.deriveColorAlpha(
			color,
			40 ) );
		g2.drawRoundRect(
			x,
			y + 2,
			width - 1,
			height - 3,
			cornerRadius,
			cornerRadius );
		g2.setColor( ColorUtilities.deriveColorAlpha(
			color,
			90 ) );
		g2.drawRoundRect(
			x,
			y + 1,
			width - 1,
			height - 2,
			cornerRadius,
			cornerRadius );
		g2.setColor( ColorUtilities.deriveColorAlpha(
			color,
			255 ) );
		g2.drawRoundRect(
			x,
			y,
			width - 1,
			height - 1,
			cornerRadius,
			cornerRadius );

		g2.dispose();
	}

	public void paintBackground( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		Graphics2D g2 = (Graphics2D) g;
		Color fg = g2.getColor();
		g2.setColor( c.getBackground() );

		g2.fillRoundRect(
			x,
			y + 2,
			width - 1,
			height - 3,
			cornerRadius,
			cornerRadius );
		g2.setColor( fg );

	}

	@SuppressWarnings("serial")
	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "BorderTest" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setSize(
			400,
			400 );
		jf.getContentPane()
			.setLayout(
				new FlowLayout() );
		jf.getContentPane()
			.setBackground(
				Color.white );
		JPanel jp = new JPanel()
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see javax.swing.JComponent#paint(java.awt.Graphics)
			 */
			//		@Override
			public void paint( Graphics g )
			{
				( (RendersBackgroundBorder) getBorder() ).paintBackground(
					this,
					g,
					0,
					0,
					getWidth(),
					getHeight() );
				super.paint( g );
			}

		};
		jp.setBackground( Color.blue );

		jp.setPreferredSize( new Dimension( 200, 200 ) );
		jp.setBorder( new RoundedBorder( 50 ) );
		jp.setOpaque( false );
		jf.getContentPane()
			.add(
				jp );
		jf.setVisible( true );
	}

}
