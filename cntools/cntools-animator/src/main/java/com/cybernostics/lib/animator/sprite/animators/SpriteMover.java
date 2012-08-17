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

package com.cybernostics.lib.animator.sprite.animators;

import com.cybernostics.lib.animator.paramaterised.LinearPath;
import com.cybernostics.lib.animator.paramaterised.Point2DFunction;
import com.cybernostics.lib.animator.sprite.ISprite;
import com.cybernostics.lib.animator.track.AdapterAnimatorTrack;
import com.cybernostics.lib.animator.track.AnimatedProperty;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackStartedListener;
import com.cybernostics.lib.animator.track.ordering.TrackUpdatedListener;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.maths.Slope;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class SpriteMover extends SpritePropertyAnimator
	implements
	AnimatedProperty< Point2D >
{

	public static AdapterAnimatorTrack< Point2D > followLine( final ISprite toMove,
		final Point2D from,
		final Point2D to,
		long duration )
	{
		AdapterAnimatorTrack< Point2D > t = move(
			toMove,
			LinearPath.create(
				from,
				to ),
			duration );
		t.addTrackStartedListener( new TrackStartedListener()
		{

			@Override
			public void trackStarted( Track source )
			{
				double angle = 360 - Slope.getAngle(
					from,
					to );
				toMove.setRotationAngle( angle );

			}

		} );
		return t;
	}

	public static AdapterAnimatorTrack< Point2D > moveLine( final ISprite toMove,
		double fromX,
		double fromY,
		double toX,
		double toY,
		long duration )
	{
		return move(
			toMove,
			LinearPath.create(
				fromX,
				fromY,
				toX,
				toY ),
			duration );
	}

	public static AdapterAnimatorTrack< Point2D > moveLine( final ISprite toMove,
		Point2D from,
		Point2D to,
		long duration )
	{
		return move(
			toMove,
			LinearPath.create(
				from,
				to ),
			duration );
	}

	public static AdapterAnimatorTrack< Point2D > moveTo( final ISprite toMove,
		double toX,
		double toY,
		long duration )
	{
		return moveTo(
			toMove,
			new Point2D.Double( toX, toY ),
			duration );
	}

	public static AdapterAnimatorTrack< Point2D > moveTo( final ISprite toMove,
		final Point2D to,
		long duration )
	{
		AdapterAnimatorTrack< Point2D > toRun = new AdapterAnimatorTrack< Point2D >(
			String.format(
				"Move %s to %3.3f,%3.3f",
				toMove.getId(),
				to.getX(),
				to.getY() ), duration, null, new SpriteMover( toMove ) )
		{

			boolean setPath = false;

			@Override
			public void start()
			{
				if (!setPath)
				{
					setPath = true;
					setAnimatorFunction( new LinearPath( toMove.getRelativeLocation(),
						to ) );
				}
				super.start();
			}

			String fullName = null;

			@Override
			public String getName()
			{
				if (fullName == null)
				{
					fullName = super.getName();
				}
				return fullName;
			}

		};
		return toRun;
	}

	public static AdapterAnimatorTrack< Point2D > move( final ISprite toMove,
		Point2DFunction path,
		long duration )
	{
		String name = String.format(
			"Move %s along path %s",
			toMove.getId(),
			path.toString() );
		AdapterAnimatorTrack< Point2D > toRun = new AdapterAnimatorTrack< Point2D >( name,
			duration,
			path,
			new SpriteMover(
				toMove ) );
		return toRun;
	}

	/**
	 * Moves the sprite around the path and adjusts the rotation to "point"
	 * along the path
	 *
	 * @param toMove - sprite to move
	 * @param path - path to follow
	 * @param duration - length of time to take ti move round the path
	 * @param angleOffset - adjust the angle to the sprite "points" the right
	 * way in case it is not oriented correctly to start with..
	 * @return
	 */
	public static AdapterAnimatorTrack< Point2D > follow( final ISprite toMove,
		Point2DFunction path,
		long duration,
		float angleOffset )
	{
		AdapterAnimatorTrack< Point2D > toRun = move(
			toMove,
			path,
			duration );

		toRun.addTrackUpdatedListener( new TrackUpdatedListener()
		{

			Point2D lastPoint = new Point2D.Double();

			double lastAngle = 0;

			@Override
			public void trackUpdated( TimeStamp now, Track source )
			{
				// figure out the angle from the current location to the last location
				// and set the rotation
				Point2D curPoint = toMove.getRelativeLocation();
				if (curPoint.equals( lastPoint ))
				{
					toMove.setRotationAngle( lastAngle );
				}
				else
				{
					lastAngle = 360 - Slope.getAngle(
						lastPoint,
						curPoint );
					toMove.setRotationAngle( lastAngle );
					lastPoint.setLocation( curPoint );
				}

			}

		} );

		return toRun;
	}

	public SpriteMover( ISprite value )
	{
		super( value );
	}

	@Override
	public void update( Point2D value )
	{
		toAnimate.setRelativeLocation( value );
	}

}
