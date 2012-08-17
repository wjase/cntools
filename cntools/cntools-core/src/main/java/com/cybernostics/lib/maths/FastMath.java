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

/**
 *
 * @author jasonw
 */
public class FastMath
{

	public static final double PION2 = Math.PI / 2;

	public static final double PION4 = Math.PI / 4;

	public static final double _2PI = Math.PI * 2;

	/**
	 * Fast Trig functions for x86. This forces the trig function to stay within
	 * the safe area on the x86 processor (-45 degrees to +45 degrees) The
	 * results may be very slightly off from what the Math and StrictMath trig
	 * functions give due to rounding in the angle reduction but it will be very
	 * very close.
	 */
	static double reduceSinAngle( double radians )
	{
		radians %= _2PI; // put us in -2PI to +2PI space
		if (Math.abs( radians ) > Math.PI)
		{ // put us in -PI to +PI space
			radians = radians - ( _2PI );
		}
		if (Math.abs( radians ) > PION2)
		{// put us in -PI/2 to +PI/2 space
			radians = Math.PI - radians;
		}

		return radians;
	}

	static double fastSin( double radians )
	{
		radians = reduceSinAngle( radians ); // limits angle to between -PI/2 and +PI/2
		if (Math.abs( radians ) <= PION4)
		{
			return Math.sin( radians );
		}
		else
		{
			return Math.cos( PION2 - radians );
		}
	}

	static double fastCos( double radians )
	{
		return fastSin( radians + PION2 );
	}

}
