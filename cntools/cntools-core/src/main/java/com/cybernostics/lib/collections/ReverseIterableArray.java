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

import java.util.Iterator;

public class ReverseIterableArray< T > implements Iterable< T >
{

	public static < T > ReverseIterableArray< T > get( T[] toIt )
	{
		return new ReverseIterableArray< T >( toIt );
	}

	private final T[] en;

	public ReverseIterableArray( T[] en )
	{
		this.en = en;
	}

	// return a reverse iterator for the array
	@Override
	public Iterator< T > iterator()
	{
		return new Iterator< T >()
		{
			int index = en.length;

			@Override
			public boolean hasNext()
			{
				return index > 0;
			}

			@Override
			public T next()
			{
				--index;
				return en[ index ];
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
