package com.cybernostics.lib.gui.control;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.persist.xml.ObjectSerialiser;
import com.cybernostics.lib.persist.xml.RectangleAdapter;
import java.beans.PropertyChangeListener;
import java.net.URI;

@XmlRootElement
public class CropInfo implements Comparable< CropInfo >
{

	/**
	 * The current crop rectangle relative to the image dimensions
	 */
	protected Rectangle2D cropDimensions = new Rectangle2D.Float( 0, 0, 1, 1 );
	/**
	 * The location of the sourceImage
	 */
	private URI location = null;

	public static CropInfo fromXML( String xmlString ) throws IOException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream( xmlString.getBytes() );
		CropInfo other = (CropInfo) ObjectSerialiser.jaxbReadObjectAsXML(
			CropInfo.class,
			bis );
		return other;

	}

	static public String toXML( CropInfo ci )
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			ObjectSerialiser.jaxbWriteObjectAsXML(
				ci,
				bos );
		}
		catch (IOException e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		return bos.toString();
	}

	public final static String CROPPED = "CROP_RECT";

	//protected AffineTransform scaler = AffineTransform.getScaleInstance( 1.0, 1.0 );

	public CropInfo()
	{
		setCropDimensions( new Rectangle2D.Float( 0f, 0f, 1f, 1f ) );
	}

	public CropInfo( URI toLoad )
	{
		this.location = toLoad;
		setCropDimensions( new Rectangle2D.Float( 0f, 0f, 1f, 1f ) );
	}

	public CropInfo( URI uRI, Float aFloat )
	{
		this( uRI );
		setCropDimensions( aFloat );
	}

	public CropInfo( CropInfo ci )
	{
		this( ci.getLocation() );
		setCropDimensions( ci.getCropDimensions() );
	}

	/**
	 * Ensures that the crop area exists within the parent image
	 */
	public void clipCrop()
	{
		double x = cropDimensions.getMinX();
		double y = cropDimensions.getMinY();
		double width = cropDimensions.getWidth();
		double height = cropDimensions.getHeight();

		x = ( x < 0 ) ? 0 : ( x > 1.0 ) ? 1.0 : x;
		y = ( y < 0 ) ? 0 : ( y > 1.0 ) ? 1.0 : y;
		width = ( width < 0 ) ? 0 : ( ( width + x ) > 1.0 ) ? 1.0 - x : width;
		height = ( height < 0 ) ? 0 : ( ( height + y ) > 1.0 ) ? 1.0 - y
			: height;

		cropDimensions.setRect(
			x,
			y,
			width,
			height );
	}

	@Override
	public int compareTo( CropInfo o )
	{
		if (this.location == null)
		{
			return -1;
		}
		int compare = this.location.compareTo( o.location );
		if (compare != 0)
		{
			return compare;

		}
		double thisSize = cropDimensions.getHeight()
			* cropDimensions.getWidth();
		double oSize = o.cropDimensions.getHeight()
			* o.cropDimensions.getWidth();
		compare = java.lang.Double.compare(
			thisSize,
			oSize );

		if (compare != 0)
		{
			return compare;
		}

		compare = java.lang.Double.compare(
			cropDimensions.getMinX(),
			o.cropDimensions.getMinX() );
		if (compare != 0)
		{
			return compare;
		}
		compare = java.lang.Double.compare(
			cropDimensions.getMinY(),
			o.cropDimensions.getMinY() );
		if (compare != 0)
		{
			return compare;
		}
		return 0;
	}

	@XmlJavaTypeAdapter(value = RectangleAdapter.class, type = Rectangle2D.class)
	public Rectangle2D getCropDimensions()
	{
		return cropDimensions;
	}

	public static BufferedImage getCroppedImage( CropInfo toLoad )
		throws MalformedURLException, IOException
	{
		if (toLoad == null)
		{
			return null;
		}
		URI cropLocation = toLoad.getLocation();

		if (cropLocation == null)
		{
			return null;
		}

		BufferedImage uncropped = ImageLoader.loadBufferedImage( toLoad.getLocation()
			.toURL() );
		int fullWidth = uncropped.getWidth();
		int fullHeight = uncropped.getHeight();
		Rectangle2D cropDimensions = toLoad.getCropDimensions();
		Point2D topLeft = new Point2D.Double( cropDimensions.getX(),
			cropDimensions.getY() );
		int x = (int) ( topLeft.getX() * fullWidth );
		int y = (int) ( topLeft.getY() * fullHeight );
		int w = (int) ( cropDimensions.getWidth() * fullWidth );
		int h = (int) ( cropDimensions.getHeight() * fullHeight );
		//clipCrop();
		BufferedImage cropped = uncropped.getSubimage(
			x,
			y,
			w - 1,
			h - 1 );
		uncropped = null;
		return cropped;
	}

	@XmlElement
	public URI getLocation()
	{
		return location;
	}

	protected PropertyChangeSupport updater = new PropertyChangeSupport( this );
	boolean dimensionsSet = false;

	public synchronized void addPropertyChangeListener( PropertyChangeListener listener )
	{
		updater.addPropertyChangeListener( listener );
	}

	public final void setCropDimensions( Rectangle2D dimensions )
	{
		// clip the dimensions to those possible
		cropDimensions = dimensions;
		dimensionsSet = true;
		clipCrop();
		updater.firePropertyChange(
			CROPPED,
			null,
			cropDimensions );

	}

	public void setLocation( File f )
	{
		this.location = f.toURI();
	}

	public void setLocation( URI u )
	{
		this.location = u;
	}
}
