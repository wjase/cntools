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

import com.cybernostics.lib.gui.shape.IconRect;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.Icon;

/**
 *
 * @author jasonw
 */
public class Anchor
{

	private static final double HALF = 0.5;

	/**
	 *
	 */
	public static enum Position
	{

		/**
		 *
		 */
		CENTRE,
		/**
		 *
		 */
		EAST,
		/**
		 *
		 */
		NORTH,
		/**
		 *
		 */
		NORTHEAST,
		/**
		 *
		 */
		NORTHWEST,
		/**
		 *
		 */
		SOUTH,
		/**
		 *
		 */
		SOUTHEAST,
		/**
		 *
		 */
		SOUTHWEST,
		/**
		 *
		 */
		WEST
	};

	private static Point2D[] points =
	{
		/**
		 *
		 */
		new Point2D.Double( HALF, HALF ), // CENTRE
		/**
		 *
		 */
		new Point2D.Double( 1, HALF ),// EAST
		/**
		 *
		 */
		new Point2D.Double( HALF, 0 ),//NORTH
		/**
		 *
		 */
		new Point2D.Double( 1, 0 ),//NORTHEAST,
		/**
		 *
		 */
		new Point2D.Double( 0, 0 ),//NORTHWEST
		/**
		 *
		 */
		new Point2D.Double( HALF, 1 ),//SOUTH,
		/**
		 *
		 */
		new Point2D.Double( 1, 1 ),//SOUTHEAST,
		/**
		 *
		 */
		new Point2D.Double( 0, 1 ),//SOUTHWEST,
		/**
		 *
		 */
		new Point2D.Double( 0, HALF ) //WEST
	};

	public Anchor()
	{
	}

	public Anchor( Rectangle2D itemRect, Position anchor )
	{
		myPos = anchor;
		this.itemRect = itemRect;
	}

	public Anchor( Icon c, Position anchor )
	{
		itemRect = IconRect.get( c );
	}

	private Position myPos = Position.CENTRE;

	private Rectangle2D itemRect = new Rectangle2D.Double( 0, 0, 1, 1 );

	public void setItemRect( Rectangle2D itemRect )
	{
		this.itemRect = itemRect;
		setPosition( myPos );
	}

	public Position getPosition()
	{
		return myPos;
	}

	private Point2D anchorLocation = new Point2D.Double( 0, 0 );

	/**
	 * Returns the point on the given rect corresponding to the current anchor
	 * position.
	 *
	 * @return
	 */
	public Point2D getAnchorLocation()
	{
		return anchorLocation;
	}

	/**
	 * Returns a Point2D normalised to the width and height of the icon 0->1
	 *
	 * @return
	 */
	public Point2D getRelativeAnchorLocation()
	{
		return points[ myPos.ordinal() ];
	}

	public void setPosition( Position locationAnchor )
	{
		this.myPos = locationAnchor;
		switch (locationAnchor)
		{
			case CENTRE:
				anchorLocation.setLocation(
					HALF * itemRect.getWidth(),
					HALF * itemRect.getHeight() );
				break;
			case EAST:
				anchorLocation.setLocation(
					itemRect.getWidth(),
					HALF * itemRect.getHeight() );
				break;
			case SOUTHEAST:
				anchorLocation.setLocation(
					itemRect.getWidth(),
					itemRect.getHeight() );
				break;
			case SOUTH:
				anchorLocation.setLocation(
					HALF * itemRect.getWidth(),
					itemRect.getHeight() );
				break;
			case SOUTHWEST:
				anchorLocation.setLocation(
					0.0f,
					itemRect.getHeight() );
				break;
			case WEST:
				anchorLocation.setLocation(
					0.0f,
					HALF * itemRect.getHeight() );
				break;
			case NORTHWEST:
				anchorLocation.setLocation(
					0.0f,
					0.0f );
				break;
			case NORTH:
				anchorLocation.setLocation(
					HALF * itemRect.getWidth(),
					0.0f );
				break;
			case NORTHEAST:
				anchorLocation.setLocation(
					itemRect.getWidth(),
					0.0f );
				break;
		}

	}

}
