package com.cybernostics.lib.gui.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class OvalBorder implements Border
{
	protected int m_w = 6;
	protected int m_h = 6;
	protected Color m_topColor = Color.white;
	protected Color m_bottomColor = Color.green;
	protected BasicStroke wideStroke = new BasicStroke( 8 );

	public OvalBorder()
	{
		m_w = 6;
		m_h = 6;
	}

	public OvalBorder( int w, int h )
	{
		m_w = w;
		m_h = h;
	}

	public OvalBorder( int w, int h, Color topColor, Color bottomColor )
	{
		m_w = w;
		m_h = h;
		m_topColor = topColor;
		m_bottomColor = bottomColor;
	}

	public Insets getBorderInsets( Component c )
	{
		return new Insets( m_h, m_w, m_h, m_w );
	}

	public boolean isBorderOpaque()
	{
		return true;
	}

	public void paintBorder( Component c, Graphics g, int x, int y, int w, int h )
	{

		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON );
		g2d.setStroke( wideStroke );

		w--;
		h--;
		g.setColor( m_topColor );
		g.drawLine(
			x,
			y + h - m_h,
			x,
			y + m_h );
		g.drawArc(
			x,
			y,
			2 * m_w,
			2 * m_h,
			180,
			-90 );
		g.drawLine(
			x + m_w,
			y,
			x + w - m_w,
			y );
		g.drawArc(
			x + w - 2 * m_w,
			y,
			2 * m_w,
			2 * m_h,
			90,
			-90 );

		g.setColor( m_bottomColor );
		g.drawLine(
			x + w,
			y + m_h,
			x + w,
			y + h - m_h );
		g.drawArc(
			x + w - 2 * m_w,
			y + h - 2 * m_h,
			2 * m_w,
			2 * m_h,
			0,
			-90 );
		g.drawLine(
			x + m_w,
			y + h,
			x + w - m_w,
			y + h );
		g.drawArc(
			x,
			y + h - 2 * m_h,
			2 * m_w,
			2 * m_h,
			-90,
			-90 );
	}

	public static void main( String[] args )
	{
		JFrame frame = new JFrame( "Custom Border: OvalBorder" );
		JLabel label = new JLabel( "OvalBorder" );
		( (JPanel) frame.getContentPane() ).setBorder( new CompoundBorder( new EmptyBorder( 10,
			10,
			10,
			10 ),
			new OvalBorder( 10, 10 ) ) );
		frame.getContentPane()
			.add(
				label );
		frame.setBounds(
			0,
			0,
			300,
			150 );
		frame.setVisible( true );
	}
}