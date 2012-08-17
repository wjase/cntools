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
package com.cybernostics.lib.animator.track.sound;

import com.cybernostics.lib.animator.sprite.ISprite;
import java.awt.geom.Point2D;

import com.cybernostics.lib.animator.track.LocationListener;
import com.cybernostics.lib.media.SoundEffect;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

/**
 * 
 * @author jasonw
 */
public class DirectionChangeListener
	implements
	LocationListener,
	PropertyChangeListener
{

	double lastSlope = 0.0;
	double currentSlope = 0.0;
	// circular buffer to check the current slope and the one before that
	Point2D points[] = new Point2D[ 3 ];
	int currentIndex = 0;
	// Don't start calculating until we have enough samples
	boolean bufferFilled = false;
	String clipName;
	SoundEffect theEffect;

	/**
	 * 
	 * @param soundFile
	 * @param volume
	 */
	public DirectionChangeListener( URL soundFile, double volume )
	{
		theEffect = new SoundEffect( soundFile, volume );
	}

	@Override
	public void setLocation( Point2D location, ISprite target )
	{
		points[ currentIndex ] = location;
		if (bufferFilled == false)
		{
			if (currentIndex == 2)
			{
				bufferFilled = true;
				int oldest = getOldestPoint();
				int previous = getPreviousPoint();

				lastSlope = ( points[ previous ].getY() - points[ oldest ].getY() )
					/ ( points[ previous ].getX() - points[ oldest ].getX() );
				currentSlope = ( points[ currentIndex ].getY() - points[ previous ].getY() )
					/ ( points[ currentIndex ].getX() - points[ previous ].getX() );
				if (checkBounce())
				{
					theEffect.play();
				}
			}
		}
		else
		{
			lastSlope = currentSlope;
			int previous = getPreviousPoint();
			currentSlope = ( points[ currentIndex ].getY() - points[ previous ].getY() )
				/ ( points[ currentIndex ].getX() - points[ previous ].getX() );
			if (checkBounce())
			{
				theEffect.play();
			}

		}

		currentIndex = ( currentIndex + 1 ) % 3;
	}

	private int getOldestPoint()
	{
		return ( currentIndex + 1 ) % 3;
	}

	private int getPreviousPoint()
	{
		return ( currentIndex + 2 ) % 3;
	}

	private boolean checkBounce()
	{
		return ( ( currentSlope < 0 ) && ( lastSlope > 0 ) );
	}

	@Override
	public void propertyChange( PropertyChangeEvent evt )
	{
		if (evt.getPropertyName()
			.equalsIgnoreCase(
				"location" ))
		{
			Point2D loc = (Point2D) evt.getNewValue();
			System.out.printf(
				"Location %f,%f",
				loc.getX(),
				loc.getY() );
			setLocation(
				loc,
				(ISprite) evt.getSource() );
		}
	}
}
