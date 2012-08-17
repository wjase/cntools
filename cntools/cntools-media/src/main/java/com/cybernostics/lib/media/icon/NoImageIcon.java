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
public class NoImageIcon
{

	public static final String NO_IMAGE_ID = "noimage";

	public static void main( String[] args )
	{

		JFrame jf = new JFrame();
		jf.setContentPane( new JLabel( NoImageIcon.get() ) );
		jf.setVisible( true );

	}

	static Finder loader = ResourceFinder.get( NoImageIcon.class );
	private static AttributedScalableIcon icon = null;

	public static AttributedScalableIcon get()
	{

		if (icon == null)
		{
			try
			{
				icon = AttributedScalableIcon.create( loader.getResource( NO_IMAGE_ID
					+ ".svg" ) );
				icon.put(
					"id",
					NO_IMAGE_ID );

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
