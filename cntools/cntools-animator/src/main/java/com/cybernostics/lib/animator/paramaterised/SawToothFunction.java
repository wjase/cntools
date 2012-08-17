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
 * Linear change from low to high and back down again over the period
 *
 * @author jasonw
 *
 */
public class SawToothFunction implements DoubleFunction
{

	LinearChange upward = null;

	LinearChange downward = null;

	/**
	 *
	 * @param high
	 * @param low
	 */
	public SawToothFunction( float high, float low )
	{
		upward = new LinearChange( low, high );
		downward = new LinearChange( high, low );

	}

	public SawToothFunction( double high, double low )
	{
		upward = new LinearChange( low, high );
		downward = new LinearChange( high, low );

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction#getPoint
	 * (float)
	 */
	@Override
	public Double getPoint( float t )
	{
		if (t > 0.5f)
		{
			return downward.getPoint( ( t - 0.5f ) * 2 );
		}
		return upward.getPoint( t * 2 );
	}

}
