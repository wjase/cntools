package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;

/**
 * @author jasonw
 *
 */
public class IconSizer
{

	public static void fitToParent( CNButton cb )
	{
		ScalableIcon si = (ScalableIcon) cb.getIcon();
		if (si != null)
		{
			trackSize(
				si,
				cb );
		}
	}

	private JButton managed = null;

	private ScalableIcon toSize = null;

	private static String SIZER = "SIZER";

	public IconSizer( ScalableIcon icon, JButton but )
	{
		if (but.getClientProperty( SIZER ) != null)
		{
			return;
		}
		managed = but;
		this.toSize = icon;
		//but.setMinimumSize( icon.getPreferredSize() );

		managed.putClientProperty(
			SIZER,
			this );
		icon.addPropertyChangeListener( new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				managed.repaint();

			}

		} );

		if (but.isVisible())
		{
			icon.setSize( getButtonMaxIconDimension( but ) );
		}
		else
		{
			new WhenMadeVisible( but )
			{

				@Override
				public void doThis( AWTEvent e )
				{
					toSize.setSize( getButtonMaxIconDimension( managed ) );
				}

			};

		}

		new WhenResized( but )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				toSize.setSize( getButtonMaxIconDimension( managed ) );
			}

		};

	}

	public static IconSizer trackSize( ScalableIcon icon, final JButton but )
	{
		return new IconSizer( icon, but );
	}

	private static Dimension getButtonMaxIconDimension( JButton but )
	{
		// Insets i = but.getInsets();
		Dimension d = but.getSize();
		// d.width -= ( i.left + i.right );
		// d.height -= ( i.top + i.bottom );
		return d;
	}

}
