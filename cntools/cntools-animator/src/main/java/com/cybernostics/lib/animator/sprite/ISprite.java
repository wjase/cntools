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

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A sprite is an image which is rendered within the bounds of a parent
 * component. The size, location are both in the range 0->1.0 which is scaled to
 * the dimensions of the owner component
 *
 * @author jasonw
 */
public interface ISprite extends OwnerSizeListener
{

	/**
	 * The Id is intended to be a unique identifier for this sprite, much like
	 * the id in SVG or Html
	 *
	 * @return
	 */
	public String getId();

	public void setShowControlPoint( boolean flag );

	public void setId( String name );

	public Rectangle2D getRelativeBounds();

	public void setRelativeBounds( double x,
		double y,
		double width,
		double height );

	public void setRelativeBounds( Rectangle2D rect );

	public Point2D getRelativeLocation();

	/**
	 * Move the sprite to the location where 0,0 is top/left and 1.0,1.0 is
	 * bottom / right
	 */
	public void setRelativeLocation( double x, double y );

	/**
	 * Move the sprite to the specified location.
	 *
	 * @see setRelativeLocation(double,double)
	 *
	 * @param loc
	 */
	public void setRelativeLocation( Point2D loc );

	public double getRotationAngle();

	public void setRotationAngle( double toRotate );

	public Dimension2D getRelativeSize();

	public void setRelativeSize( double width, double height );

	public void setRelativeSize( Dimension2D size );

	public void setScale( double toScale );

	public double getScale();

	public void render( Graphics2D g2 );

	public void setZ_order( int z_order );

	public int getZ_order();

	public void setTransparency( double f );

	public double getTransparency();

	public void setOwner( SpriteOwner owner );

	public SpriteOwner getOwner();

	public void setAnchor( Anchor.Position locationAnchor );

	/**
	 * Set this if the sprite needs to restore the Graphics2D state after
	 * calling render. default: true
	 *
	 */
	public void setRestoreContext( boolean value );

	public void setContainer( SpriteLayer container );

	public void setFlip( IconTransformer.FlipType flip );

	public IconTransformer.FlipType getFlip();

	public void update();

	public void setUpdateListener( UpdateListener listener );

	public void setWatcher( ISpriteWatcher watcher );

}
