package com.cybernostics.lib.media.image;

import com.cybernostics.lib.concurrent.WatchableWorkerTask;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.media.AsyncListModel;
import com.cybernostics.lib.media.ListFetcher;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.ListModel;

/**
 *
 * @author jasonw
 */
public class ImageLoader
{

	public enum ScaleType
	{

		BOTH, HEIGHT_ONLY, WIDTH_ONLY
	};

	/**
	 * Creates a thumbnail of the given file. The thumbnails largest dimension will be size. Throws an exception if the
	 * image cannot be read.
	 * 
	 * @param file
	 *            File The image to be read.
	 * @param size
	 *            int Length of the longest dimension of the thumbnail.
	 * @return BufferedImage
	 * @throws IOException
	 *             If image cannot be read.
	 */
	public static BufferedImage getThumb( File file, int size )
		throws IOException
	{
		BufferedImage image = null;
		// currently metadata extractor only works for JPG images
		if (file.getName()
			.toUpperCase()
			.endsWith(
				"JPG" ) || file.getName()
			.toUpperCase()
			.endsWith(
				"JPEG" ))
		{
			try
			{
				// locate the bytes of the thumbnail data and read them using
				// ImageIO
				// (thumbnail data will usually be a JPG image itself, if not
				// there is a fallback)
				Metadata metadata = JpegMetadataReader.readMetadata( file );
				if (metadata.containsDirectory( ExifDirectory.class ))
				{
					Directory dir = metadata.getDirectory( ExifDirectory.class );
					if (dir.containsTag( ExifDirectory.TAG_THUMBNAIL_DATA ))
					{
						byte[] bytes = dir.getByteArray( ExifDirectory.TAG_THUMBNAIL_DATA );
						image = ImageIO.read( new ByteArrayInputStream( bytes ) );
						if (size == 0)
						{
							if (image != null)
							{
								return image;
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				throw new IOException( ex );
			}
		}

		// if we failed to read the thumbnail we read the entire file
		if (image == null)
		{
			image = ImageIO.read( file );
		}
		// now we just have to scale the image to the desired size
		// no rendering hints used... it's just a thumbnail anyway
		double scaleX = size / (double) image.getWidth();
		double scaleY = size / (double) image.getHeight();
		double scale = Math.min(
			scaleX,
			scaleY );
		final BufferedImage thumb = BitmapMaker.createFastImage(
			(int) Math.round( scale * image.getWidth() ),
			(int) Math.round( scale * image.getHeight() ),
			Transparency.OPAQUE );
		Graphics g = thumb.createGraphics();
		g.drawImage(
			image,
			0,
			0,
			thumb.getWidth(),
			thumb.getHeight(),
			null );
		g.dispose();
		image.flush();
		return thumb;
	}

	public static BufferedImage getThumb( String location ) throws IOException
	{
		File f;
		try
		{
			f = new File( new URI( location ) );
			return getThumb(
				f,
				0 );
		}
		catch (Exception ex)
		{
			throw new IOException( ex );
		}

	}

	public static BufferedImage loadBufferedImageFromStream( InputStream imageData )
		throws IOException
	{
		BufferedImage im = null;

		try
		{
			im = ImageIO.read( imageData );
		}
		catch (Error e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		return im;
	}

	/**
	 * Loads an image from the specified URL.
	 * 
	 * @param theUrl
	 * @return the image or null of it can't be found
	 * @throws IOException
	 */
	public static BufferedImage loadBufferedImage( URL theUrl )
		throws IOException
	{
		BufferedImage im = null;
		try
		{
			im = ImageIO.read( theUrl );
		}
		catch (Exception e)
		{
			throw new IOException( "Couldn't Load " + theUrl, e );
		}
		catch (Error e)
		{
			throw new IOException( e );
		}

		return im;

	}

	public static BufferedImage getThumb( URL toLoad, int size )
		throws IOException
	{
		BufferedImage image = null;
		// currently metadata extractor only works for JPG images
		if (toLoad.toExternalForm()
			.toUpperCase()
			.endsWith(
				"JPG" )
			|| toLoad.toExternalForm()
				.toUpperCase()
				.endsWith(
					"JPEG" ))
		{
			try
			{
				// locate the bytes of the thumbnail data and read them using
				// ImageIO
				// (thumbnail data will usually be a JPG image itself, if not
				// there is a fallback)
				Metadata metadata = JpegMetadataReader.readMetadata( toLoad.openStream() );
				if (metadata.containsDirectory( ExifDirectory.class ))
				{
					Directory dir = metadata.getDirectory( ExifDirectory.class );
					if (dir.containsTag( ExifDirectory.TAG_THUMBNAIL_DATA ))
					{
						byte[] bytes = dir.getByteArray( ExifDirectory.TAG_THUMBNAIL_DATA );
						image = ImageIO.read( new ByteArrayInputStream( bytes ) );
					}
				}
			}
			catch (JpegProcessingException ex)
			{
				throw new IOException( ex );
			}
			catch (MetadataException ex)
			{
				throw new IOException( ex );
			}
		}

		// now we just have to scale the image to the desired size
		// no rendering hints used... it's just a thumbnail anyway
		double scaleX = 1;
		double scaleY = 1;

		// if we failed to read the thumbnail we read the entire file
		if (image == null)
		{
			image = loadBufferedImage( toLoad );
		}
		else
		{

			if (size != 0)
			{// now we just have to scale the image to the desired size
				// no rendering hints used... it's just a thumbnail anyway
				scaleX = size / (double) image.getWidth();
				scaleY = size / (double) image.getHeight();
			}
		}

		if (image == null)
		{
			// System.out.println( "null image for " + file.toExternalForm() );
			return null;
		}

		double scale = Math.min(
			scaleX,
			scaleY );
		final BufferedImage thumb = BitmapMaker.createFastImage(
			(int) Math.round( scale * image.getWidth() ),
			(int) Math.round( scale * image.getHeight() ),
			Transparency.OPAQUE );
		Graphics g = thumb.createGraphics();
		g.drawImage(
			image,
			0,
			0,
			thumb.getWidth(),
			thumb.getHeight(),
			null );
		g.dispose();

		return thumb;

	}

	public static ListModel getThumbIcons( URL toLoadBase,
		PropertyChangeListener threadlistener,
		final ResourceFilter filt )
	{
		AsyncListModel< ScalableIcon > theModel = new AsyncListModel< ScalableIcon >();

		// StopWatch sw = new StopWatch( "getIcons" );
		// sw.start();
		ListFetcher< ScalableIcon > myFetcher = new ListFetcher< ScalableIcon >(
			theModel, toLoadBase )
		{

			@Override
			protected List< ScalableIcon > doInBackground() throws Exception
			{
				List< ScalableIcon > theIcons = new ArrayList< ScalableIcon >();
				List< URL > theUrls = ResourceFinder.getChildren(
					resourcePath,
					filt );

				Dimension dMinimum = new Dimension( 80, 80 );

				for (URL eachURL : theUrls)
				{
					if (!eachURL.getFile()
						.endsWith(
							"svg" ))
					{
						BufferedImage bimage = null;
						try
						{
							bimage = getThumb(
								eachURL,
								0 );

							if (bimage == null)
							{
								continue;
							}

							bimage = BitmapMaker.getScaled(
								bimage,
								80,
								80 );
							AttributedScalableIcon asi = new AttributedScalableIcon( new ScalableImageIcon( bimage ) );
							asi.put(
								"URL",
								eachURL );

							asi.setSize( dMinimum );
							theIcons.add( asi );
							publish( asi );
						}
						catch (Exception e)
						{
							UnhandledExceptionManager.handleException( e );
						}
					}
					else
					{
						AttributedScalableIcon asi = AttributedScalableIcon.create( eachURL );
						asi.setSize( dMinimum );
						theIcons.add( asi );
						asi.put(
							"URL",
							eachURL );

					}

				}
				return theIcons;
			}
		};

		if (threadlistener != null)
		{
			myFetcher.addPropertyChangeListener( threadlistener );
		}
		myFetcher.execute();

		// sw.stop();
		// sw.summarise();
		return theModel;
	}

	public static WatchableWorkerTask startloadImage( final URL location )
	{
		return new WatchableWorkerTask( "loadImage" + location )
		{

			@Override
			protected Object doTask() throws Exception
			{
				return loadBufferedImage( location );
			}
		};
	}

	public static WatchableWorkerTask startLoadScalableImageIcon( final URL location )
	{
		return new WatchableWorkerTask( "loadIcon" + location )
		{

			@Override
			protected Object doTask() throws Exception
			{
				BufferedImage toLoad = ImageLoader.loadBufferedImage( location );
				return new ScalableImageIcon( toLoad );
			}
		};

	}

	public static ListModel getIcons( String pathFrom )
	{
		return getIcons(
			pathFrom,
			ScaleType.BOTH );
	}

	private static final Dimension defaultDimension = new Dimension( 80, 80 );

	public static ListModel getIcons( String pathFrom,
		final ScaleType scalingType )
	{
		return getIcons(
			pathFrom,
			scalingType,
			defaultDimension );
	}

	public static ListModel getIcons( String pathFrom,
		final ScaleType scalingType,
		final Dimension imageSize )
	{
		AsyncListModel< ImageIcon > theModel = new AsyncListModel< ImageIcon >();

		// StopWatch sw = new StopWatch( "getIcons" );
		// sw.start();
		ListFetcher< ImageIcon > myFetcher;
		try
		{
			myFetcher = new ListFetcher< ImageIcon >( theModel,
				new URL( pathFrom ) )
			{

				@Override
				protected List< ImageIcon > doInBackground() throws Exception
				{
					List< URL > theUrls = null;
					theUrls = ResourceFinder.getChildren(
						resourcePath,
						null );

					List< ImageIcon > theIcons = new ArrayList< ImageIcon >();

					for (URL eachURL : theUrls)
					{
						BufferedImage bimage = null;
						BufferedImage scaled = null;
						try
						{
							bimage = ImageLoader.loadBufferedImage( eachURL );

							if (bimage == null)
							{
								continue;
							}
							float aspect = ( 1f * bimage.getHeight() )
								/ bimage.getWidth();
							switch (scalingType)
							{
								case BOTH:
									scaled = BitmapMaker.getScaled(
										bimage,
										imageSize.width,
										imageSize.height );
									break;
								case HEIGHT_ONLY:
									scaled = BitmapMaker.getScaled(
										bimage,
										(int) ( imageSize.height / aspect ),
										imageSize.height );
									break;
								case WIDTH_ONLY:
									scaled = BitmapMaker.getScaled(
										bimage,
										imageSize.width,
										(int) ( imageSize.width * aspect ) );
									break;
							}

							bimage.flush();

							ImageIcon ii = new ImageIcon( scaled );
							ii.setDescription( eachURL.toExternalForm() );

							theIcons.add( ii );
							publish( ii );
						}
						catch (Exception e)
						{
							UnhandledExceptionManager.handleException( e );
						}
					}
					return theIcons;
				}
			};

			myFetcher.execute();

		}
		catch (MalformedURLException ex)
		{
			throw new RuntimeException( ex );
		}

		// sw.stop();
		// sw.summarise();
		return theModel;
	}

}
