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

package com.cybernostics.lib.collections;

import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author jasonw
 */
public class MapDump
{
	public static void dump( Map< ? extends Object, ? extends Object > todump )
	{
		for (Entry< ? extends Object, ? extends Object > eachentry : todump.entrySet())
		{
			System.out.println( String.format(
				"%s=%s\n",
				eachentry.getKey(),
				eachentry.getValue() ) );
		}
	}

	public static String asString( Map< ? extends Object, ? extends Object > todump )
	{
		StringBuilder sb = new StringBuilder();
		for (Entry< ? extends Object, ? extends Object > eachentry : todump.entrySet())
		{
			sb.append( String.format(
				"%s=%s\n",
				eachentry.getKey(),
				eachentry.getValue() ) );
			sb.append( '\n' );
		}
		return sb.toString();

	}
}
