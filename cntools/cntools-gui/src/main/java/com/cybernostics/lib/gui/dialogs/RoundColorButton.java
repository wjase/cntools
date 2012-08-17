package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.concurrent.AppTaskQueue;
import com.cybernostics.lib.concurrent.CallableWorkerTask;
import com.cybernostics.lib.svg.editor.SVGColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import com.cybernostics.lib.gui.control.SoundToolTipButton;
import com.cybernostics.lib.media.SoundEffect;

public class RoundColorButton extends SoundToolTipButton
{

	static Dimension mySize = new Dimension( 60, 40 );
	public static float[] BLUR =
	{ 0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f };
	private final int inset = 10;

	public RoundColorButton( SVGColor eachColor )
	{
		this( eachColor, eachColor.getSVGText() );
	}

	@Override
	public Dimension getPreferredSize()
	{
		return mySize;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 188853479985291217L;
	private Color myColor;
	private SoundEffect clickSound = DefaultClickSound.get();

	public SoundEffect getClickSound()
	{
		return clickSound;
	}

	public void setClickSound( SoundEffect clickSound )
	{
		this.clickSound = clickSound;
	}

	public RoundColorButton( Color theColor, String ColourName )
	{
		setToolTipText( ColourName );
		setName( ColourName );

		myColor = theColor;

		setRolloverEnabled( true );

		// These statements enlarge the button so that it
		// becomes a circle rather than an oval.
		Dimension size = getPreferredSize();
		size.width = size.height = Math.max(
			size.width,
			size.height );
		setPreferredSize( size );

		// This call causes the JButton not to paint the background.
		// This allows us to paint a round background.
		setContentAreaFilled( false );
		setBorderPainted( false );

		addActionListener( new ActionListener()
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{

				AppTaskQueue.submitTask( new CallableWorkerTask( "beep" )
				{

					@Override
					protected Object doTask() throws Exception
					{
						clickSound.play();
						return null;
					}
				} );
			}
		} );

	}

	// Paint the round background and label.
	@Override
	protected void paintComponent( Graphics g )
	{
		if (getModel().isRollover())
		{
			g.setColor( Color.lightGray );
			g.fillOval(
				0,
				0,
				getSize().width,
				getSize().height );
		}
		if (getModel().isArmed())
		{
			// You might want to make the highlight color
			// a property of the RoundButton class.
			g.setColor( Color.lightGray );
			g.fillOval(
				5,
				5,
				getSize().width - 10,
				getSize().height - 10 );
		}
		else
		{
			paintGradientButton( g );
			// g.setColor(myColor);

			// g.fillOval(5, 5, getSize().width - 10, getSize().height - 10);
		}

		// This call will paint the label and the focus rectangle.
		super.paintComponent( g );
	}

	protected void paintGradientButton( Graphics g )
	{
		super.paintComponent( g );

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON );

		int width = getWidth();
		int height = getHeight();
		int buttonHeight = height - ( inset * 2 );
		int buttonWidth = width - ( inset * 2 );

		g.setColor( myColor );
		g.fillOval(
			inset,
			inset,
			buttonWidth,
			buttonHeight );

	}

	// Paint the border of the button using a simple stroke.
	@Override
	protected void paintBorder( Graphics g )
	{
		g.setColor( getForeground() );
		g.drawOval(
			5,
			5,
			getSize().width - 10,
			getSize().height - 10 );
	}

	// Hit detection.
	Shape shape;

	@Override
	public boolean contains( int x, int y )
	{
		// If the button has changed size, make a new shape object.
		if (( shape == null ) || !shape.getBounds()
			.equals(
				getBounds() ))
		{
			shape = new Ellipse2D.Float( 5, 5, getWidth() - 5, getHeight() - 5 );
		}
		return shape.contains(
			x,
			y );
	}

	/**
	 * @return the myColor
	 */
	public Color getMyColor()
	{
		return myColor;
	}

	/**
	 * @param myColor
	 *            the myColor to set
	 */
	public void setMyColor( Color myColor )
	{
		this.myColor = myColor;
		repaint();
	}

	@Override
	public Dimension getMaximumSize()
	{
		return mySize;
	}

	@Override
	public Dimension getSize( Dimension arg0 )
	{
		return mySize;
	}
}