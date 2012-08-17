package com.cybernostics.lib.gui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.cybernostics.lib.gui.graphics.StateSaver;

/**
 * 
 * @author Administrator
 */
public class RoundedPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3412460026410492046L;

	public static void main( String args[] )
	{
		JFrame frame = new JFrame();
		RoundedPanel p = new RoundedPanel( new BorderLayout(), 40 );
		JPanel p2 = new JPanel();
		frame.getContentPane()
			.setBackground(
				Color.green );
		p2.setBackground( Color.blue );
		p.add( p2 );
		frame.getContentPane()
			.add(
				p );
		frame.setSize(
			200,
			200 );
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private final int cornerRadius;

	private transient final RoundRectangle2D.Float roundBounds;

	public RoundedPanel()
	{
		this( 10 );
	}

	public RoundedPanel( int cornerRadius )
	{
		this( new FlowLayout(), cornerRadius );
	}

	public RoundedPanel( LayoutManager layout )
	{
		this( layout, 10 );
	}

	public RoundedPanel( LayoutManager layout, int cornerRadius )
	{
		super( layout );
		this.cornerRadius = cornerRadius;
		this.roundBounds = new RoundRectangle2D.Float( 0,
			0,
			getWidth(),
			getHeight(),
			cornerRadius,
			this.cornerRadius );
		setOpaque( false );
	}

	protected void prepareRoundedClip( Graphics2D g2 )
	{
		Dimension size = getSize();
		roundBounds.width = size.width - 1;
		roundBounds.height = size.height - 1;
		Area boundsClip = new Area( roundBounds );
		Shape clipShape = g2.getClip();
		if (clipShape != null)
		{
			boundsClip.intersect( new Area( clipShape ) );
		}

		g2.setClip( boundsClip );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint( Graphics g )
	{
		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			prepareRoundedClip( g2 );
			super.paint( g2 );

		}
		finally
		{
			g2.restore();
		}
	}

}
