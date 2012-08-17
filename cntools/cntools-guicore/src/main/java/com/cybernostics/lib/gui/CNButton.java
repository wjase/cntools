package com.cybernostics.lib.gui;

import com.cybernostics.lib.Application.AppResources;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.action.ButtonStyler;
import com.cybernostics.lib.gui.action.ChainableAction;
import com.cybernostics.lib.gui.action.IconOnlyButtonStyler;
import com.cybernostics.lib.gui.shapeeffects.HaloEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import javax.swing.*;

/**
 * @author jasonw
 *
 */
public class CNButton extends JButton implements Comparable< JComponent >
{

	protected static final String TOOL_TIP = "customToolTip";

	/**
	 *
	 */
	private static final long serialVersionUID = -3052628185565986947L;

	public CNButton()
	{
	}

	/**
	 *
	 */
	public CNButton( Icon icon, final String label )
	{
		super( null, icon );

		setName( label );
		setMargin( new Insets( 0, 0, 0, 0 ) );
		setBorder( null );
		addButtonSound();
	}

	public CNButton( Action a )
	{
		super( a );
		System.out.println( a.getValue( Action.NAME ) );
		addButtonSound();
		if (a.getValue( ButtonStyler.STYLER ) != null)
		{
			( (ButtonStyler) a.getValue( ButtonStyler.STYLER ) ).apply( this );
		}
		else
		{
			IconOnlyButtonStyler.get()
				.apply(
					this );
		}

	}

	@Override
	public void setAction( Action a )
	{
		super.setAction( a );
		Object name = a.getValue( ChainableAction.ID_PROPERTY );
		if (name == null)
		{
			name = a.getValue( ChainableAction.NAME );
		}
		if (name != null)
		{
			setName( name.toString() );
		}
	}

	public static final String CLICK_SOUND = "click";

	public static SingletonInstance< SoundEffect > defaultClick = new SingletonInstance< SoundEffect >()
	{

		@Override
		protected SoundEffect createInstance()
		{
			URL snd = AppResources.getResource( "sound/click.mp3" );
			if (snd != null)
			{
				return new SoundEffect( snd, 0.8 );
			}
			return null;
		}

	};

	private static final ActionListener clickSoundActionListener = new ActionListener()
	{

		@Override
		public void actionPerformed( ActionEvent e )
		{
			CNButton b = (CNButton) e.getSource();
			SoundEffect clickSound = (SoundEffect) b.getClientProperty( CLICK_SOUND );
			if (clickSound != null)
			{
				clickSound.play();
			}
			else
			{
				SoundEffect click = defaultClick.get();
				if (click != null)
				{
					click.play();
				}
			}

		}

	};

	private void addButtonSound()
	{
		addActionListener( clickSoundActionListener );
	}

	private TriggerToolTip toolTip = new TriggerToolTip();

	@Override
	public JToolTip createToolTip()
	{
		//		Object tooltip = getClientProperty( TOOL_TIP );
		//
		//		if (tooltip != null)
		//		{
		//			return (JToolTip) tooltip;
		//	
		toolTip.setComponent( this );
		return toolTip;
	}

	private ShapeEffect halo = new HaloEffect( 5, Color.green.brighter()
		.brighter()
		.brighter() );

	public ShapeEffect getBackgroundPainter()
	{
		return backgroundEffect;
	}

	public void setBackgroundPainter( ShapeEffect backgroundEffect )
	{
		this.backgroundEffect = backgroundEffect;
	}

	private ShapeEffect backgroundEffect = null;

	@Override
	protected void paintComponent( Graphics g )
	{

		int width = getWidth();
		int height = getHeight();
		if (getModel().isPressed())
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.scale(
				.95,
				.95 );
			int x = (int) ( width * 0.05 );
			int y = (int) ( height * 0.05 );
			g.translate(
				x,
				y );
		}

		if (backgroundEffect != null)
		{
			backgroundEffect.draw(
				(Graphics2D) g,
				new Rectangle2D.Double( 0, 0, getWidth(), getHeight() ) );
		}

		try
		{
			super.paintComponent( g );
		}
		catch (Exception e)
		{
			System.out.println( "Error painting " + getName() );
			UnhandledExceptionManager.handleException( e );
		}

		if (getModel().isSelected())
		{
			halo.draw(
				(Graphics2D) g,
				new Rectangle2D.Double( 0.1 * width,
					0.1 * height,
					0.8 * getWidth(),
																	0.8 * getHeight() ) );
		}

	}

	/*
	 * (non-Javadoc) @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( JComponent o )
	{
		return getName().compareTo(
			o.getName() );

	}

	@Override
	public void setIcon( Icon value )
	{
		super.setIcon( value );
		if (value instanceof ScalableIcon)
		{
			IconSizer.trackSize(
				(ScalableIcon) value,
				this );
		}
		invalidate();
	}

	@Override
	public void setDisabledIcon( Icon value )
	{
		super.setDisabledIcon( value );
		if (value instanceof ScalableIcon)
		{
			IconSizer.trackSize(
				(ScalableIcon) value,
				this );
		}
	}

}
