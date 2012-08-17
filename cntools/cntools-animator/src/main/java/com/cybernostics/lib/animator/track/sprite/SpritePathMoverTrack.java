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
package com.cybernostics.lib.animator.track.sprite;

import com.cybernostics.lib.animator.paramaterised.LinearPath;
import com.cybernostics.lib.animator.paramaterised.Point2DFunction;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.track.LocationListener;
import com.cybernostics.lib.concurrent.GUIEventThread;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * 
 * @author jasonw
 */
public class SpritePathMoverTrack extends SpriteAnimatorTrack
{

	public enum Direction
	{

		FORWARD, REVERSE
	}

	/**
	 * 
	 * @param name
	 * @param toMove
	 * @param toTravel
	 * @param duration
	 * @return
	 */
	public static SpritePathMoverTrack create( String name,
		ISprite toMove,
		Point2DFunction toTravel,
		long duration )
	{
		return new SpritePathMoverTrack( name, toMove, toTravel, duration );
	}

	/**
	 * 
	 * @param name
	 * @param toMove
	 * @param toTravel
	 * @param duration
	 * @return
	 */
	public static SpritePathMoverTrack createReverse( String name,
		ISprite toMove,
		Point2DFunction toTravel,
		long duration )
	{
		SpritePathMoverTrack mover = new SpritePathMoverTrack( name,
			toMove,
			toTravel,
			duration );
		mover.setDirection( Direction.REVERSE );
		return mover;
	}

	/**
	 * 
	 * @param name
	 * @param toMove
	 * @param xStart
	 * @param yStart
	 * @param xStop
	 * @param yStop
	 * @param duration
	 * @return
	 */
	public static SpritePathMoverTrack moveLinear( String name,
		ISprite toMove,
		double xStart,
		double yStart,
		double xStop,
		double yStop,
		long duration )
	{
		return new SpritePathMoverTrack( name,
			toMove,
			new LinearPath( new Point2D.Double( xStart, yStart ),
				new Point2D.Double( xStop, yStop ) ),
			duration );
	}

	Point2DFunction thePath;
	Direction direction = Direction.FORWARD;
	ArrayList< LocationListener > locationTrackers = new ArrayList< LocationListener >();

	/**
	 * 
	 * @param locListener
	 */
	public void addLocationListener( LocationListener locListener )
	{
		locationTrackers.add( locListener );
	}

	/**
	 * 
	 * @param newPoint
	 */
	public void fireLocationUpdated( final Point2D newPoint )
	{
		GUIEventThread.run( new Runnable()
		{

			public void run()
			{
				for (LocationListener eachListener : locationTrackers)
				{
					eachListener.setLocation(
						newPoint,
						getTarget() );
				}
			}
		} );

	}

	/**
	 * 
	 * @param name
	 * @param toMove
	 * @param thePath
	 * @param duration
	 */
	public SpritePathMoverTrack(
		String name,
		ISprite toMove,
		Point2DFunction thePath,
		long duration )
	{
		super( name, duration );
		setName( name );
		setTarget( toMove );

		this.thePath = thePath;

	}

	/**
	 * 
	 * @param t
	 */
	@Override
	public void update( float t )
	{
		if (direction == Direction.REVERSE)
		{
			t = 1.0f - t;
		}
		Point2D newLoc = thePath.getPoint( t );
		getTarget().setRelativeLocation(
			newLoc );
		fireLocationUpdated( newLoc );
	}

	/**
	 * 
	 * @param direction
	 */
	public void setDirection( Direction direction )
	{
		this.direction = direction;
	}
}
