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

package com.cybernostics.lib.patterns.singleton;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author jasonw
 */
public class DefaultConstructorSingleton< T > extends SingletonInstance< T >
{
	Class< T > clazz = null;

	/**
	 * The class argument is needed because you can't access default constructor
	 * on a generic type argument
	 * @param clazz 
	 */
	public DefaultConstructorSingleton( Class< T > clazz )
	{
		this.clazz = clazz;
	}

	@Override
	protected T createInstance()
	{
		try
		{
			return clazz.newInstance();
		}
		catch (Exception ex)
		{
			Logger.getLogger(
				DefaultConstructorSingleton.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );

		}
		return null;
	}

}
