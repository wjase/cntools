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

/**
 * Implements a linear range of values from start to stop as the normalised
 * animation time period moves from 0.0 to 1.0
 *
 * @author jasonw
 */
public class LinearChangeDouble implements ParamaterisedFunction< Double >
{

	public static LinearChangeDouble create( double start, double stop )
	{
		return new LinearChangeDouble( start, stop );
	}

	private double start;

	private double range;

	/**
	 *
	 * @param start
	 * @param stop
	 */
	public LinearChangeDouble( double start, double stop )
	{
		this.start = start;

		range = stop - start;
	}

	@Override
	public Double getPoint( float t )
	{
		if (t > 1.0)
		{
			t = 1.0f;
		}
		return t * range + start;
	}

}
