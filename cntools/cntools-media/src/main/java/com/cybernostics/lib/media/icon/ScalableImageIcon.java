package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author jasonw
 *
 */
public class ScalableImageIcon implements ScalableIcon
{

	Dimension2D desiredSize = null;

	private Icon toRender = null;

	public ScalableImageIcon( BufferedImage image )
	{
		this( new ImageIcon( image ) );
	}

	public ScalableImageIcon( Icon toRender )
	{
		this.toRender = toRender;
		setSize( new Dimension( toRender.getIconWidth(),
			toRender.getIconHeight() ) );
	}

	/**
	 * @param url
	 */
	public ScalableImageIcon( URL url )
	{
		this.toRender = new ImageIcon( url );
		setSize( new Dimension( toRender.getIconWidth(),
			toRender.getIconHeight() ) );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.cybernostics.lib.gui.icon.ScalableIcon#copy()
	 */
	@Override
	public ScalableIcon copy()
	{
		return new ScalableImageIcon( toRender );
	}

	@Override
	public void setSize( Dimension2D d )
	{
		if (( dMin != null )
			&& ( ( d.getWidth() < dMin.width ) || ( d.getHeight() < dMin.height ) ))
		{
			return;
		}
		// System.out.printf("width,height = %d,%d\n",d.width,d.height);
		if (d.getWidth() > 0 && d.getHeight() > 0)
		{
			desiredSize = d;
		}
		firePreferredSizeChanged( d );
	}

	@Override
	public int getIconHeight()
	{
		return (int) desiredSize.getHeight();
	}

	@Override
	public int getIconWidth()
	{
		return (int) desiredSize.getWidth();
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{

		StateSaver g2 = StateSaver.wrap( g );

		try
		{
			// AffineTransform scaler = AffineTransform.getScaleInstance( 1.0f *
			// desiredSize.width / toRender.getIconWidth(), 1.0f
			// * desiredSize.height / toRender.getIconHeight() );
			g2.translate(
				x,
				y );
			g2.scale(
				1.0f * desiredSize.getWidth() / toRender.getIconWidth(),
				1.0f * desiredSize.getHeight()
					/ toRender.getIconHeight() );
			toRender.paintIcon(
				c,
				g2,
				0,
				0 );

		}
		finally
		{
			g2.restore();
		}

		// g.drawImage( scaledVersion, x, y, null );

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.lib.gui.icon.ScalableIcon#addPreferredSizeListener(com
	 * .cybernostics.lib.gui.icon.PreferredSizeListener)
	 */
	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		sizeListeners.add( listener );

	}

	private ArrayList< PreferredSizeListener > sizeListeners = new ArrayList< PreferredSizeListener >();

	private void firePreferredSizeChanged( Dimension2D d )
	{
		for (PreferredSizeListener eachListener : sizeListeners)
		{
			eachListener.preferredSizeChanged( d );
		}
	}

	@Override
	public BufferedImage getImage()
	{
		return (BufferedImage) ( (ImageIcon) toRender ).getImage();
	}

	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension( toRender.getIconWidth(), toRender.getIconHeight() );
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		//TODO?
	}

}
