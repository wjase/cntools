package com.cybernostics.lib.gui.control;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;

import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.adapter.XMLPropertyComponent;
import com.cybernostics.lib.gui.border.GradientDropShadowBorder;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import com.cybernostics.lib.gui.declarative.events.WhenHiddenOrClosed;
import com.cybernostics.lib.gui.declarative.events.WhenResized;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.image.ImageLoader;
import java.net.URI;
import java.net.URL;

/**
 * Crop Control allows users to define a sub region of a source image.
 * 
 * @author jasonw
 * 
 */
public class CropControl extends JPanel implements XMLPropertyComponent
{

	private static final long serialVersionUID = -956904273931691077L;

	private AlphaComposite alpha = AlphaComposite.getInstance(
		AlphaComposite.SRC_OVER,
		0.5f );
	/**
	 * Stores the original and cropped images sharing a common raster space to
	 * save memory
	 */
	private BufferedImage crop;
	CropInfo cropInfo = null;
	int handleSize = 5;
	private AffineTransform imageScaler = AffineTransform.getScaleInstance(
		1.0,
		1.0 );
	private AffineTransform mouseScaler = AffineTransform.getScaleInstance(
		1.0,
		1.0 );

	public CropControl()
	{
	}

	public CropControl( CropInfo info )
	{
		cropInfo = info;
		try
		{
			crop = ImageLoader.loadBufferedImage( info.getLocation()
				.toURL() );
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				CropControl.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	public CropControl( URI toLoad )
	{
		cropInfo = new CropInfo( toLoad );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify()
	{
		initControl();
		super.addNotify();
	}

	private void createMouseScaler()
	{
		try
		{
			mouseScaler = imageScaler.createInverse();
		}
		catch (NoninvertibleTransformException e)
		{
		}
	}

	@Override
	public Object getControlXMLProperty()
	{
		return CropInfo.toXML( cropInfo );
	}

	public CropInfo getCropped()
	{
		return cropInfo;
	}

	AffineTransform relToAbs = null;

	public AffineTransform getRelToAbsScaler()
	{
		if (relToAbs == null)
		{
			relToAbs = AffineTransform.getScaleInstance(
				crop.getWidth(),
				crop.getHeight() );
		}
		return relToAbs;
	}

	AffineTransform absToRel = null;

	public AffineTransform getAbsToRelScaler()
	{
		if (absToRel == null)
		{
			absToRel = AffineTransform.getScaleInstance(
				1.0 / crop.getWidth(),
				1.0 / crop.getHeight() );
		}
		return absToRel;
	}

	/**
	 * Returns the current crop rectangle relative to the crop size
	 * 
	 * @return
	 */
	public Rectangle2D getCurrent()
	{
		if (cropInfo == null)
		{
			cropInfo = new CropInfo();
		}
		if (cropInfo != null)
		{
			Rectangle2D rect = cropInfo.getCropDimensions();
			return getRelToAbsScaler().createTransformedShape(
				rect )
				.getBounds2D();

		}

		return null;
	}

	//    public Rectangle2D getCurrentRelative()
	//    {
	//        Rectangle2D currentRect = getCurrent();
	//
	//
	//        Area a = new Area( currentRect );
	//        a.transform( scaler );
	//        return a.getBounds2D();
	//
	//    }
	/**
	 * Returns the point on the rectangle border farthest from the specified
	 * point
	 * 
	 * @param mousePoint
	 * @return
	 */
	private Point2D getDiagonallyOppositePoint( Point2D mousePoint )
	{
		Rectangle2D rect = getCurrent();

		// start with top left
		Point2D.Float farthest = new Point2D.Float( (float) rect.getMinX(),
			(float) rect.getMinY() );
		Point2D.Float nextPoint = new Point2D.Float( (float) rect.getMinX(),
			(float) rect.getMaxY() );
		if (farthest.distance( mousePoint ) < nextPoint.distance( mousePoint ))
		{
			farthest = nextPoint;
		}
		nextPoint = new Point2D.Float( (float) rect.getMaxX(),
			(float) rect.getMaxY() );
		if (farthest.distance( mousePoint ) < nextPoint.distance( mousePoint ))
		{
			farthest = nextPoint;
		}
		nextPoint = new Point2D.Float( (float) rect.getMaxX(),
			(float) rect.getMinY() );
		if (farthest.distance( mousePoint ) < nextPoint.distance( mousePoint ))
		{
			farthest = nextPoint;
		}
		return farthest;
	}

	public CropInfo getImage()
	{
		return cropInfo;
	}

	private void initControl()
	{
		setOpaque( true );
		Border inner = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(
				Color.white,
				10 ),
			BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
		inner = BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder( Color.black ),
			inner );
		setBorder( BorderFactory.createCompoundBorder(
			new GradientDropShadowBorder(),
			inner ) );
		// setBorder(inner);
		// setMinimumSize( new Dimension( 200, 200 ) ); // don't hog space
		// setPreferredSize( new Dimension( 200, 200 ) ); // don't hog space
		initEventListeners();

	}

	private void initEventListeners()
	{

		MouseInputAdapter myListener = new MouseInputAdapter()
		{

			@Override
			public void mouseDragged( MouseEvent e )
			{
				updateSize( e );
			}

			@Override
			public void mousePressed( MouseEvent e )
			{
				Point2D mousePoint = new Point2D.Float( e.getX(), e.getY() );
				mouseScaler.transform(
					mousePoint,
					mousePoint );
				setCurrent( ( new Line2D.Float( mousePoint,
					getDiagonallyOppositePoint( mousePoint ) ) ).getBounds2D() );

				repaint();
			}

			@Override
			public void mouseReleased( MouseEvent e )
			{
				updateSize( e );
			}

			/*
			 * Update the size of the current rectangle and call repaint.
			 * Because currentRect always has the same origin, translate it if
			 * the width or height is negative. For efficiency (though that
			 * isn't an issue for this program), specify the painting region
			 * using arguments to the repaint() call.
			 */
			void updateSize( MouseEvent e )
			{
				Point2D mousePoint = new Point2D.Float( e.getX(), e.getY() );
				mouseScaler.transform(
					mousePoint,
					mousePoint );
				setCurrent( ( new Line2D.Float( mousePoint,
					getDiagonallyOppositePoint( mousePoint ) ) ).getBounds2D() );

				updateRect( e );
			}
		};

		addMouseListener( myListener );
		addMouseMotionListener( myListener );

		new WhenResized( this )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				updateImageScaler( getSize() );
			}
		};
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		super.paintComponent( g );

		if (crop == null)
		{
			return;
		}
		StateSaver g2 = StateSaver.wrap( g );

		try
		{
			Insets i = getInsets();
			// System.out.printf("top=%d,left=%d,right=%d,bottom=%d\n",i.top,i.left,i.bottom,i.right);
			g2.setClip( new Rectangle2D.Double( i.left, i.top, getWidth()
				- i.left - i.right, getHeight() - i.top
				- i.bottom ) );
			g2.translate(
				i.left,
				i.top );
			g2.scale(
				imageScaler.getScaleX(),
				imageScaler.getScaleY() );

			if (crop != null)
			{
				g2.drawImage(
					crop,
					0,
					0,
					this );
			}
			//
			g2.setComposite( alpha );

			paintCropHandles( g2 );

		}
		finally
		{
			g2.restore();
		}

	}

