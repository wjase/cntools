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

import com.cybernostics.lib.collections.IterableArray;

/**
 *
 * @author jasonw
 */
public class SumFunction implements FloatFunction
{

	public SumFunction( FloatFunction... fns )
	{
		funcs = IterableArray.get( fns );
	}

	IterableArray< FloatFunction > funcs = null;

	@Override
	public Float getPoint( float t )
	{
		float sum = 0;
		for (FloatFunction eachFn : funcs)
		{
			sum += eachFn.getPoint( t );
		}

		return sum;
	}
}
