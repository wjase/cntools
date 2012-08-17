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

/**
 *
 * @author jasonw
 */
public class DoubleDimension extends Dimension2D
{

	Point2D size = new Point2D.Double();

	public DoubleDimension( Rectangle2D rect )
	{
		this( rect.getWidth(), rect.getHeight() );
	}

	public DoubleDimension( int width, int height )
	{
		size.setLocation(
			width,
			height );
	}

	public DoubleDimension( double width, double height )
	{
		size.setLocation(
			width,
			height );
	}

	public DoubleDimension( Dimension d )
	{
		size.setLocation(
			d.getWidth(),
			d.getHeight() );
	}

	public DoubleDimension()
	{
	}

	@Override
	public boolean equals( Object obj )
	{
		Dimension d = (Dimension) obj;
		return d.getHeight() == size.getY() && size.getX() == d.getWidth();

	}

	@Override
	public int hashCode()
	{
		int hash = 5;
		hash = 29
			* hash
			+ (int) ( Double.doubleToLongBits( size.getX() ) ^ ( Double.doubleToLongBits( size.getX() ) >>> 32 ) );
		hash = 29
			* hash
			+ (int) ( Double.doubleToLongBits( size.getY() ) ^ ( Double.doubleToLongBits( size.getY() ) >>> 32 ) );
		return hash;
	}

	@Override
	public double getHeight()
	{
		return size.getY();
	}

	@Override
	public double getWidth()
	{
		return size.getX();
	}

	@Override
	public void setSize( double width, double height )
	{
		size.setLocation(
			width,
			height );
	}

	private static final String formatstr = "(%f,%f)";

	@Override
	public String toString()
	{
		return String.format(
			formatstr,
			size.getX(),
			size.getY() );
	}

	public void transform( AffineTransform at )
	{
		at.transform(
			size,
			size );
	}
}
