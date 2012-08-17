package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.gui.control.SoundToolTipButton;
import com.cybernostics.lib.media.SoundEffect;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.Icon;

public class RoundRectButton extends SoundToolTipButton
{

	static Dimension mySize = new ScreenRelativeDimension( 0.05f, 0.05f );
	public static float[] BLUR =
	{ 0.10f, 0.10f, 0.10f, 0.10f, 0.30f, 0.10f, 0.10f, 0.10f, 0.10f };
	/**
	 * 
	 */
	private static final long serialVersionUID = 188853479985291217L;
	//	// Test routine.
	//	public static void main(String[] args)
	//	{
	//		ToolTipManager.sharedInstance().setLightWeightPopupEnabled( true );
	//		NimbusLook.set(  );
	//
	//		
	//        ResourceFinder finder = ResourceFinder.get(RoundRectButton.class);
	//
	//		// Create a button with the label "Jackpot".
	//		ImageIcon blueIcon = null;
	//		try
	//		{
	//			blueIcon = ImageLoader. ResLoader.current.loadIcon( finder.getResource("images/elephant.png") );
	//		}
	//		catch ( Exception e )
	//		{
	//			// TODO Auto-generated catch block
	//			UnhandledExceptionManager.handleException( e );
	//		}
	//		JButton bluebutton = new RoundRectButton( "", blueIcon, "Blue", "Blue" );
	//		JButton pinkbutton = new RoundRectButton( "Go Pink", null, "Pink", "Pink" );
	//		JButton graybutton = new RoundRectButton( "Go Gray", null, "Gray", "Gray" );
	//		JButton graybutton2 = new RoundRectButton( "Go Gray", null, "Gray", "Gray" );
	//
	//		InputStream in = pinkbutton.getClass().getResourceAsStream( "sound/red.wav" );
	//		assert ( in != null );
	//
	//		// Create a frame in which to show the button.
	//		JFrame frame = new JFrame();
	//		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	//		frame.getContentPane().setBackground( Color.gray );
	//		frame.getContentPane().add( bluebutton );
	//		frame.getContentPane().add( pinkbutton );
	//		frame.getContentPane().add( graybutton );
	//		frame.getContentPane().add( graybutton2 );
	//		frame.getContentPane().setLayout( new FlowLayout() );
	//		frame.setSize( 150, 150 );
	//		frame.setVisible( true );
	//	}
	private Color myColor = new Color( 0, 100, 0 ); // Color
	// ;
	// .
	// blue
	// ;
	private SoundEffect buttonClickSound = DefaultClickSound.get();
	// Hit detection.
	Shape shape;
	Dimension myCalculatedSize = null;

	public RoundRectButton(
		String ButtonText,
		Icon butIcon,
		String TipText,
		String TipSound )
	{
		setToolTipText( TipText );

		setRolloverEnabled( true );

		if (ButtonText != null)
		{
			setText( ButtonText );
			setForeground( Color.darkGray );
		}

		if (butIcon != null)
		{
			setIcon( butIcon );
		}
		else
		{
		}

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
						buttonClickSound.play();
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
	public boolean contains( int x, int y )
	{
		// If the button has changed size, make a new shape object.
		return getShape().contains(
			x,
			y );
	}

	public SoundEffect getButtonClickSound()
	{
		return buttonClickSound;
	}

	/**
	 * @return the myColor
	 */
	public Color getMyColor()
	{
		return myColor;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return getMinimumSize();
	}

	Shape getShape()
	{
		if (( shape == null ) || !shape.getBounds()
			.equals(
				getBounds() ))
		{
			shape = new Ellipse2D.Float( 5, 5, getWidth() - 5, getHeight() - 5 );
		}

		return shape;
	}

	@Override
	public Dimension getSize( Dimension arg0 )
	{
		return super.getSize();
	}

	public void setButtonClickSound( SoundEffect buttonClickSound )
	{
		this.buttonClickSound = buttonClickSound;
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
	// preferredSize
	// getPreferredSize
	/**
	 * @param myColor
	 *            the myColor to set
	 */
	public void setMyColor( Color myColor )
	{
		this.myColor = myColor;
	}
}