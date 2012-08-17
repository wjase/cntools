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
import com.cybernostics.lib.gui.graphics.StateSaver;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.svg.SubRegionContainer;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.*;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 *
 * @author jasonw
 */
public class JLabelSprite extends JLabel implements ISprite, SubRegionContainer
{

	public static JLabelSprite create( ISprite isp )
	{
		if (isp instanceof JLabelSprite)
		{
			return (JLabelSprite) isp;
		}
		if (isp instanceof WatchableSprite)
		{
			return create( ( (WatchableSprite) isp ).getWatched() );

		}
		JLabelSprite jsl = new JLabelSprite( (IconSprite) isp );
		jsl.setUI( new BasicLabelUI() );
		return jsl;
	}

	public JLabelSprite( IconSprite isp )
	{
		if (isp == null)
		{
			throw new NullPointerException();
		}
		inner = isp;

		setIcon( isp );

		isp.setUpdateListener( new UpdateListener()
		{

			@Override
			public void update( ISprite src )
			{
				repaint();
			}

		} );
		isp.setWatcher( new ISpriteWatcher()
		{

			private void update()
			{
				repaint();
			}

			@Override
			public void changedId( String name )
			{
			}

			@Override
			public void changedRelativeBounds( Rectangle2D rect )
			{
				updateBounds();
				update();
			}

			@Override
			public void changedRotationAngle( double toRotate )
			{
				update();
			}

			@Override
			public void changedScale( double toScale )
			{
				update();
			}

			@Override
			public void changedZ_order( int z_order )
			{
				update();
			}

			@Override
			public void changedTransparency( double f )
			{
				update();
			}

			@Override
			public void changedAnchor( Position locationAnchor )
			{
				update();
			}

			@Override
			public void changedFlip( FlipType flip )
			{
				repaint();
			}

		} );
		updateBounds();
		addComponentListener( new ComponentAdapter()
		{

			@Override
			public void componentResized( ComponentEvent e )
			{
				super.componentResized( e );
				updateBounds();
			}

		} );

		putClientProperty(
			ComponentSprite.ID_PROP,
			isp.getId() );
	}

	public static final String LATEST_BOUNDS = "latestbounds";

	private IconSprite inner = null;

	@Override
	public void update()
	{
		if (inner.isNeedsRender())
		{
			return;
		}
		inner.update();
		if (listener != null)
		{
			listener.update( this );
		}
		repaint();
	}

	@Override
	public void setZ_order( int z_order )
	{
		inner.setZ_order( z_order );
		repaint();
	}

	public void setVisible( boolean visible )
	{
		inner.setVisible( visible );
		repaint();
	}

	public void setTransparency( double f )
	{
		inner.setTransparency( f );
		repaint();
	}

	public void setShowControlPoint( boolean showControlPoint )
	{
		inner.setShowControlPoint( showControlPoint );
		repaint();
	}

	public void setScale( double d )
	{
		inner.setScale( d );
		repaint();
	}

	public void setRotationAngle( double degrees )
	{
		if (inner.getRotationAngle() != degrees)
		{
			inner.setRotationAngle( degrees );
			repaint();
		}

	}

	public void setRestoreContext( boolean value )
	{
		inner.setRestoreContext( value );
	}

	protected void setRenderComposite( Composite renderTransparency )
	{
		inner.setRenderComposite( renderTransparency );
		repaint();
	}

	public void setRelativeSize( double width, double height )
	{
		inner.setRelativeSize(
			width,
			height );
		repaint();
	}

	public void setRelativeSize( Dimension2D size )
	{
		inner.setRelativeSize( size );
		repaint();
	}

	public void setRelativeLocation( double x, double y )
	{
		inner.setRelativeLocation(
			x,
			y );
		repaint();
	}

	public void setRelativeLocation( Point2D location )
	{
		inner.setRelativeLocation( location );
		repaint();
	}

	public void setRelativeBounds( Rectangle2D rect )
	{
		inner.setRelativeBounds( rect );
		repaint();
	}

	public void setRelativeBounds( double x,
		double y,
		double width,
		double height )
	{
		inner.setRelativeBounds(
			x,
			y,
			width,
			height );
		repaint();
	}

	protected void setNeedsRender( boolean needsRender )
	{
		inner.setNeedsRender( needsRender );
		repaint();
	}

	public final void setId( String name )
	{
		inner.setId( name );
		putClientProperty(
			ComponentSprite.ID_PROP,
			inner.getId() );
	}

