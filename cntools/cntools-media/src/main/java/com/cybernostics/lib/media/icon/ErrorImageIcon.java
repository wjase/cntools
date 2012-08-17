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
public class ErrorImageIcon
{

	public static final String ERROR_IMAGE_ID = "errorimage";

	public static void main( String[] args )
	{

		JFrame jf = new JFrame();
		jf.setContentPane( new JLabel( ErrorImageIcon.get() ) );
		jf.setVisible( true );

	}

	static Finder loader = ResourceFinder.get( ErrorImageIcon.class );
	private static AttributedScalableIcon icon = null;

	public static AttributedScalableIcon get()
	{

		if (icon == null)
		{
			try
			{
				icon = AttributedScalableIcon.create( loader.getResource( ERROR_IMAGE_ID
					+ ".svg" ) );
				icon.put(
					"id",
					ERROR_IMAGE_ID );

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
