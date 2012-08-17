package com.cybernostics.lib.svg.editor;

import java.awt.Graphics;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.Dimension;

/**
 * @author jasonw
 * 
 */
public class ImagePanel extends JPanel
{

	private static final long serialVersionUID = -96170763595699820L;
	private ScalableIcon toRender = null;

	public void setToRender( ScalableIcon toRender )
	{
		this.toRender = toRender;
	}

	public ImagePanel( ScalableIcon icon )
	{

		this.toRender = icon;
		setOpaque( false );

		new WhenResized( this )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				toRender.setSize( getSize() );
				repaint(
					0,
					0,
					getWidth(),
					getHeight() );
			}
		};

		setMaximumSize( new Dimension( 10000, 10000 ) );
		setMinimumSize( new Dimension( 80, 80 ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paintComponent( Graphics g )
	{
		StateSaver saver = StateSaver.wrap( g );
		try
		{
			toRender.paintIcon(
				this,
				g,
				0,
				0 );
		}
		finally
		{
			saver.restore();
		}
		super.paintComponent( g );
	}
}
