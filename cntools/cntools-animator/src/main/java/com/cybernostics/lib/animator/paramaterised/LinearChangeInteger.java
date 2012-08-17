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

/**
 * 
 * @author jasonw
 */
public class LinearChangeInteger implements ParamaterisedFunction< Integer >
{

	int start;
	int stop;
	int range;

	/**
	 * 
	 * @param start
	 * @param stop
	 */
	public LinearChangeInteger( int start, int stop )
	{
		this.start = start;
		this.stop = stop;
		range = stop - start;
	}

	@Override
	public Integer getPoint( float t )
	{
		return (int) ( t * range + start );
	}

}
