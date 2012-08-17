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
package com.cybernostics.lib.animator.test;

public class ExampleObject
{
	StringBuilder ouput = new StringBuilder();

	public double getABoundProperty()
	{
		return aBoundProperty;
	}

	public void setABoundProperty( double boundProperty )
	{
		aBoundProperty = boundProperty;
		String s = String.format(
			"Set bound property value:%f\n",
			boundProperty );
		ouput.append( s );
	}

	public double aSimpleProperty = -1.0f;
	private double aBoundProperty = -1.0f;

	public String getLog()
	{
		return ouput.toString();
	}
}