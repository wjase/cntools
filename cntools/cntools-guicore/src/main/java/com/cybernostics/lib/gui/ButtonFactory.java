package com.cybernostics.lib.gui;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.action.CNAppAction;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.gui.icon.GrayableIcon;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.JButton;

public class ButtonFactory
{

	protected static final String SOUND_EFFECT = "clickSound";

	protected static final String TOOL_TIP = "customToolTip";

	private static Dimension defaultSize = new Dimension( 30, 30 );

	public static JButton getButton( String path,
		String Label,
		Dimension desiredSize )
	{
		return getButton(
			AppResources.getResource( path ),
			Label,
			desiredSize );
	}

	public static JButton getButton( URL path,
		String Label,
		Dimension desiredSize )
	{
		if (path == null)
		{
			throw new NullPointerException( Label );
		}
		Dimension sizeToUse = desiredSize != null ? desiredSize : defaultSize;
		Icon icon = null;
		JButton theBut;
		try
		{
			if (path.getPath()
				.endsWith(
					"svg" ))
			{
				ScalableSVGIcon sicon = new ScalableSVGIcon( path );
				sicon.setSize( sizeToUse );
				icon = sicon;
			}
			else
			{
				ScalableImageIcon si = new ScalableImageIcon( path );
				si.setSize( sizeToUse );
				icon = si;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace( System.out );
		}

		if (( icon != null ) && ( icon.getIconWidth() != -1 ))
		{
			theBut = getButton(
				icon,
				Label );// new BrightColourButton( null,
			// icon, Label, Label );
		}
		else
		{
			theBut = getButton(
				icon,
				Label );
		}

		theBut.setOpaque( false );
		theBut.setRolloverEnabled( true );
		theBut.setBackground( new Color( 255, 255, 255, 255 ) );
		theBut.setBorderPainted( false );
		return theBut;

	}

	/**
	 * Create a standard set of Buttons to use in the application
	 *
	 * @param type - the button type to create
	 * @param desiredSize - size of the icon for the button or default size if
	 * null
	 * @return a new Button instance for each call or null if the type is
	 * unknown.
	 */
	public static CNButton getStdButton( StdButtonType type,
		Dimension desiredSize )
	{
		return getButton( IconFactory.getStdIcon( type ) );

	}

	/**
	 * @param stdIcon
	 * @return
	 */
	private static CNButton getButton( Icon stdIcon )
	{
		return getButton(
			stdIcon,
			null );
	}

	public static CNButton getButton( CNAppAction action )
	{
		CNButton jb = getButton(
			action.getIcon(),
			action.getDescription() );
		jb.setAction( action );
		action.getStyler()
			.apply(
				jb );
		return jb;
	}

	public static void setToolTip( JButton jb, TooltipListener listener )
	{
		jb.putClientProperty(
			TriggerToolTip.TOOL_ACTION_PROPERTY,
			listener );
		jb.setToolTipText( "." );
	}

	public static void setSoundEffect( JButton jb, SoundEffect effect )
	{
		jb.putClientProperty(
			SOUND_EFFECT,
			effect );
	}

	public static CNButton getButton( Icon icon, final String label )
	{
		CNButton jb = new CNButton( icon, label );

		jb.setBorder( null );

		jb.setBorderPainted( false );
		jb.setOpaque( false );
		jb.setRolloverEnabled( true );
		jb.setContentAreaFilled( false );
		jb.setFocusPainted( true );
		jb.setBorder( null );

		if (icon != null)
		{
			if (icon instanceof ScalableIcon)
			{
				if (jb.isVisible())
				{
					ScalableIcon svgicon = (ScalableIcon) jb.getIcon();

					//ScreenRelativeIconSizer.setSize( svgicon );
					svgicon.setSize( getMaxIconSize( jb ) );
				}
				//(( SVGIcon ) icon).setPreferredSize( defaultSize );

				new WhenResized( jb )
				{

					@Override
					public void doThis( ComponentEvent e )
					{
						JButton jb = (JButton) e.getComponent();
						ScalableIcon icon = (ScalableIcon) jb.getIcon();
						Dimension d = getMaxIconSize( jb );
						d.width = d.height;
						icon.setSize( d );
						jb.repaint();
					}

				};

			}

			jb.setDisabledIcon( new GrayableIcon( icon ) );

		}
		return jb;
	}

	static Dimension getMaxIconSize( JButton jb )
	{
		Insets i = jb.getInsets();
		Dimension dMax = jb.getPreferredSize();
		dMax.width -= ( i.left + i.right );
		dMax.height -= ( i.top + i.bottom );
		return dMax;
	}

	/**
	 * @param type = type of button to get
	 * @return
	 */
	public static JButton getStdButton( StdButtonType type )
	{
		return getStdButton(
			type,
			null );
	}

}
