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

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author jasonw
 */
public class FilteredCollection< type > extends ArrayList< type >
{

	private CollectionFilter< type > filter;

	public FilteredCollection(
		CollectionFilter< type > filter,
		Collection< type > toFilter )
	{
		this.filter = filter;
		addAll( toFilter );
	}

	@Override
	public final boolean addAll( Collection< ? extends type > c )
	{
		for (type eachItem : c)
		{
			if (filter.include( eachItem ))
			{
				add( eachItem );
			}
		}
		return true;
	}

}
