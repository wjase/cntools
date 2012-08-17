/*
 * #%L cntools-animator %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */
package com.cybernostics.lib.animator.sprite;

import com.cybernostics.lib.animator.sprite.IconTransformer.FlipType;
import com.cybernostics.lib.animator.sprite.component.JLabelSprite;
import com.cybernostics.lib.animator.ui.AnimatedScene;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.GraphicsConfigurationSource;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shape.IconRect;
import com.cybernostics.lib.maths.DimensionFloat;
import com.cybernostics.lib.maths.DoubleDimension;
import com.cybernostics.lib.maths.Random;
import com.cybernostics.lib.media.icon.NoImageIcon;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author jasonw
 *
 */
public class IconSprite
	implements
	Comparable< ISprite >,
	Icon,
	ISprite,
	GraphicsConfigurationSource
{

	public static IconSprite createClone( IconSprite ics )
	{
		ScaledRotatedIconClone src = new ScaledRotatedIconClone( ics.getIconRenderer() );
		IconSprite ns = new IconSprite();
		ns.setIconRenderer( src );
		return ns;
	}

	public static IconSprite create( String id,
		Rectangle2D bounds,
		URL iconResource,
		double alpha )
	{
		IconSprite isp = create(
			id,
			bounds,
			iconResource );
		isp.setTransparency( alpha );
		return isp;

	}

	/**
	 * Convenience method for creating sprites from common params
	 *
	 * @param id
	 * @param bounds
	 * @param iconResource
	 * @return
	 */
	public static IconSprite create( String id,
		Rectangle2D bounds,
		URL iconResource )
	{
		IconSprite isp = new IconSprite( id );
		if (iconResource != null)
		{
			ScalableSVGIcon svi = new ScalableSVGIcon( iconResource );
			svi.setUseBuffer( true );
			isp.setIcon( svi );
		}
		if (bounds != null)
		{
			isp.setRelativeBounds( bounds );
		}
		return isp;
	}

	@Override
	public int getIconHeight()
	{
		return iconRenderer.getIconHeight();
	}

	@Override
	public int getIconWidth()
	{
		return iconRenderer.getIconWidth();
	}

	@Override
	public void paintIcon( Component c, Graphics g, int x, int y )
	{
		Graphics2D g2 = (Graphics2D) g;
		if (getTransparency() < 1.0)
		{
			g2.setComposite( getRenderComposite() );
		}

		iconRenderer.paintIcon(
			c,
			g2,
			x,
			y );
	}

	private boolean needsRender = true;

	private boolean visible = true;

	private static double controlSpotDiameter = 20;

	private boolean restoreContext = true;

	private IconTransformer iconRenderer = null;

	public IconTransformer getIconRenderer()
	{
		return iconRenderer;
	}

	/**
	 * Normally don't call this directly. Instead call the static createClone
	 * method to create a clone Sprite.
	 *
	 * @param iconRenderer
	 */
	public void setIconRenderer( IconTransformer iconRenderer )
	{
		this.iconRenderer = iconRenderer;

		ownerSizeUpdated(); // update everything
	}

	/**
	 * If set to true, the Graphics2D context will be reset after render
	 *
	 * @param value
	 */
	@Override
	public void setRestoreContext( boolean value )
	{
		restoreContext = value;
	}

	// sum of spriteLocation plus the scaled offset to account for the hostspot
	private AffineTransform spritePixelLocation = new AffineTransform();

	private boolean isUpdating = false;

	/**
	 * Updates the absolute location of the sprite
	 */
	private void updateLocationTransform()
	{

		if (isUpdating)
		{
			return; // to save calling it multiple times during set bounds
		}
		if (owner != null)
		{
			spritePixelLocation.setToIdentity();
			spritePixelLocation.concatenate( owner.getTransform() );
			spritePixelLocation.translate(
				location.getX(),
				location.getY() );
			// strip out the scale bit and leave just the translation info
			spritePixelLocation.setToTranslation(
				spritePixelLocation.getTranslateX(),
													spritePixelLocation.getTranslateY() );

		}
		watcher.changedRelativeBounds( getRelativeBounds() );
	}

	//    private void updateLocationOffset()
	//    {
	//        iconRenderer.getTransform().transform( myAnchor.getAnchorLocation(), hotspot );
	//
	//    }
	@Override
	public int compareTo( ISprite o )
	{
		int zCompare = z_order.compareTo( o.getZ_order() );
		if (zCompare == 0)
		{
			zCompare = id.compareTo( o.getId() );
		}
		return zCompare;
	}

	@Override
	public String getId()
	{
		return id;
	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	@Override
	public void setRelativeBounds( double x,
		double y,
		double width,
		double height )
	{
		if (iconRenderer == null)
		{
			return;
		}
		isUpdating = true;
		// use the current anchor to set the correct pos
		Point2D anchorPos = iconRenderer.getAnchor()
			.getRelativeAnchorLocation();
		setRelativeLocation(
			x + ( anchorPos.getX() * width ),
			y + ( anchorPos.getY() * height ) );
		setRelativeSize(
			width,
			height );
		isUpdating = false;
		updateLocationTransform();
		iconRenderer.setUnScaledSize( pixelSize );

	}

	/**
	 * Gets bounds relative to sprites parent
	 *
	 * @return
	 */
	@Override
	public Rectangle2D getRelativeBounds()
	{
		double width = relativeSize.getWidth();
		double height = relativeSize.getHeight();
		Point2D anchorPos = iconRenderer != null ? iconRenderer.getAnchor()
			.getRelativeAnchorLocation() : zeroPoint;
		return new Rectangle2D.Double( location.getX()
			- ( anchorPos.getX() * width ),
			( location.getY() - anchorPos.
																						getY()
				*
																							height ),
			width,
			height );
	}

	private static final Point zeroPoint = new Point();

	/**
	 *
	 * @param rect
	 */
	@Override
	public void setRelativeBounds( Rectangle2D rect )
	{
		setRelativeBounds(
			rect.getMinX(),
			rect.getMinY(),
			rect.getWidth(),
			rect.getHeight() );
	}

	public IconSprite( Icon toRender )
	{
		setIcon( toRender );
		setId( Random.uid() );
	}

	public IconSprite()
	{
		setId( Random.uid() );
	}

	public void setIcon( Icon ic )
	{
		iconRenderer = new ScaledRotatedIcon( ic,
			IconRect.getDimension( ic ),
			this );
		iconRenderer.setIcon( ic );
	}

	public Icon getIcon()
	{
		return iconRenderer.getIcon();
	}

	/**
	 *
	 *
	 * @param name
	 * @param size
	 */
	public IconSprite( String id )
	{
		setId( id );
	}

	/**
	 * The stock size
	 */
	private static DoubleDimension DEFAULT_SIZE = new DoubleDimension( 0.1, 0.1 );

	/**
	 * The current location of the sprite on the panel - scaled to be between 0
	 * & 1 on both axes
	 */
	private Point2D location = new Point2D.Float( 0, 0 );

	/**
	 *
	 * @return
	 */
	@Override
	public Point2D getRelativeLocation()
	{
		return location;
	}

	/**
	 *
	 * @param location
	 */
	@Override
	public void setRelativeLocation( Point2D location )
	{
		this.location = location;
		updateLocationTransform();

	}

	/**
	 * This is the unscaled size of the source image in units in pixels. i.e.
	 * when scale is 1.0 this is how big the sprite will be
	 */
	//    private Dimension screenSize = new Dimension();
	private Dimension pixelSize = new Dimension();

	/**
	 * This is the unscaled size of the source image in units relative to the
	 * screen size. i.e. a width of 1.0 is the width of the screen. Ditto for
	 * the height. i.e. when scale is 1.0 this is how big the sprite will be
	 */
	private Dimension2D relativeSize = new DoubleDimension( 0.1f, 0.1f );

	/**
	 * The name of the sprite - this can be used for logging
	 */
	private String id;

	/**
	 * The transparency with which the sprite is rendered
	 */
	protected Composite renderTransparency = makeComposite( 1.0f );

	/**
	 *
	 * @return
	 */
	public Composite getRenderComposite()
	{
		return renderTransparency;
	}

	@Override
	public double getTransparency()
	{
		return transparency;
	}

	/**
	 *
	 * @param g2
	 */
	@Override
	public final void render( Graphics2D g2 )
	{
		if (!initialised)
		{
			ownerSizeUpdated();
		}

		if (!isVisible())
		{
			return;
		}

		if (iconRenderer == null)
		{
			return;
		}
		// we only use this if saveContext is true
		StateSaver saved = null;

		if (restoreContext)
		{
			// create a special state saving context which wraps g2
			g2 = saved = StateSaver.wrap( g2 );
		}
		try
		{
			if (getTransparency() < 1.0)
			{
				g2.setComposite( getRenderComposite() );
			}

			g2.transform( spritePixelLocation );

			Point2D hotspot = iconRenderer.getHotspot();
			//g2.fill( new Rectangle2D.Double( -hotspot.getX(), -hotspot.getY(), iconRenderer.getIconWidth(), getIconHeight() ) );
			iconRenderer.paintIcon(
				null,
				g2,
				(int) -hotspot.getX(),
				(int) -hotspot.getY() );

			if (showControlPoint)
			{
				g2.setColor( Color.red );
				g2.fill( new Ellipse2D.Double( location.getX()
					- ( controlSpotDiameter / 2 ),
					location.getY() -
							( controlSpotDiameter / 2 ),
					controlSpotDiameter,
					controlSpotDiameter ) );
			}

		}
		finally
		{
			if (restoreContext)
			{
				saved.restore();
			}
		}
	}

	public Point2D getHotspotOffset()
	{
		return iconRenderer.getHotspot();
	}

	/**
	 *
	 * @param renderTransparency
	 */
	public void setRenderComposite( Composite renderTransparency )
	{
		this.renderTransparency = renderTransparency;
	}

	/**
	 *
	 */
	protected double transparency = 1.0f;

	private Integer z_order = 0;

	/**
	 * Scaler to maintain relative size compared to screen
	 */
	//    protected AffineTransform screenSizeScaler = new AffineTransform();
	private SpriteOwner owner = null;

	/**
	 * Sets the owner object for this sprite which determines the mapping from
	 * relative to owner-based coordinates.
	 *
	 * @param owner
	 */
	@Override
	public void setOwner( SpriteOwner owner )
	{
		this.owner = owner;
		ownerSizeUpdated();
		owner.addUpdateListener( new OwnerSizeListener()
		{

			@Override
			public void ownerSizeUpdated()
			{
				setNeedsRender( true );
				IconSprite.this.ownerSizeUpdated();
			}

		} );

	}

	@Override
	public SpriteOwner getOwner()
	{
		return owner;
	}

	/**
	 *
	 */
	protected boolean showControlPoint = false;

	/**
	 *
	 * @param showControlPoint
	 */
	public void setShowControlPoint( boolean showControlPoint )
	{
		this.showControlPoint = showControlPoint;
	}

	/**
	 *
	 */
	//protected final Point2D.Float renderedLocation = new Point2D.Float( 0, 0 );
	/**
	 *
	 */
	protected DoubleDimension renderedSize = new DoubleDimension( DEFAULT_SIZE.getWidth(),
		DEFAULT_SIZE.getHeight() );

	/**
	 * @return the showControlPoint
	 */
	public boolean isShowControlPoint()
	{
		return showControlPoint;
	}

	private AlphaComposite makeComposite( double alpha )
	{
		return ( AlphaComposite.getInstance(
			AlphaComposite.SRC_OVER,
			(float) alpha ) );
	}

	/**
	 * The current angle to which all frames are rotated before display
	 */
	protected float rotationAngle = 0.0f;

	/**
	 *
	 * @return angle in degrees
	 */
	@Override
	public double getRotationAngle()
	{
		return rotationAngle;
	}

	/**
	 * @param size the size to set
	 */
	@Override
	public void setRelativeSize( Dimension2D size )
	{
		this.relativeSize = size;
		updateLocationTransform();
	}

	/**
	 * @param width
	 * @param height
	 */
	@Override
	public void setRelativeSize( double width, double height )
	{
		setRelativeSize( new DimensionFloat( (float) width, (float) height ) );
		ownerSizeUpdated();
	}

	/**
	 * Sets the position offset of the sprite location. Default is top left
	 * (NORTHWEST)
	 *
	 * @param locationAnchor
	 */
	@Override
	public void setAnchor( Anchor.Position locationAnchor )
	{
		// changing the anchor should not change the bounds
		Rectangle2D rect = getRelativeBounds();
		iconRenderer.setAnchor( locationAnchor );
		setRelativeBounds( rect );
		watcher.changedAnchor( locationAnchor );

	}

	/**
	 *
	 * @param x
	 * @param y
	 */
	@Override
	public void setRelativeLocation( double x, double y )
	{
		this.location.setLocation(
			x,
			y );
		updateLocationTransform();
	}

	/**
	 *
	 * @param name
	 */
	@Override
	public final void setId( String name )
	{
		this.id = name;
	}

	/**
	 *
	 * @param degrees
	 */
	@Override
	public void setRotationAngle( double degrees )
	{
		this.rotationAngle = (float) degrees;
		iconRenderer.setAngle( degrees );
		watcher.changedRotationAngle( degrees );
	}

	/**
	 * @return the size
	 */
	@Override
	public Dimension2D getRelativeSize()
	{
		return relativeSize;
	}

	@Override
	public double getScale()
	{
		return iconRenderer.getScale();
	}

	/**
	 * @param z_order the z_order to set
	 */
	@Override
	public void setZ_order( int z_order )
	{
		this.z_order = z_order;
		watcher.changedZ_order( z_order );
	}

	/**
	 * @return the z_order
	 */
	@Override
	public int getZ_order()
	{
		return z_order;
	}

	/**
	 *
	 * @return
	 */
	public boolean isVisible()
	{
		return transparency > 0 && visible == true;
	}

	/**
	 * @param f
	 */
	@Override
	public void setTransparency( double f )
	{
		setRenderComposite( makeComposite( f ) );
		transparency = f;
		watcher.changedTransparency( f );
	}

	/**
	 * @param d
	 */
	@Override
	public void setScale( double d )
	{
		iconRenderer.setScale( d );
		updateLocationTransform();
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible( boolean visible )
	{
		this.visible = visible;
	}

	/**
	 * @param needsRender the needsRender to set
	 */
	public void setNeedsRender( boolean needsRender )
	{
		this.needsRender = needsRender;
		if (needsRender)
		{
			iconRenderer.setNeedsRender( true );
		}
	}

	/**
	 * @return the needsRender
	 */
	public boolean isNeedsRender()
	{
		return needsRender;
	}

	private SpriteLayer container = null;

	@Override
	public void setContainer( SpriteLayer container )
	{
		this.container = container;
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		if (owner != null)
		{
			return owner.getGraphicsConfiguration();
		}
		return null;
	}

	@Override
	public void setFlip( FlipType flip )
	{
		iconRenderer.setFlip( flip );
		watcher.changedFlip( flip );
	}

	@Override
	public FlipType getFlip()
	{
		return iconRenderer.getFlip();
	}

	@Override
	public void update()
	{
		setNeedsRender( true );
		if (null != whenUpdated)
		{
			whenUpdated.update( this );
		}
	}

	private ISpriteWatcher watcher = NOPSpriteWatcher.get();

	@Override
	public void setWatcher( ISpriteWatcher watcher )
	{
		this.watcher = watcher;
	}

	private UpdateListener whenUpdated = null;

	@Override
	public void setUpdateListener( UpdateListener listener )
	{
		whenUpdated = listener;
	}

	static class TestSprite extends IconSprite
	{

		public TestSprite( String name, final Color toPaint )
		{
			super( new Icon()
			{

				@Override
				public void paintIcon( Component c, Graphics g, int x, int y )
				{
					g.setColor( toPaint );
					g.fillRect(
						0,
						0,
						200,
						100 );
				}

				@Override
				public int getIconWidth()
				{
					return 200;
				}

				@Override
				public int getIconHeight()
				{
					return 100;
				}

			} );
			setRelativeSize( DEFAULT_SIZE );

		}

	}

	public static void main( String[] args )
	{
		final IconSprite testSprite = new TestSprite( "s1", Color.blue );
		testSprite.setRelativeLocation(
			0.7,
			0.7 );
		testSprite.setRotationAngle( 45 );
		testSprite.setAnchor( Anchor.Position.NORTHEAST );
		testSprite.setShowControlPoint( true );
		final AnimatedScene sp = new AnimatedScene();
		sp.setLayout( null );
		//        final SpriteCanvas canvas = new SpriteCanvas();
		//sp.setForegroundPainter( ShapeEffectStack.get( SetColor.get( Color.green ), new GridLinesEffect( 10 ) ) );
		sp.addSprites( testSprite );

		final IconSprite cloneSprite = createClone( testSprite );
		sp.addSprites( cloneSprite );
		cloneSprite.setRelativeLocation(
			0.2,
			0.2 );
		cloneSprite.setScale( 0.5 );
		cloneSprite.setShowControlPoint( true );

		final IconSprite fish = new IconSprite( "fishy" );
		fish.setIcon( new ScalableSVGIcon( NoImageIcon.get()
			.getURL() ) );

		fish.setRelativeLocation(
			0.5,
			0.5 );

		ISprite is = JLabelSprite.create( fish );

		sp.addSprites( is );

		//        new WhenMadeVisible( sp )
		//        {
		//
		//            @Override
		//            public void doThis( AWTEvent e )
		//            {
		//                //      canvas.setGraphicsConfiguration( sp.getGraphicsConfiguration() );
		//            }
		//
		//        };
		//sp.setContentPainter( canvas );

		final JFrame jft = new JFrame( "SpriteTest" );
		jft.setSize(
			600,
			400 );
		jft.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jft.setContentPane( sp );
		final JSlider rotator = new JSlider( 0, 360 )
		{

			@Override
			public void paint( Graphics g )
			{
				super.paint( g );
			}

		};
		rotator.setIgnoreRepaint( true );
		rotator.addChangeListener( new ChangeListener()
		{

			@Override
			public void stateChanged( ChangeEvent e )
			{
				fish.setRotationAngle( rotator.getValue() );
				testSprite.setRotationAngle( rotator.getValue() );
				rotator.invalidate();

			}

		} );
		sp.add( rotator );
		rotator.setBounds(
			0,
			100,
			300,
			30 );
		rotator.setOpaque( false );
		rotator.setBounds(
			0,
			0,
			300,
			20 );

		JButton b = new JButton( "Boo" );
		sp.add( b );
		//b.setOpaque( true );

		b.setAction( new AbstractAction()
		{

			{
				putValue(
					NAME,
					"Boo" );
			}

			@Override
			public void actionPerformed( ActionEvent e )
			{
				JOptionPane.showConfirmDialog(
					jft,
					"Boo" );
			}

		} );
		b.setLocation(
			200,
			200 );
		b.setSize(
			100,
			100 );
		//ISprite isp = ComponentSprite.getFrom( b );
		//isp.setRelativeBounds( 0.8, 0.05, 0.08, 0.08 );
		//isp.setTransparency( 0.5 );
		GUIEventThread.show( jft );
	}

	boolean initialised = false;

	@Override
	public void ownerSizeUpdated()
	{
		if (owner != null)
		{
			AffineTransform at = owner.getTransform();
			pixelSize.width = (int) ( relativeSize.getWidth() * at.getScaleX() );
			pixelSize.height = (int) ( relativeSize.getHeight() * at.getScaleY() );
			iconRenderer.setUnScaledSize( pixelSize );
			iconRenderer.setAspectRatio( at.getScaleX() / at.getScaleY() );
			updateLocationTransform();
			initialised = true;

		}

	}

}