	public void setFlip( FlipType flip )
	{
		inner.setFlip( flip );
		repaint();
	}

	public void setContainer( SpriteLayer container )
	{
		inner.setContainer( container );
	}

	public void setAnchor( Position locationAnchor )
	{
		inner.setAnchor( locationAnchor );
	}

	public boolean isVisible()
	{
		return inner.isVisible();
	}

	public boolean isShowControlPoint()
	{
		return inner.isShowControlPoint();
	}

	protected boolean isNeedsRender()
	{
		return inner.isNeedsRender();
	}

	@Override
	public int getZ_order()
	{
		return inner.getZ_order();
	}

	@Override
	public double getTransparency()
	{
		return inner.getTransparency();
	}

	@Override
	public double getScale()
	{
		return inner.getScale();
	}

	@Override
	public double getRotationAngle()
	{
		return inner.getRotationAngle();
	}

	protected Composite getRenderComposite()
	{
		return inner.getRenderComposite();
	}

	@Override
	public Dimension2D getRelativeSize()
	{
		return inner.getRelativeSize();
	}

	@Override
	public Point2D getRelativeLocation()
	{
		return inner.getRelativeLocation();
	}

	@Override
	public Rectangle2D getRelativeBounds()
	{
		return inner.getRelativeBounds();
	}

	@Override
	public SpriteOwner getOwner()
	{
		return inner.getOwner();
	}

	@Override
	public GraphicsConfiguration getGraphicsConfiguration()
	{
		return inner.getGraphicsConfiguration();
	}

	@Override
	public FlipType getFlip()
	{
		return inner.getFlip();
	}

	@Override
	public String getId()
	{
		return inner.getId();
	}

	@Override
	public void render( Graphics2D g2 )
	{
		StateSaver g3 = StateSaver.wrap( g2 );
		paintComponent( g3 );
		g3.restore();
	}

	@Override
	public void setOwner( SpriteOwner owner )
	{
		inner.setOwner( owner );
		updateBounds();
	}

	@Override
	public void ownerSizeUpdated()
	{
		inner.ownerSizeUpdated();
		updateBounds();
		repaint();
	}

	private void updateBounds()
	{
		SpriteOwner owner = inner.getOwner();
		if (owner != null)
		{
			Rectangle2D bounds = inner.getRelativeBounds();
			Point2D offset = inner.getHotspotOffset();
			// if the sprite bounds aren't set yet but the component
			// bounds have been set then set the sprite bounds from the
			// component...
			///
			if (bounds.getWidth() == 0)
			{
				if (getBounds().width > 0)
				{
					// if we need to get relative bounds from the component
					// i.e. one which has been laid out by a layout manager
					updateRelativeBoundsFromComponent();
					return;
				}
				return; // no bounds yet
			}

			AffineTransform at = owner.getTransform();
			Rectangle latestBounds = at.createTransformedShape(
				bounds )
				.getBounds();
			int width = inner.getIconWidth();
			int height = inner.getIconHeight();
			//
			//            if ( width > latestBounds.width )
			//            {
			//                latestBounds.x -= ( width - latestBounds.width / 2 );
			//                latestBounds.y -= ( height - latestBounds.height / 2 );
			//                latestBounds.width = width;
			//                latestBounds.height = height;
			//            }

			latestBounds.x -= offset.getX();
			latestBounds.y -= offset.getY();
			latestBounds.width = width;
			latestBounds.height = height;
			putClientProperty(
				LATEST_BOUNDS,
				latestBounds );

			setBounds( latestBounds );

		}
	}

	private void updateRelativeBoundsFromComponent()
	{
		SpriteOwner so = getOwner();
		if (so != null)
		{
			Rectangle r = getBounds();

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
					inner.setRelativeBounds( at.createInverse()
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
	public void setWatcher( ISpriteWatcher watcher )
	{
		throw new UnsupportedOperationException( "Not supported yet." );
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
		this.listener = listener;
	}

	private Rectangle2D NO_AREA = new Rectangle2D.Double( 0, 0, 0, 0 );

	@Override
	public Rectangle2D getItemRectangle( String regionName )
	{
		Icon i = getIcon();
		if (i instanceof SubRegionContainer)
		{
			return ( (SubRegionContainer) i ).getItemRectangle( regionName );
		}
		return NO_AREA;
	}

}
