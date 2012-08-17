package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jasonw
 */
public class ScalableIconPanel extends JPanel
{

	public static void main( String[] args )
	{
		JFrame jf = new JFrame();
		ScalableIconPanel sp = new ScalableIconPanel( NoImageIcon.get() );
		sp.setBorder( BorderFactory.createLineBorder(
			Color.yellow,
			20 ) );
		jf.setContentPane( sp );
		jf.setVisible( true );
		jf.pack();

		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	ScalableIcon toRender = null;

	public ScalableIcon getIcon()
	{
		return toRender;
	}

	public ScalableIconPanel()
	{
	}

	public ScalableIconPanel( ScalableIcon si )
	{
		toRender = si;
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );
		if (toRender == null)
		{
			return;
		}
		Insets i = getInsets();
		StateSaver saver = StateSaver.wrap( g );
		try
		{
			toRender.paintIcon(
				this,
				g,
				i.left,
				i.top );
		}
		finally
		{
			saver.restore();
		}
	}

	Dimension dMin = null;

	@Override
	public Dimension getMinimumSize()
	{
		Insets i = getInsets();
		if (dMin == null)
		{
			if (toRender == null)
			{
				return super.getMinimumSize();
			}
			dMin = new Dimension( toRender.getIconWidth() + ( i.left + i.right ),
				toRender.getIconHeight()
					+ ( i.top + i.bottom ) );
		}
		return dMin;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return getMinimumSize();
	}

	public void setIcon( ScalableIcon si )
	{
		toRender = si;
		dMin = null;
		invalidate();

	}
}
