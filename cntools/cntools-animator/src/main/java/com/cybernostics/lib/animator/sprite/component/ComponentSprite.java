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
package com.cybernostics.lib.animator.sprite.component;

import com.cybernostics.lib.animator.sprite.Anchor.Position;
import com.cybernostics.lib.animator.sprite.IconTransformer.FlipType;
import com.cybernostics.lib.animator.sprite.*;
import com.cybernostics.lib.exceptions.ShouldNeverThrow;
import com.cybernostics.lib.gui.CachedImage;
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.gui.shapeeffects.CompositeAdjust;
import com.cybernostics.lib.gui.shapeeffects.ShapeEffect;
import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.maths.DoubleDimension;
import com.cybernostics.lib.maths.Random;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

/**
 * Paints a component in the z-index stack of other sprites
 *
 * @author jasonw
 */
public class ComponentSprite extends ShapedPanel implements ISprite
{

	public static final String ID_PROP = "sprite_id";

	public static ComponentSprite getFrom( JComponent comp )
	{
		if (comp == null)
		{
			return null;
		}
		ComponentSprite is = (ComponentSprite) comp.getClientProperty( CLIENT_PROP_NAME );
		Container parent = comp.getParent();
		while (( is == null ) && ( parent != null )
			&& ( parent instanceof JComponent ))
		{
			JComponent jc = ( (JComponent) parent );
			is = (ComponentSprite) jc.getClientProperty( CLIENT_PROP_NAME );
			if (is != null)
			{
				jc.putClientProperty(
					CLIENT_PROP_NAME,
					is );
			}
			parent = parent.getParent();
		}

		return is;
	}

	public static final String CLIENT_PROP_NAME = "sprite";

	public static final String LATEST_BOUNDS = "latestbounds";

	private SpriteOwner owner;

	private Rectangle2D bounds = new Rectangle2D.Double();

	private CachedImage cache = null;

	private JComponent contained = null;

	public JComponent getContained()
	{
		return contained;
	}