	private void paintCropHandles( Graphics2D g2 )
	{
		// Draw a rectangle on top of the image.
		Rectangle2D current = getCurrent();
		// g2.draw( current );

		Rectangle2D.Float handleRect = new Rectangle2D.Float( (float) current.getMinX()
			- handleSize,
			(float) current
				.getMinY()
				- handleSize,
			handleSize * 2,
			handleSize * 2 );

		g2.setPaint( Color.red );

		g2.fill( handleRect );
		// bottom left
		handleRect.y = (float) current.getMaxY() - handleSize;
		g2.fill( handleRect );
		// bottom right
		handleRect.x = (float) current.getMaxX() - handleSize;
		g2.fill( handleRect );
		// top right
		handleRect.y = (float) current.getMinY() - handleSize;
		g2.fill( handleRect );

		Shape sHighlight = new Rectangle2D.Double( 0,
			0,
			crop.getWidth(),
			crop.getHeight() );
		Area aHighlighted = new Area( sHighlight );
		aHighlighted.subtract( new Area( current ) );

		g2.setPaint( Color.gray );
		g2.fill( aHighlighted );

	}

	// @Override
	// public void setBounds( int x, int y, int width, int height )
	// {
	// super.setBounds( x, y, width, height );
	// updateImageScaler( width, height );
	// }
	//
	// @Override
	// public void setBounds( Rectangle r )
	// {
	// // TODO Auto-generated method stub
	// super.setBounds( r );
	// updateImageScaler( r.width, r.height );
	// }
	@Override
	public void setControlXMLProperty( String xmlValue )
	{
		try
		{
			cropInfo = CropInfo.fromXML( xmlValue );
		}
		catch (IOException ex)
		{
			Logger.getLogger(
				CropControl.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

	public void setCurrent( Rectangle2D rectangle2D )
	{
		AffineTransform at = this.getAbsToRelScaler();
		cropInfo.setCropDimensions( at.createTransformedShape(
			rectangle2D )
			.getBounds2D() );
		updateRect( null );
	}

	public void setCurrentRelative( Rectangle2D.Float currentRelative )
	{
		cropInfo.setCropDimensions( currentRelative );
		updateRect( null );
	}

	public void setImage( CropInfo toCrop )
	{
		if (toCrop != null)
		{
			try
			{
				relToAbs = null;
				absToRel = null;
				cropInfo = toCrop;
				URI toLoad = toCrop.getLocation();
				if (toLoad != null)
				{
					crop = ImageLoader.loadBufferedImage( toLoad.toURL() );
					Dimension d = getSize();
					updateImageScaler(
						d.width,
						d.height );
					updateRect( null );
				}
			}
			catch (Exception ex)
			{
				Logger.getLogger(
					CropControl.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}

		}

	}

	// @Override
	// public void setSize( Dimension d )
	// {
	// super.setSize( d );
	// updateImageScaler( d );
	// }
	//
	// @Override
	// public void setSize( int width, int height )
	// {
	// super.setSize( width, height );
	// updateImageScaler( width, height );
	// }
	/**
	 * The image scaler ensures that the whole image is visible in the control
	 * without scrolling. When the image is changed then the control needs to be
	 * repainted with the new scaler.
	 */
	public void updateImageScaler( Dimension d )
	{
		updateImageScaler(
			d.width,
			d.height );
	}

	/**
	 * The image scaler ensures that the whole image is visible in the control
	 * without scrolling. When the image is changed then the control needs to be
	 * repainted with the new scaler.
	 */
	private void updateImageScaler( int width, int height )
	{
		if (crop != null)
		{
			Insets i = getInsets();
			imageScaler.setToScale(
				1.0 * ( width - i.left - i.right ) / crop.getWidth(),
				1.0
					* ( height - i.top - i.bottom ) / crop.getHeight() );
			createMouseScaler();
			repaint();

		}

	}

	public void updateRect( MouseEvent e )
	{
		handleSize = crop.getWidth() / 50;
		new RunLater()
		{/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.cybernostics.lib.gui.declarative.events.RunLater#run(java.lang
			 * .Object[])
			 */

			@Override
			public void run( Object... args )
			{
				firePropertyChange(
					"croppedImage",
					null,
					crop );
			}
		};

		repaint();
	}
}