package com.cybernostics.lib.media.icon;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;

/**
 *
 * @author jasonw
 */
public class ActionIconLoader
{

	public static void setIcon( final Action act,
		URL toLoad,
		final IconLoadClient optClient )
	{
		if (act == null)
		{
			throw new NullPointerException( "Action is null" );
		}
		SVGIconLoader.load(
			toLoad,
			new IconLoadClient()
		{

			@Override
			public void iconLoaded( ScalableSVGIcon svgsi )
			{
				act.putValue(
					Action.LARGE_ICON_KEY,
					svgsi );
				if (optClient != null)
				{
					optClient.iconLoaded( svgsi );
				}
			}

		} );
	}

	public static void setIcon( Action toSet,
		String iconLocation,
		IconLoadClient optClient )
	{
		try
		{
			setIcon(
				toSet,
				new URL( iconLocation ),
				optClient );
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				ActionIconLoader.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

}
