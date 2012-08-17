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
import com.cybernostics.lib.gui.declarative.events.SupportsPropertyChanges;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Adds notification to a sprite for processes which want to watch one
 *
 * @author jasonw
 */
public class WatchableSprite implements ISprite, SupportsPropertyChanges
{

	@Override
	public void setUpdateListener( UpdateListener listener )
	{
		spriteToWatch.setUpdateListener( listener );
	}

	@Override
	public void update()
	{
		spriteToWatch.update();
	}

	public ISprite getWatched()
	{
		return spriteToWatch;
	}

	@Override
	public void setContainer( SpriteLayer container )
	{
		spriteToWatch.setContainer( container );
	}

	@Override
	public void setRestoreContext( boolean value )
	{
		spriteToWatch.setRestoreContext( value );
	}

	public static WatchableSprite wrap( ISprite sprite )
	{
		return new WatchableSprite( sprite );
	}

	private ISprite spriteToWatch = null;

	private PropertyChangeSupport changeNotification = new PropertyChangeSupport( this );

	public WatchableSprite( ISprite toWatch )
	{
		spriteToWatch = toWatch;
	}

	//        fireChange( "framenumber", old, currentFrameNumber );
	//         fireChange( "transparency", null, renderTransparency );
	@Override
	public void setZ_order( int z_order )
	{
		spriteToWatch.setZ_order( z_order );
		fireChange(
			"z_order",
			null,
			z_order );

	}

	@Override
	public void setTransparency( double f )
	{
		spriteToWatch.setTransparency( f );
		fireChange(
			"transparency",
			null,
			f );
	}

	@Override
	public void setScale( double toScale )
	{
		spriteToWatch.setScale( toScale );
		fireChange(
			"scale",
			null,
			toScale );

	}

	@Override
	public void setRotationAngle( double toRotate )
	{
		spriteToWatch.setRotationAngle( toRotate );
		fireChange(
			"angle",
			null,
			toRotate );

	}

	@Override
	public void setRelativeSize( Dimension2D size )
	{
		spriteToWatch.setRelativeSize( size );
		fireChange(
			"size",
			null,
			size );
	}

	@Override
	public void setRelativeSize( double width, double height )
	{
		spriteToWatch.setRelativeSize(
			width,
			height );
	}

	@Override
	public void setRelativeLocation( Point2D loc )
	{
		spriteToWatch.setRelativeLocation( loc );
		fireChange(
			"location",
			null,
			loc );

	}

	@Override
	public void setRelativeLocation( double x, double y )
	{
		spriteToWatch.setRelativeLocation(
			x,
			y );
		fireChange(
			"location",
			null,
			new Point2D.Double( x, y ) );

	}

	@Override
	public void setRelativeBounds( Rectangle2D rect )
	{
		spriteToWatch.setRelativeBounds( rect );
	}

	@Override
	public void setRelativeBounds( double x,
		double y,
		double width,
		double height )
	{
		spriteToWatch.setRelativeBounds(
			x,
			y,
			width,
			height );
	}

	@Override
	public void setOwner( SpriteOwner owner )
	{
		spriteToWatch.setOwner( owner );
	}

	@Override
	public void setFlip( FlipType flip )
	{
		spriteToWatch.setFlip( flip );
		fireChange(
			"flip",
			null,
			flip );
	}

	@Override
	public FlipType getFlip()
	{
		return spriteToWatch.getFlip();
	}

	@Override
	public void setId( String name )
	{
		spriteToWatch.setId( name );
	}

	@Override
	public void setAnchor( Anchor.Position locationAnchor )
	{
		spriteToWatch.setAnchor( locationAnchor );
	}

	@Override
	public void render( Graphics2D g2 )
	{
		spriteToWatch.render( g2 );
	}

	@Override
	public void removePropertyChangeListener( PropertyChangeListener listener )
	{
	}

	@Override
	public int getZ_order()
	{
		return spriteToWatch.getZ_order();
	}

	@Override
	public double getTransparency()
	{
		return spriteToWatch.getTransparency();
	}

	@Override
	public double getScale()
	{
		return spriteToWatch.getScale();
	}

	@Override
	public double getRotationAngle()
	{
		return spriteToWatch.getRotationAngle();
	}

	@Override
	public Dimension2D getRelativeSize()
	{
		return spriteToWatch.getRelativeSize();
	}

	@Override
	public Point2D getRelativeLocation()
	{
		return spriteToWatch.getRelativeLocation();
	}

	@Override
	public Rectangle2D getRelativeBounds()
	{
		return spriteToWatch.getRelativeBounds();
	}

	@Override
	public SpriteOwner getOwner()
	{
		return spriteToWatch.getOwner();
	}

	@Override
	public String getId()
	{
		return spriteToWatch.getId();
	}

	@Override
	public void addPropertyChangeListener( PropertyChangeListener listener )
	{
		changeNotification.addPropertyChangeListener( listener );
	}

	/**
	 *
	 * @param type
	 * @param oldValue
	 * @param newValue
	 */
	public void fireChange( String type, Object oldValue, Object newValue )
	{
		changeNotification.firePropertyChange(
			type,
			oldValue,
			newValue );
	}

	@Override
	public void addPropertyChangeListener( String propertyName,
		PropertyChangeListener listener )
	{
		changeNotification.addPropertyChangeListener(
			propertyName,
			listener );
	}

	@Override
	public void ownerSizeUpdated()
	{
		spriteToWatch.ownerSizeUpdated();
	}

	@Override
	public void setShowControlPoint( boolean flag )
	{
		spriteToWatch.setShowControlPoint( flag );
	}

	@Override
	public void setWatcher( ISpriteWatcher watcher )
	{
		spriteToWatch.setWatcher( watcher );
	}

}
