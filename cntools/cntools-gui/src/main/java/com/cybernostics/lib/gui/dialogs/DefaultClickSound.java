package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jasonw
 */
public class DefaultClickSound
{

	private static SingletonInstance< SoundEffect > defSound = new SingletonInstance< SoundEffect >()
	{

		@Override
		protected SoundEffect createInstance()
		{
			try
			{
				return new SoundEffect( AppResources.getResource( "sound/click.mp3" ) );
			}
			catch (ResourceFinderException ex)
			{
				Logger.getLogger(
					DefaultClickSound.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			return null;
		}
	};

	public static SoundEffect get()
	{
		return defSound.get();
	}
}
