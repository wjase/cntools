package com.cybernostics.lib.gui.panel;

import com.cybernostics.lib.gui.border.GradientDropShadowBorder;
import com.cybernostics.lib.gui.control.SoundToolTipButton;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import com.cybernostics.lib.media.icon.ScreenRelativeIconSizer;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 *
 * @author jasonw
 */
public class DefaultImagePanelRenderer implements ImagePanelRenderer
{

	@Override
	public AbstractButton getImagePaneButton( Object item )
	{
		SoundToolTipButton btnIcon = new SoundToolTipButton();
		btnIcon.setModel( new JToggleButton.ToggleButtonModel() );

		if (item instanceof URL)
		{
			URL url = (URL) item;
			AttributedScalableIcon asi = AttributedScalableIcon.create( url );

			btnIcon.setIcon( asi );
			//btnIcon.setSelectedIcon( new SelectedIcon( si ) );
			ScreenRelativeIconSizer.setSize( asi );
			btnIcon.putClientProperty(
				OBJ_PROPERTY,
				asi );
			btnIcon.setMargin( new Insets( 3, 3, 3, 3 ) );
		}

		if (item instanceof ImageIcon)
		{
			ImageIcon ii = (ImageIcon) item;
			AttributedScalableIcon asi = new AttributedScalableIcon( new ScalableImageIcon( ii ) );
			ScreenRelativeIconSizer.setSize( asi );
			try
			{
				asi.setURL( new URL( ii.getDescription() ) );
			}
			catch (MalformedURLException ex)
			{
				Logger.getLogger(
					DefaultImagePanelRenderer.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
			btnIcon.setPreferredSize( new Dimension( ii.getIconWidth() + 10,
				ii.getIconHeight() + 10 ) );
			btnIcon.putClientProperty(
				OBJ_PROPERTY,
				asi );
		}

		if (item instanceof AttributedScalableIcon)
		{
			AttributedScalableIcon asi = (AttributedScalableIcon) item;
			btnIcon.setIcon( asi );
			btnIcon.setPreferredSize( new Dimension( asi.getIconWidth() + 10,
				asi.getIconHeight() + 10 ) );
			btnIcon.putClientProperty(
				OBJ_PROPERTY,
				item );
		}
		btnIcon.setBorder( new GradientDropShadowBorder() );
		btnIcon.setMinimumSize( btnIcon.getPreferredSize() );
		btnIcon.setMaximumSize( btnIcon.getPreferredSize() );

		return btnIcon;
	}

	@Override
	public Object getObjectPayload( AbstractButton ab )
	{
		return ab.getClientProperty( OBJ_PROPERTY );
	}
}
