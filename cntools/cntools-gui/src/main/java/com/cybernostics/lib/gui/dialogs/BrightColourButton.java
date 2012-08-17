package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.gui.ScreenRelativeDimension;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.control.SoundToolTipButton;
import com.cybernostics.lib.media.SoundEffect;

public class BrightColourButton extends SoundToolTipButton
{

	static ScreenRelativeDimension mySize = new ScreenRelativeDimension( .1f,
		.1f );

	public static float[] BLUR =
	{ 0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f };

	/**
	 *
	 */
	private static final long serialVersionUID = 188853479985291217L;

	private Color myColor = new Color( 0, 100, 0 ); // Color
	// ;
	// .
	// blue
	// ;

	private SoundEffect clickSound = DefaultClickSound.get();
	// Hit detection.

	Shape shape;

	Dimension myCalculatedSize = null;

	public BrightColourButton(
		String ButtonText,
		Icon butIcon,
		String TipText,
		String TipSound )
	{
		//super( TipSound, TipText );

		setToolTipText( TipText );

		// setBorder( new EmptyBorder( 5, 5, 5, 5 ) );
		setRolloverEnabled( true );

		if (ButtonText != null)
		{
			setText( ButtonText );
		}

		if (butIcon != null)
		{
			setIcon( butIcon );
			// setMinimumSize(new Dimension((int)
			// Math.round(butIcon.getIconWidth() * 1.5), (int)
			// Math.round(butIcon
			// .getIconHeight() * 1.5)));
		}
		else
		{
			// setPreferredSize(mySize);
		}

		// This call causes the JButton not to paint the background.
		// This allows us to paint a round background.
		setContentAreaFilled( false );

		// setBorderPainted( false );

		// Set the UI
		updateUI();

		addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent e )
			{
				final Timer aTimer = new Timer();
				TimerTask tt = new TimerTask()
				{

					@Override
					public void run()
					{
						clickSound.play();
						aTimer.cancel();
					}

				};

				aTimer.schedule(
					tt,
					1 ); // do it now

			}

		} );

	}

	@Override
	protected void paintComponent( Graphics g )
	{
		StateSaver g2 = StateSaver.wrap( g );

		try
		{
			if (getModel().isPressed())
			{
				g2.translate(
					3,
					3 );
			}
			super.paintComponent( g2 );

		}
		finally
		{
			g2.restore();
		}
	}

	// // Paint the round background and label.
	// @Override
	// protected void paintComponent( Graphics g )
	// {
	//    
	// Graphics2D g2 = ( Graphics2D ) g;
	// if ( getModel().isPressed() )
	// {
	// g2.translate( 3, 3 );
	// }
	// }
	//
	// g2.setColor( Color.gray );
	//
	// Area clipArea = new Area( getShape() );
	// g2.setClip( clipArea );
	// scaleForSize( clipArea, 5.0f, false );
	// Stroke saved = g2.getStroke();
	// g2.setStroke( new BasicStroke( 6 ) );
	// g2.fillRect( 0, 0, getWidth(), getHeight() );
	// g2.setColor( Color.magenta );
	// g2.draw( clipArea );
	// g2.setColor( Color.lightGray );
	//
	// scaleForSize( clipArea, 5.0f, false );
	// g2.draw( clipArea );
	// g2.setColor( Color.cyan );
	// scaleForSize( clipArea, 5.0f, false );
	// g2.draw( clipArea );
	// g2.setStroke( saved );
	//
	// //
	// // AffineTransform shrinker = new AffineTransform();
	// // shrinker.scale( .9, .9 );
	// // shrinker.translate( 5, 5 );
	//
	// // // This call will paint the label and the focus rectangle.
	// // super.paintComponent( g );
	//
	// super.paintComponent( g );
	// }
	//
	// // Paint the border of the button using a simple stroke.
	// @Override
	// protected void paintBorder( Graphics g )
	// {
	// // g.setColor(getForeground());
	// // g.drawRoundRect(5, 5, getSize().width - 10, getSize().height - 10, 5,
	// 5);
	// }
	@Override
	public boolean contains( int x, int y )
	{
		// If the button has changed size, make a new shape object.
		return getShape().contains(
			x,
			y );
	}

	public SoundEffect getButtonClickSound()
	{
		return clickSound;
	}

	/**
	 * @return the myColor
	 */
	public Color getMyColor()
	{
		return myColor;
	}

	Shape getShape()
	{
		if (( shape == null ) || !shape.getBounds()
			.equals(
				getBounds() ))
		{
			shape = new RoundRectangle2D.Float( 5,
				5,
				getWidth() - 5,
				getHeight() - 5,
				15,
				15 );
		}

		return shape;
	}

	@Override
	public Dimension getSize( Dimension arg0 )
	{
		return super.getSize();
	}

	public void scaleForSize( Area toScale, float size, boolean outer )
	{
		float scalex = 1.0f - ( size * ( outer ? 1 : 2 ) / getWidth() );
		float scaley = 1.0f - ( size * ( outer ? 1 : 2 ) / getHeight() );
		toScale.transform( AffineTransform.getScaleInstance(
			scalex,
			scaley ) );
		if (!outer)
		{
			toScale.transform( AffineTransform.getTranslateInstance(
				size,
				size ) );
		}

	}

	public void setButtonClickSound( SoundEffect buttonClickSound )
	{
		this.clickSound = buttonClickSound;
	}

	/**
	 * @param myColor the myColor to set
	 */
	public void setMyColor( Color myColor )
	{
		this.myColor = myColor;
	}
	// // getMinimumSize
	// @Override
	// public Dimension getMinimumSize()
	// {
	// if ( myCalculatedSize == null )
	// {
	// Icon myIcon = getIcon();
	// FontMetrics fm = getFontMetrics( getFont() );
	// int h =
	// Math.max( ( myIcon != null ? myIcon.getIconHeight() : 0 ), fm.getHeight()
	// * 2 ) + getInsets().top
	// + getInsets().bottom;
	// int w =
	// ( int ) ( fm.stringWidth( getText() ) * 1.1 ) + getInsets().left +
	// getInsets().right
	// + getIconTextGap() + ( myIcon != null ? myIcon.getIconWidth() : 0 );
	// // Preserve minimum aspect ratio
	// w = Math.max( w, ( int ) ( h * 1.3 ) );
	// myCalculatedSize = new Dimension( w, h );
	// }
	//
	// if ( myCalculatedSize.width < mySize.width || myCalculatedSize.height <
	// mySize.height )
	// {
	// return mySize;
	// }
	// return myCalculatedSize;
	// }
	//
	// // preferredSize
	//
	// // getPreferredSize
	//
	// @Override
	// public Dimension getPreferredSize()
	// {
	// return getMinimumSize();
	// }
}