package com.cybernostics.lib.gui.border;

import com.cybernostics.lib.gui.shapeeffects.HaloEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapedBorder;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * 
 * @author Administrator
 */
public class GlowBorder implements Border, ShapedBorder
{

	HaloEffect halo = null;

	private final int width;

	public GlowBorder()
	{
		this( 10, Color.green.brighter()
			.brighter()
			.brighter()
			.brighter() );
	}

	public GlowBorder( int width, Color toPaint )
	{
		this.width = width;
		halo = new HaloEffect( width, toPaint );
	}

	@Override
	public Insets getBorderInsets( Component c )
	{
		return getBorderInsets(
			c,
			new Insets( width, width, width, width ) );
	}

	public Insets getBorderInsets( Component c, Insets insets )
	{
		return new Insets( width, width, width, width );

		// insets.top = insets.bottom = cornerRadius / 2;
		// insets.left = insets.right = 1;
		// return insets;
	}

	@Override
	public boolean isBorderOpaque()
	{
		return false;
	}

	@Override
	public void paintBorder( Component c,
		Graphics g,
		int x,
		int y,
		int width,
		int height )
	{
		int w = this.width;
		Graphics2D g2 = (Graphics2D) g.create();
		Rectangle2D r = new Rectangle2D.Double( x + w,
			y + w,
			width - w - w,
			height - w - w );
		halo.draw(
			g2,
			r );
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
		JPanel jp = new ShapedPanel();
		jp.setBackground( Color.blue );

		jp.setPreferredSize( new Dimension( 200, 200 ) );
		jp.setBorder( new GlowBorder() );
		jp.setOpaque( true );
		jf.getContentPane()
			.add(
				jp );
		jf.getContentPane()
			.setBackground(
				Color.black );
		jf.setVisible( true );
	}

	@Override
	public Shape getBorderClipShape( Component c )
	{
		int w = this.width;
		Rectangle r = c.getBounds();
		Rectangle2D clipRect = new Rectangle2D.Double( w,
			w,
			r.width - w - w,
			r.height - w - w );
		return clipRect;
	}

	@Override
	public Shape getBackgroundClipShape( Component c )
	{
		return getBorderClipShape( c );
	}

}
