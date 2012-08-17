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

import java.util.*;

public class IterableArray< T > implements Iterable< T >
{

	public static < T > IterableArray< T > get( T[] toIt )
	{
		return new IterableArray< T >( toIt );
	}

	private final T[] en;

	public IterableArray( T[] en )
	{
		this.en = en;
	}

	// return an adaptor for the Enumeration
	@Override
	public Iterator< T > iterator()
	{
		return new Iterator< T >()
		{

			int index = -1;

			@Override
			public boolean hasNext()
			{
				return index < en.length - 1;
			}

			@Override
			public T next()
			{
				++index;
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
