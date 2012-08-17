package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author jasonw
 */
public class LoadingIcon
{

	public static final String LOADING_IMAGE_ID = "loading";

	public static void main( String[] args )
	{

		JFrame jf = new JFrame();
		jf.setContentPane( new JLabel( LoadingIcon.get() ) );
		jf.setVisible( true );

	}

	static Finder loader = ResourceFinder.get( LoadingIcon.class );
	private static AttributedScalableIcon icon = null;

	public static AttributedScalableIcon get()
	{
		if (icon == null)
		{
			try
			{
				ScalableSVGIcon ssi = new ScalableSVGIcon( loader.getResource( LOADING_IMAGE_ID
					+ ".svg" ) );
				icon = new AttributedScalableIcon( ssi );
				icon.put(
					"id",
					LOADING_IMAGE_ID );

			}
			catch (ResourceFinderException ex)
			{
				throw new Error( ex );
			}
			icon.waitLoad();
		}
		return icon;
	}
}
