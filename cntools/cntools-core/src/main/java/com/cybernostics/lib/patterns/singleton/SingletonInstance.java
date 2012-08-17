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

/**
 * This class wraps the pattern where you have
 *
 * private SomeClass anObject = null
 *
 * public getAnObject() { if(anObject==null) { anObject = //create an Object
 * somehow } return anObject; }
 *
 * While it doesn't take a heap less code it wraps the idea that there should
 * only be one of these in a single declaration. Someone else can't blunder
 * along later and not get that you only meant for there to be one of these.
 *
 * Implements the singleton pattern for a type Usage:
 *
 * Declare a private static instance and implement a public static accessor
 * which accesses it if required. For classes which have public empty
 * constructors, use SimpleSingleInstance, or otherwise overload createInstance
 * in your own version.
 *
 *
 * // somewhere deep in a class definition...
 *
 * private static SingletonInstance<SomeClass> onlyOne = new
 * SingletonInstance<SomeClass> { protected SomeClass createInstance() { //
 * implement whatever you need to here return new SomeClass(); } };
 *
 *
 * @author jasonw
 */
abstract public class SingletonInstance< T >
{

	private T internal = null;

	/**
	 * This is the access which optionally creates
	 */
	public T get()
	{
		if (internal == null)
		{
			internal = createInstance();

		}
		return internal;
	}

	/**
	 * Allow the singleton to be initialised externally if required. If get()
	 * has already been call
	 *
	 * @param toSet
	 */
	public void set( T toSet )
	{
		if (internal != null)
		{
			throw new RuntimeException( "Singleton already initialised..." );
		}
		internal = toSet;
	}

	abstract protected T createInstance();

}
