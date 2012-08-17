package com.cybernostics.lib.gui.control;

import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;

/**
 *
 * @author jasonw
 */
public class ButtonActionSound
{

	public static void addClickSound( final AbstractButton b )
	{
		final SoundEffect effect = (SoundEffect) b.getAction()
			.getValue(
				"ClickSound" );
		addClickSound(
			b,
			effect );
	}

	public static void addClickSound( final AbstractButton b,
		final SoundEffect effect )
	{
		if (effect != null)
		{
			new WhenClicked( b )
			{

				@Override
				public void doThis( ActionEvent e )
				{
					effect.play();
				}
			};

		}

	}
}