	public ComponentSprite( JComponent comp )
	{
		setLayout( new GridLayout() );

		setOpaque( false );
		add( comp );
		contained = comp;

		setUI( null );
		//NoseyRepaintManager.install( this );

		cache = new CachedImage();

		cache.setRenderer( new ShapeEffect()
		{

			@Override
			public void draw( Graphics2D g2, Shape s )
			{
				Graphics g = g2.create();
				ComponentSprite.super.paintChildren( g );
				g.dispose();
			}

		} );

		comp.putClientProperty(
			ComponentSprite.CLIENT_PROP_NAME,
			this ); // i own you!
		comp.addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentMoved( ComponentEvent e )
			{
				updateRelativeBoundsFromComponent();
				cache.setSize( getSize() );
			}

			@Override
			public void componentResized( ComponentEvent e )
			{
				updateRelativeBoundsFromComponent();
			}

		} );

		comp.invalidate();
		comp.addPropertyChangeListener( new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				cache.setNeedsRender( true );
			}

		} );

	}

	@Override
	public String getId()
	{
		String id = (String) contained.getClientProperty( ID_PROP );
		if (id == null)
		{
			id = Random.uid();
			contained.putClientProperty(
				ID_PROP,
				id );
		}

		return id;
	}

	@Override
	public void setShowControlPoint( boolean flag )
	{
		throw new UnsupportedOperationException( "Not supported" );
	}

	@Override
	public void setId( String name )
	{
		contained.putClientProperty(
			ID_PROP,
			name );
	}

	@Override
	public Rectangle2D getRelativeBounds()
	{
		return bounds;
	}

	@Override
	public void setRelativeBounds( double x,
		double y,
		double width,
		double height )
	{
		bounds.setFrame(
			x,
			y,
			width,
			height );
		updateBounds();
	}

	@Override
	public void setRelativeBounds( Rectangle2D rect )
	{
		bounds.setFrame( rect );
		updateBounds();
	}

	@Override
	public Point2D getRelativeLocation()
	{
		return new Point2D.Double( bounds.getMinX(), bounds.getMinY() );
	}

	@Override
	public void setRelativeLocation( double x, double y )
	{
		setRelativeBounds(
			x,
			y,
			bounds.getWidth(),
			bounds.getHeight() );
	}

	@Override
	public void setRelativeLocation( Point2D loc )
	{
		setRelativeBounds(
			loc.getX(),
			loc.getY(),
			bounds.getWidth(),
			bounds.getHeight() );
	}

	@Override
	public double getRotationAngle()
	{
		return 0;
	}

	@Override
	public void setRotationAngle( double toRotate )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public Dimension2D getRelativeSize()
	{
		return new DoubleDimension( bounds );
	}

	@Override
	public void setRelativeSize( double width, double height )
	{
		setRelativeBounds(
			bounds.getMinY(),
			bounds.getMinY(),
			width,
			height );
	}

	@Override
	public void setRelativeSize( Dimension2D size )
	{
		setRelativeBounds(
			bounds.getMinY(),
			bounds.getMinY(),
			size.getWidth(),
			size.getHeight() );
	}

	@Override
	public void setScale( double toScale )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public double getScale()
	{
		return 1.0;
	}

	@Override
	public void repaint()
	{
		//        update();
		super.repaint();
	}

	@Override
	public void repaint( Rectangle r )
	{
		//        update();
		super.repaint( r );
	}

	@Override
	public void repaint( long tm )
	{
		//       update();
		super.repaint( tm );
	}

	@Override
	public void repaint( int x, int y, int width, int height )
	{
		//        update();
		super.repaint(
			x,
			y,
			width,
			height );
	}

	@Override
	public void repaint( long tm, int x, int y, int width, int height )
	{
		//        update();
		super.repaint(
			tm,
			x,
			y,
			width,
			height );
	}

	@Override
	public void update( Graphics g )
	{
		update();
		super.update( g );
	}

	@Override
	public void render( Graphics2D g2 )
	{
		StateSaver g_2 = StateSaver.wrap( g2 );
		try
		{
			Rectangle drawBounds = getBounds();
			//g_2.translate( drawBounds.x, drawBounds.y );
			g_2.setClip(
				0,
				0,
				drawBounds.width,
				drawBounds.height );
			composite.draw(
				g_2,
				null );
			//componentPainter.draw( ( Graphics2D ) g2.create(), bounds );
			cache.draw(
				(Graphics2D) g_2,
				0,
				0 );

			//managedComponent.paint( g_2 );
		}
		finally
		{
			g_2.restore();
		}
	}

	private int z_order = 0;

	@Override
	public void setZ_order( int z_order )
	{
		this.z_order = z_order;
		update();
	}

	@Override
	public int getZ_order()
	{
		return z_order;
	}

	private CompositeAdjust composite = new CompositeAdjust( 1 );

	@Override
	public void setTransparency( double f )
	{
		composite.setLevel( f );
		invalidate();
		repaint( 5 );
	}

	@Override
	public double getTransparency()
	{
		return composite.getLevel();
	}

	@Override
	public void setOwner( SpriteOwner owner )
	{
		this.owner = owner;
		updateBounds();
	}

	@Override
	public SpriteOwner getOwner()
	{
		return owner;
	}

	@Override
	public void setAnchor( Position locationAnchor )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public void setRestoreContext( boolean value )
	{
	}

	@Override
	public void setContainer( SpriteLayer container )
	{
	}

	@Override
	public void setFlip( FlipType flip )
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public FlipType getFlip()
	{
		throw new UnsupportedOperationException( "Not supported." );
	}

	@Override
	public void ownerSizeUpdated()
	{
		updateBounds();
	}

	private void updateBounds()
	{
		if (owner != null)
		{
			// if the sprite bounds aren't set yet but the component
			// bounds have been set then set the sprite bounds from the
			// component...
			///
			if (bounds.getWidth() == 0)
			{
				if (getBounds().width > 0)
				{
					updateRelativeBoundsFromComponent();
					return;
				}
				return; // no bounds yet
			}
			AffineTransform at = owner.getTransform();
			Rectangle latestBounds = at.createTransformedShape(
				bounds )
				.getBounds();
			putClientProperty(
				LATEST_BOUNDS,
				latestBounds );
			setBounds( latestBounds );
			cache.setSize(
				latestBounds.width,
				latestBounds.height );

		}
	}

	private void updateRelativeBoundsFromComponent()
	{
		SpriteOwner so = getOwner();
		if (so != null)
		{
			Rectangle r = getBounds();
			cache.setSize(
				r.width,
				r.height );
			Rectangle latest = (Rectangle) getClientProperty( LATEST_BOUNDS );
			if (latest != null
				&&
					( ( r.x == latest.x ) && ( r.y == latest.y )
						&& ( r.width == latest.width ) && ( r.height == latest.height ) ))
			{
				return; // prevent cicular updates from sprite to component to sprite...
			}
			try
			{
				AffineTransform at = so.getTransform();
				if (at.getScaleX() != 1)
				{
					bounds.setFrame( at.createInverse()
						.createTransformedShape(
							r )
						.getBounds2D() );
				}
			}
			catch (NoninvertibleTransformException ex)
			{
				ShouldNeverThrow.wrap( ex );
			}
		}
	}

	@Override
	public void update()
	{
		if (cache == null)
		{
			return;
		}
		if (cache != null)
		{
			if (cache.isNeedsRender())
			{
				return;
			}
			cache.setNeedsRender( true );
		}

		if (listener != null)
		{
			listener.update( this );
		}

		//System.out.printf( "update\n" );
	}

	@Override
	public void setWatcher( ISpriteWatcher watcher )
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	@Override
	public void paintComponent( Graphics g )
	{

		Graphics gc = g.create();
		try
		{
			composite.draw(
				(Graphics2D) g,
				null );
			ComponentSprite.super.paintChildren( g );

		}
		finally
		{
			gc.dispose();
		}

	}

	private static final SingletonInstance< UpdateListener > NOPListener = new SingletonInstance< UpdateListener >()
	{

		@Override
		protected UpdateListener createInstance()
		{
			return new UpdateListener()
			{

				@Override
				public void update( ISprite src )
				{
				}

			};
		}

	};

	private UpdateListener listener = NOPListener.get();

	@Override
	public void setUpdateListener( UpdateListener listener )
	{
		if (listener != null)
		{
			this.listener = listener;
		}
		else
		{
			this.listener = NOPListener.get();
		}
	}

}
