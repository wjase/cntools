package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.test.JFrameTest;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author jasonw
 */
public class TestIcon implements Icon
{

	public TestIcon( int width, int height )
	{
		dSize.width = width;
		dSize.height = height;
	}

	private Dimension dSize = new Dimension( 100, 100 );

	private Color borderColor = Color.black;

	private Color fillColor = Color.blue;

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		int maxX = x + dSize.width;
		int maxY = y + dSize.height;
		g.setColor( fillColor );
		g.fillRect(
			x,
			y,
			dSize.width,
			dSize.height );
		int[] xpoints =
		{ x, maxX, x + ( dSize.width / 2 ) };
		int[] ypoints =
		{ maxY, maxY, y };
		g.setColor( borderColor );
		g.drawPolygon(
			xpoints,
			ypoints,
			3 );
	}

	@Override
	public int getIconWidth()
	{
		return dSize.width;
	}

	@Override
	public int getIconHeight()
	{
		return dSize.height;
	}

	public static void main( String[] args )
	{
		JFrameTest jf = JFrameTest.create( "test" );

		JLabel jl = new JLabel( new TestIcon( 100, 20 ) );
		jf.getContentPane()
			.add(
				jl );
		GUIEventThread.show( jf );

	}

}
