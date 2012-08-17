package com.cybernostics.lib.gui.control;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.paramaterised.SawToothFunction;
import com.cybernostics.lib.animator.track.AdapterAnimatorTrack;
import com.cybernostics.lib.animator.track.AnimatedProperty;
import com.cybernostics.lib.animator.track.ReflectionPropertyAnimatorTrack;
import com.cybernostics.lib.gui.draw.GraphicsUtil;
import com.cybernostics.lib.gui.shapeeffects.GlowEffect;
import com.cybernostics.lib.gui.graphics.StateSaver;

/**
 * Implements a transparent glowing text togglebutton
 *
 * @author jasonw
 *
 */
public class GlowTextButton extends JToggleButton
{

	static Sequencer buttonEffectSequencer = Sequencer.get();

	static
	{
		buttonEffectSequencer.start();
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -4580426683591119830L;

	private Color glowColor = null;

	private int glowWidth = 5;

	private double glowAlpha = 1.0f;

	public double getGlowAlpha()
	{
		return glowAlpha;
	}

	public void setGlowAlpha( double glowAlpha )
	{
		this.glowAlpha = glowAlpha;
		repaint();

	}

	public int getGlowWidth()
	{
		return glowWidth;
	}

	public void setGlowWidth( int glowWidth )
	{
		this.glowWidth = glowWidth;
		repaint();
	}

	// This will animate the glowing when selected
	//ReflectionPropertyAnimatorTrack< Double> animatedGlow = new ReflectionPropertyAnimatorTrack< Double>( "glowFlash", new SawToothFunction( 1.0f, 0.3f ),
	// this, "setGlowAlpha", 1000 );
	AdapterAnimatorTrack< Double > animatedGlow = new AdapterAnimatorTrack< Double >( "glowflash",
		1000,
		new SawToothFunction( 1.0f, 0.3f ),
		new AnimatedProperty< Double >()
		{

			@Override
			public void update( Double value )
			{
				setGlowAlpha( value );
			}

		} );

	public GlowTextButton( String text, Color glowColor, int glowWidth )
	{
		setText( text );
		this.glowColor = glowColor;
		this.glowWidth = glowWidth;

		setOpaque( false );
		setContentAreaFilled( false );

		getModel().addItemListener(
			new ItemListener()
		{

			@Override
			public void itemStateChanged( ItemEvent e )
			{
				if (getModel().isSelected())
				{
					if (!animatedGlow.isRunning())
					{
						buttonEffectSequencer.addAndStartTrack( animatedGlow );
					}
				}
				else
				{
					animatedGlow.stop( true );
				}
			}

		} );

		animatedGlow.setPeriodic( true );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paint( Graphics g )
	{
		StateSaver g2 = StateSaver.wrap( g );
		try
		{
			Dimension size = getSize();
			FontMetrics fm = g2.getFontMetrics();

			Insets i = getInsets();

			Rectangle viewRect = new Rectangle( size );

			viewRect.x += i.left;
			viewRect.y += i.top;
			viewRect.width -= ( i.right + viewRect.x );
			viewRect.height -= ( i.bottom + viewRect.y );

			Rectangle iconRect = new Rectangle();
			Rectangle textRect = new Rectangle();

			Font f = getFont();
			g2.setFont( f );
			g2.setColor( getForeground() );

			// layout the text and icon
			SwingUtilities.layoutCompoundLabel(
				this,
				fm,
				getText(),
				getIcon(),
				getVerticalAlignment(),
				getHorizontalAlignment(),
				getVerticalTextPosition(),
				getHorizontalTextPosition(),
				viewRect,
				iconRect,
				textRect,
				getText() == null ? 0 : getIconTextGap() );

			g2.setColor( getForeground() );

			if (model.isArmed() && model.isPressed() || model.isSelected())
			{
			}

			// Paint the Icon
			if (getIcon() != null)
			{
				getIcon().paintIcon(
					this,
					g2,
					iconRect.x,
					iconRect.y );
			}

			// Draw the Text

			int yOffset = ( g2.getFontMetrics().getHeight() )
				- g2.getFontMetrics()
					.getDescent();
			GlowEffect.drawText(
				g2,
				getText(),
				textRect.x,
				textRect.y + yOffset,
				glowColor,
				getModel().isRollover()
					|| getModel().isSelected() ? glowWidth : 0,
				glowAlpha * GraphicsUtil.getAlpha( g2 ) );

			// draw the dashed focus line.
			if (isFocusPainted() && hasFocus())
			{
				( (Graphics2D) g2 ).draw( viewRect );
			}
		}
		finally
		{
			g2.restore();
		}

	}

}
