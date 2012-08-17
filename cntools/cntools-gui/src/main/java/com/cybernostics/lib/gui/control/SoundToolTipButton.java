package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.media.SoundEffect;
import javax.swing.JButton;
import javax.swing.JToolTip;

public class SoundToolTipButton extends JButton
{

	SoundEffect tipSound = null;

	JToolTip myToolTip = null;

	public void setTipSound( SoundEffect tipSound )
	{
		if (tipSound != null)
		{
			this.tipSound = tipSound;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4689649844231529950L;

	@Override
	public JToolTip createToolTip()
	{
		if (myToolTip == null)
		{
			if (tipSound != null)
			{

				myToolTip = new SounderToolTip( tipSound );
			}
			else
			{
				return super.createToolTip();
			}

		}

		return myToolTip;
	}
}
