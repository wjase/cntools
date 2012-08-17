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

public class IterableEnumeration< T > implements Iterable< T >
{

	public static < T > IterableEnumeration< T > get( Enumeration< T > toIt )
	{
		return new IterableEnumeration< T >( toIt );
	}

	private final Enumeration< T > en;

	public IterableEnumeration( Enumeration< T > en )
	{
		this.en = en;
	}

	// return an adaptor for the Enumeration
	@Override
	public Iterator< T > iterator()
	{
		return new Iterator< T >()
		{

			@Override
			public boolean hasNext()
			{
				return en.hasMoreElements();
			}

			@Override
			public T next()
			{
				return en.nextElement();
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
