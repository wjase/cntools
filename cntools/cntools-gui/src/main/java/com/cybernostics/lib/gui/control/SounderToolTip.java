package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.media.SoundEffect;
import java.awt.Graphics;
import javax.swing.JToolTip;

/**
 * @author jasonw
 *
 */
public class SounderToolTip extends JToolTip
{

	SoundEffect toPlay = null;

	/**
	 *
	 */
	private static final long serialVersionUID = -5045161476913943936L;

	@Override
	public void paintComponent( Graphics g )
	{
		if (!getTipText().equals(
			"." ))
		{
			super.paintComponent( g );
		}
		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				if (toPlay != null)
				{
					toPlay.play();
				}
			}

		};
	}

	public SounderToolTip( SoundEffect toPlay )
	{
		this.toPlay = toPlay;
	}

	@Override
	public void updateUI()
	{
		super.updateUI();
	}

}
