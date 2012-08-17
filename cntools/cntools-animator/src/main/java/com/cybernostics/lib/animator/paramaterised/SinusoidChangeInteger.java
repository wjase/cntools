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
public class SinusoidChangeInteger implements ParamaterisedFunction< Integer >
{

	int start;
	int stop;
	int range;

	/**
	 * 
	 * @param start
	 * @param stop
	 */
	public SinusoidChangeInteger( int start, int stop )
	{
		this.start = start;
		this.stop = stop;
		range = stop - start;
	}

	final static double minusPiOn2 = -1 * Math.PI / 2;

	@Override
	public Integer getPoint( float t )
	{
		return (int) ( start + ( range / 2 * ( 1 + Math.sin( minusPiOn2
			+ ( Math.PI * t ) ) ) ) );
	}

}
