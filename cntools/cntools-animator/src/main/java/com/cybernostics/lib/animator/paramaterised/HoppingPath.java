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
package com.cybernostics.lib.animator.paramaterised;

import java.awt.geom.Point2D;

/**
 * 
 * @author jasonw
 */
public class HoppingPath extends LinearPath
{

	float jumpHeight;

	/**
	 * 
	 * @param jumpHeight
	 * @param start
	 * @param stop
	 */
	public HoppingPath(
		float jumpHeight,
		Point2D.Float start,
		Point2D.Float stop )
	{
		super( start, stop );
		this.jumpHeight = jumpHeight;

	}

	@Override
	public Point2D getPoint( float t )
	{
		Point2D nextPoint = super.getPoint( t );

		float shrinkFactor = (float) ( ( 0.7 * ( 1 - t ) ) + 0.3 );
		// Use the shrink factor to reduce the jump height and distance to give
		// a perspective effect
		float delta = (float) Math.abs( ( ( jumpHeight * shrinkFactor ) )
			* Math.sin( t * ( 10 ) * Math.PI ) );
		nextPoint.setLocation(
			nextPoint.getX(),
			nextPoint.getY() - delta );

		return nextPoint;
	}
}
