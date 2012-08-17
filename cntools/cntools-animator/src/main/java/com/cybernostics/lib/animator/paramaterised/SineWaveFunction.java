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
public class SineWaveFunction implements FloatFunction
{

	private float amplitute = 0;
	private float offset = 0;
	private float periods = 1;
	final static double Pi2 = Math.PI * 2;

	public SineWaveFunction( float min, float max, float periods )
	{
		amplitute = ( max - min ) / 2;
		offset = min + amplitute;
	}

	@Override
	public Float getPoint( float t )
	{
		return (float) ( amplitute * Math.sin( periods * Pi2 * t ) + offset );
	}
}
