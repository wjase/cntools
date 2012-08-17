package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.media.image.BitmapMaker;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.svg.SubRegionContainer;
import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import sun.swing.SwingUtilities2;

/**
 * @author jasonw
 *
 */
public class ScalableSVGIcon implements ScalableIcon, SubRegionContainer
{

	private SVGIcon contained;

	//public static final Dimension FILL_PARENT = new Dimension( -1, -1 );
	public static ScalableIcon loadIcon( Finder finder, String toLoad )
	{
		try
		{
			return new ScalableSVGIcon( finder.getResource( toLoad ) );
		}
		catch (ResourceFinderException ex)
		{
			Logger.getLogger(
				ScalableSVGIcon.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		return NoImageIcon.get();

	}

	public static ScalableIcon loadIcon( URL toLoad )
	{
		return new ScalableSVGIcon( toLoad );
	}

	protected ScalableSVGIcon()
	{
	}

	public ScalableSVGIcon( ScalableSVGIcon other )
	{
		this( other.getSvgURI() );
	}

	public ScalableSVGIcon( SVGIcon other )
	{
		this( other.getSvgURI() );
	}

	public ScalableSVGIcon( URI toLoad )
	{
		setSvgURI( toLoad );
	}

	public ScalableSVGIcon( URL toLoad )
	{
		loadFromURL( toLoad );
	}

	Dimension dMin = null;

	@Override
	public void setMinimumSize( Dimension d )
	{
		dMin = d;
	}

	private Dimension dSize = null;

	@Override
	public int getIconHeight()
	{
		return dSize.height;
	}

	@Override
	public int getIconWidth()
	{
		return dSize.width;
	}

	private void loadFromURL( URL toLoad )
	{
		try
		{
			URI uriSvg = SVGCache.getSVGUniverse()
				.loadSVG(
					toLoad );
			setSvgURI( uriSvg );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( new Exception( "Couldn't load "
				+ toLoad,
				e ) );
		}

	}

	public void setSvgURI( final URI svgURI )
	{
		contained = new SVGIcon();
		contained.setAntiAlias( true );
		contained.setSvgURI( svgURI );
		//contained.setScaleToFit( true );
		setFitToParent( true ); // by default
		dSize = new Dimension( contained.getIconWidth(),
			contained.getIconHeight() );
		snapshot = new SVGSnapshot( contained.getSvgUniverse()
			.getDiagram(
				contained.getSvgURI() ) );
		setSize( dSize );

	}

	// repaints any components for which this was ever painted.
	// If the icon is no longer attached to the component it will
	// still be repainted. This may need to be revisited...
	public void repaintClients()
	{
		for (Component eachComponent : toRepaintList)
		{
			eachComponent.repaint();
		}
	}

	public void setPreferredSize( Dimension2D preferredSize )
	{
		Dimension d = new Dimension( (int) preferredSize.getWidth(),
			(int) preferredSize.getHeight() );
		setSize( d );
	}

	/*
	 * (non-Javadoc) @see
	 * com.kitfox.svg.app.beans.SVGIcon#setPreferredSize(java.awt.Dimension)
	 */
	@Override
	public void setSize( Dimension2D preferredSize )
	{
		// if it is null or too small ignore it...
		if (( dMin != null )
			&& ( ( preferredSize.getWidth() < dMin.width ) || ( preferredSize.getHeight() < dMin.height ) ))
		{
			return;
		}

		Dimension toSet = new Dimension( (int) preferredSize.getWidth(),
			(int) preferredSize.getHeight() );
		snapshot.setSize( toSet );
		contained.setPreferredSize( toSet );
		dSize = toSet;

		firePreferredSizeChanged( toSet );
	}

	/*
	 * (non-Javadoc) @see com.cybernostics.lib.gui.icon.ScalableIcon#copy()
	 */
	@Override
	public ScalableIcon copy()
	{
		ScalableSVGIcon svg = new ScalableSVGIcon();
		svg.contained.setSvgUniverse( contained.getSvgUniverse() );
		return svg;
	}

	@Override
	public void addPreferredSizeListener( PreferredSizeListener listener )
	{
		sizeListeners.add( listener );

	}

	private ArrayList< PreferredSizeListener > sizeListeners = new ArrayList< PreferredSizeListener >();

	private Set< Component > toRepaintList = new HashSet< Component >();

	private void firePreferredSizeChanged( Dimension d )
	{
		for (PreferredSizeListener eachListener : sizeListeners)
		{
			eachListener.preferredSizeChanged( d );
		}
	}

	private boolean useBuffer = false;

	public boolean isUseBuffer()
	{
		return useBuffer;
	}

	public void setUseBuffer( boolean useBuffer )
	{
		this.useBuffer = useBuffer;
	}

	private boolean fitToParent = false;

	public boolean isFitToParent()
	{
		return fitToParent;
	}

	public void setFitToParent( boolean fitToParent )
	{
		this.fitToParent = fitToParent;
	}

	AffineTransform scaler = new AffineTransform();

	@Override
	public void paintIcon( Component comp, Graphics gg, int x, int y )
	{
		boolean needsPaint = true;
		JComponent jc = (JComponent) comp;

		Dimension renderSize;
		if (jc != null && fitToParent)
		{
			renderSize = jc.getSize();
			Insets i = jc.getInsets();
			renderSize.height -= ( i.bottom + i.top );
			renderSize.width -= ( i.left + i.right );
		}
		else
		{
			renderSize = dSize;
		}

		StateSaver g2 = StateSaver.wrap( gg );

		try
		{
			g2.translate(
				x,
				y );
			if (comp != null && !toRepaintList.contains( comp ))
			{
				toRepaintList.add( comp );
			}

			if (useBuffer)
			{
				if (fitToParent)
				{
					snapshot.setSize( renderSize );

				}

				BufferedImage cached = snapshot.getImage();

				if (cached != null)
				{
					gg.drawImage(
						cached,
						0,
						0,
						renderSize.width,
						renderSize.height,
											0,
						0,
						cached.getWidth(),
						cached.getHeight(),
						comp );
					needsPaint = false;
				}
			}

			if (needsPaint)
			{
				double scaleX = renderSize.width * 1.0
					/ contained.getIconWidth();
				double scaleY = renderSize.height * 1.0
					/ contained.getIconHeight();
				g2.scale(
					scaleX,
					scaleY );

				contained.paintIcon(
					comp,
					g2,
					0,
					0 );

			}

		}
		finally
		{
			g2.restore();
		}

	}

	private SVGSnapshot snapshot = null;

	public void clearCache()
	{
		snapshot.clearCache();
	}

	public static void paintLoading( Component comp, Graphics gg, int x, int y )
	{
		StateSaver g2 = StateSaver.wrap( gg );
		try
		{
			JComponent c = (JComponent) comp;
			FontMetrics fm = SwingUtilities2.getFontMetrics(
				c,
				gg );

			g2.fill( new Rectangle2D.Double( 0,
				0,
				comp.getWidth(),
				comp.getHeight() ) );

			int height = comp.getHeight();
			SwingUtilities2.drawString(
				c,
				gg,
				"Loading",
				5,
				( height + fm.getHeight() ) / 2 );
		}
		finally
		{
			g2.restore();
		}
	}

	public static void paintFailed( Component comp, Graphics gg, int x, int y )
	{
		StateSaver g2 = StateSaver.wrap( gg );
		try
		{
			Rectangle2D.Double compRect = new Rectangle2D.Double( 0,
				0,
				comp.getWidth(),
				comp.getHeight() );

			g2.setColor( Color.GRAY );
			g2.fill( compRect );
			g2.setColor( Color.RED );
			g2.setStroke( new BasicStroke( 5,
				BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND ) );
			g2.drawLine(
				0,
				0,
				(int) compRect.getMaxX(),
				(int) compRect.getMaxY() );
			g2.drawLine(
				0,
				(int) compRect.getMaxY(),
				(int) compRect.getMaxX(),
				0 );

		}
		finally
		{
			g2.restore();
		}
	}

	@Override
	public BufferedImage getImage()
	{

		BufferedImage bi = BitmapMaker.createFastImage(
			getIconWidth(),
			getIconHeight(),
			Transparency.TRANSLUCENT );
		paintIcon(
			null,
			bi.getGraphics(),
			0,
			0 );
		return bi;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return contained.getPreferredSize();
	}

	PropertyChangeSupport changes = new PropertyChangeSupport( this );

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		changes.addPropertyChangeListener( listener );
		contained.addPropertyChangeListener( listener );
	}

	public URI getSvgURI()
	{
		return contained.getSvgURI();
	}

	public void removePropertyChangeListener( PropertyChangeListener p )
	{
		changes.removePropertyChangeListener( p );
		contained.removePropertyChangeListener( p );
	}

	public SVGUniverse getSvgUniverse()
	{
		return contained.getSvgUniverse();
	}

	public void setSvgUniverse( SVGUniverse svgUniverse )
	{
		contained.setSvgUniverse( svgUniverse );
	}

	public void setPreserveAspect( boolean b )
	{
		contained.setPreserveAspect( b );
	}

	public SVGDiagram getDiagram()
	{
		return contained.getSvgUniverse()
			.getDiagram(
				contained.getSvgURI() );
	}

	@Override
	public Rectangle2D getItemRectangle( String regionName )
	{
		return SVGUtil.getSubItemRectangle(
			regionName,
			contained );
	}

}
