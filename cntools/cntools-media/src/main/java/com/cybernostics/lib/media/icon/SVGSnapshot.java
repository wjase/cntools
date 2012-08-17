package com.cybernostics.lib.media.icon;

import com.cybernostics.lib.media.image.BitmapMaker;
import com.cybernostics.lib.test.JFrameTest;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.app.beans.SVGIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * Creates a bitmap for an SVGImage
 *
 * @author jasonw
 */
public class SVGSnapshot
{

	public static void main( String[] args )
	{
		try
		{
			JFrameTest jf = new JFrameTest( "SVGSnapshot" );
			jf.setLayout( new FlowLayout() );
			SVGIcon svi = new SVGIcon();
			svi.setSvgURI( NoImageIcon.get()
				.getURL()
				.toURI() );
			SVGDiagram diag = svi.getSvgUniverse()
				.getDiagram(
					svi.getSvgURI() );
			SVGSnapshot ss1 = new SVGSnapshot( diag );
			ss1.setSize( new Dimension( 100, 100 ) );
			JLabel jl1 = new JLabel( new ImageIcon( ss1.getImage() ) );
			ss1.setSize( new Dimension( 300, 300 ) );
			jl1.setBorder( BorderFactory.createLineBorder(
				Color.blue,
				2 ) );
			JLabel jl2 = new JLabel( new ImageIcon( ss1.getImage() ) );
			jl2.setBorder( BorderFactory.createLineBorder(
				Color.blue,
				2 ) );
			JLabel jl3 = new JLabel( svi );
			jl3.setBorder( BorderFactory.createLineBorder(
				Color.blue,
				2 ) );
			jf.getContentPane()
				.add(
					jl1 );
			jf.getContentPane()
				.add(
					jl2 );
			jf.getContentPane()
				.add(
					jl3 );
			jf.go();
		}
		catch (URISyntaxException ex)
		{
			Logger.getLogger(
				SVGSnapshot.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}

	}

	private SVGDiagram diag = null;

	private Rectangle viewport = new Rectangle();

	private boolean preserveAspect = false;

	public boolean isPreserveAspect()
	{
		return preserveAspect;
	}

	public void setPreserveAspect( boolean preserveAspect )
	{
		this.preserveAspect = preserveAspect;
	}

	public SVGSnapshot( SVGDiagram diag )
	{
		this.diag = diag;
	}

	SoftReference< BufferedImage > cachedImage = null;

	public void setSize( Dimension preferredSize )
	{

		if (cachedImage != null && cachedImage.get() != null)
		{
			BufferedImage im = cachedImage.get();
			// if the cached image isn't big enough...
			if (preferredSize.getWidth() > im.getWidth()
				|| preferredSize.getHeight() > im.getHeight())
			{
				cachedImage = null;
			}
		}
		viewport.width = preferredSize.width;
		viewport.height = preferredSize.height;
	}

	public BufferedImage getImage()
	{
		boolean needsRegen = false;

		BufferedImage imc = null;

		// still got it?
		if (cachedImage == null)
		{
			needsRegen = true;
		}
		else
		{
			imc = cachedImage.get();
		}

		// make sure the buffer is the same or bigger than the current size
		if (needsRegen == false)
		{
			if (imc == null || imc.getWidth() < viewport.width
				|| imc.getHeight() < viewport.height)
			{
				needsRegen = true;
			}
		}

		if (needsRegen)
		{
			if (viewport.width == 0 || viewport.height == 0)
			{
				return null;
			}
			imc = BitmapMaker.createFastImage(
				viewport.width,
				viewport.height,
				Transparency.TRANSLUCENT );
			cachedImage = new SoftReference< BufferedImage >( imc );
			Graphics g = imc.getGraphics();
			try
			{
				Graphics2D g2 = (Graphics2D) g;
				diag.setIgnoringClipHeuristic( true );
				diag.setDeviceViewport( viewport );
				setScale( g2 );
				diag.render( g2 );
			}
			catch (SVGException ex)
			{
				throw new RuntimeException( ex );
			}
			finally
			{
				g.dispose();
			}

		}
		return imc;
	}

	AffineTransform scaleXform = new AffineTransform();

	;

	protected void setScale( Graphics2D g )
	{
		final Rectangle2D.Double rect = new Rectangle2D.Double();
		diag.getViewRect( rect );

		if (preserveAspect)
		{
			double scaleFactor = Math.min(
				viewport.width / rect.width,
				viewport.height / rect.height );
			scaleXform.setToScale(
				scaleFactor,
				scaleFactor );
		}
		else
		{
			scaleXform.setToScale(
				viewport.width / rect.width,
				viewport.height / rect.height );
		}

		g.transform( scaleXform );

	}

	public void render( Graphics g )
	{
		g.drawImage(
			getImage(),
			0,
			0,
			null );

	}

	protected void clearCache()
	{
		cachedImage = null;
	}

}