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

import java.util.Comparator;

/**
 * Returns a reverse comparison for reverseSorting sets
 *
 * @author jasonw
 */
public class IntegerReverseComparator implements Comparator< Integer >
{

	@Override
	public int compare( Integer o1, Integer o2 )
	{
		return o2.compareTo( o1 );
	}

}