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

package com.cybernostics.lib.reference;

import java.lang.ref.SoftReference;

/**
 * This class encapsulates both the SoftReference and the code to recreate the
 * referred item. For example an Icon can be reloaded if it had been previously
 * garbage collected.
 *
 * @author jasonw
 */
abstract public class RegeneratingSoftReference< T >
{

	SoftReference< T > referent = null;

	public RegeneratingSoftReference( T referent )
	{
		this.referent = new SoftReference< T >( referent );
	}

	public RegeneratingSoftReference()
	{
	}

	public T get()
	{
		if (referent == null || referent.get() == null)
		{
			referent = new SoftReference< T >( create() );
		}
		return referent.get();

	}

	abstract public T create();

}
