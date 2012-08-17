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

import java.awt.Color;
import java.util.UUID;

public class Random
{

	public static int intRange( int low, int high )
	{
		return low + intValue( high - low );
	}

	public static int intValue( int maximum )
	{
		return (int) ( Math.random() * maximum );
	}

	public static double doubleValue( double maximum )
	{
		return ( Math.random() * maximum );
	}

	private static Color[] colors =
	{ Color.BLACK, Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA,
		Color.RED, Color.YELLOW, Color.WHITE };

	public static Color colorValue()
	{
		return colors[ intValue( colors.length ) ];
	}

	public static String stringValue( int length )
	{
		StringBuilder sb = new StringBuilder( length );
		for (int index = 0; index < length; ++index)
		{
			sb.append( letter() );
		}
		return sb.toString();
	}

	private static final String letters = "abcdefghijklmnopqrstuvwxyz";

	public static char letter()
	{
		return letters.charAt( Random.intValue( letters.length() ) );
	}

	public static String uid()
	{
		return UUID.randomUUID()
			.toString();
	}

	public static long longValue( long duration )
	{
		return (long) ( Math.random() * duration );
	}

}
