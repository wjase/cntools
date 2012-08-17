/*
 * #%L cntools-core %% Copyright (C) 2012 Cybernostics Pty Ltd %% Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License. #L%
 */
package com.cybernostics.lib.maths;

import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DimensionFloat extends Dimension2D
{

	public DimensionFloat()
	{

	}

	public DimensionFloat( Dimension d )
	{
		mySize.x = d.width;
		mySize.y = d.height;
	}

	public DimensionFloat( Point2D.Float other )
	{
		mySize = other;
	}

	public DimensionFloat( float width, float height )
	{
		mySize.x = width;
		mySize.y = height;
	}

	/**
	 * @param width
	 * @param height
	 */
	public DimensionFloat( double width, double height )
	{
		mySize.x = (float) width;
		mySize.y = (float) height;
	}

	/**
	 * @param rect
	 */
	public DimensionFloat( Rectangle2D rect )
	{
		this( rect.getWidth(), rect.getHeight() );
	}

	@Override
	public double getHeight()
	{
		return mySize.y;
	}

	@Override
	public double getWidth()
	{
		return mySize.x;
	}

	public void setSize( Point2D.Float other )
	{
		mySize.x = other.x;
		mySize.y = other.y;
	}

	Point2D.Float mySize = new Point2D.Float();
	Point2D.Float transformed = new Point2D.Float();

	public DimensionFloat transform( AffineTransform at, DimensionFloat dest )
	{
		at.transform(
			mySize,
			transformed );
		if (dest == null)
		{
			setSize( transformed );
			return this;
		}

		dest.setSize( transformed );
		return dest;
	}

	@Override
	public void setSize( double width, double height )
	{
		mySize.x = (float) width;
		mySize.y = (float) height;

	}

	/**
	 * @return
	 */
	public Dimension getSize()
	{
		return new Dimension( (int) mySize.x, (int) mySize.y );
	}

}
