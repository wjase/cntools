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

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Functions like PHP implode
 *
 * @author jasonw
 */
public class ArrayUtils
{

	public static < T > T[] copy( T[] src, T[] dest )
	{
		return copy(
			src,
			dest,
			0 );
	}

	public static < T > T[] copy( T[] src, T[] dest, int offset )
	{
		int max = dest.length;
		int index = offset;
		for (T eachOne : IterableArray.get( src ))
		{
			if (max == index)
			{
				break;
			}

			dest[ index++ ] = eachOne;
		}
		return dest;
	}

	public static < T > T[] copy( Collection< T > src, T[] dest )
	{
		return copy(
			src,
			dest,
			0 );
	}

	public static < T > T[] copy( Collection< T > src, T[] dest, int offset )
	{
		int max = dest.length;
		int index = offset;
		for (T eachOne : src)
		{
			if (max == index)
			{
				break;
			}
			dest[ index++ ] = eachOne;
		}
		return dest;
	}

	public static < T > T[] createArray( Class< T > clazz, int size )
	{
		return (T[]) Array.newInstance(
			clazz,
			size );
	}

	public static < T > T[] append( Class< T > aClass, T[] first, T... items )
	{
		return merge(
			aClass,
			first,
			items );
	}

	public static < T > T[] merge( Class< T > aClass, T[] first, T[] second )
	{
		return merge(
			aClass,
			first.length + second.length,
			first,
			second );
	}

	public static < T > T[] merge( Class< T > clazz, T[]... arrays )
	{
		int combinedLength = 0;
		for (T[] eachArray : arrays)
		{
			combinedLength += eachArray.length;
		}
		return merge(
			clazz,
			combinedLength,
			arrays );
	}

	public static < T > T[] merge( Class< T > clazz,
		int iCombinedLength,
		T[]... arrays )
	{
		int length = 0;

		T[] combined = createArray(
			clazz,
			iCombinedLength );

		int combinedIndex = 0;

		for (T[] eachArray : arrays)
		{
			if (eachArray == null)
			{
				continue;
			}
			length = eachArray.length;

			for (int index = 0; index < length; ++index)
			{
				combined[ combinedIndex ] = eachArray[ index ];
				combinedIndex++;
				if (combinedIndex == iCombinedLength)
				{
					return combined;
				}
			}
		}
		return combined;

	}

	//    public static <T> T[] merge( Class<T> clazz, int iCombinedLength, T[] first, T[] second )
	//    {
	//        if ( first == null )
	//        {
	//            return second.clone();
	//        }
	//        if ( second == null )
	//        {
	//            return first.clone();
	//        }
	//
	//        T[] combined = createArray( clazz, iCombinedLength );
	//
	//        int secondIndex = 0;
	//        for ( int index = 0; index < combined.length; ++index )
	//        {
	//            if ( index < first.length )
	//            {
	//                combined[ index] = first[ index];
	//            }
	//            else
	//            {
	//                combined[index] = second[ secondIndex];
	//                secondIndex++;
	//            }
	//        }
	//
	//        return combined;
	//    }
	//
	public static String implode( String glueString, Iterable< ? > toShow )
	{
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for (Object eachOne : toShow)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				sb.append( glueString );
			}
			sb.append( eachOne.toString() );
		}
		return sb.toString();
	}

	public static String implode( String glueString, Collection< ? > inputArray )
	{
		return implode(
			glueString,
			(Iterable< ? >) inputArray );
	}

	public static String implode( String glueString, String... inputArray )
	{

		/**
		 * Output variable
		 */
		String output = "";

		if (( inputArray != null && inputArray.length > 0 ))
		{
			StringBuilder sb = new StringBuilder();
			sb.append( inputArray[ 0 ] );

			for (int i = 1; i < inputArray.length; ++i)
			{
				sb.append( glueString );
				sb.append( inputArray[ i ] );
			}

			output = sb.toString();
		}

		return output;
	}

	public static < T > T[] asList( T... items )
	{
		return items;
	}

}
