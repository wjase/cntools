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
package com.cybernostics.lib.animator.track;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;

/**
 * Animates a property value for a field.
 * 
 * Usage:
 * 
 * see PropertyAnimatorTest
 * 
 * @author jasonw
 * 
 * @param <PropertyType>
 */
public class ReflectionPropertyAnimatorTrack< PropertyType > extends
	BasicTimerTrack
{

	private ParamaterisedFunction< PropertyType > animatorFunction;

	/**
	 * 
	 * @param animatorFunction
	 */
	public void setAnimatorFunction( ParamaterisedFunction< PropertyType > animatorFunction )
	{
		this.animatorFunction = animatorFunction;
	}

	private Object animatorTarget;
	private Field fieldToAnimate = null;
	private Method fieldSetterMethod = null;
	private boolean useAccessorMethod = true;

	/**
	 * 
	 * @param name
	 * @param animatorFunction
	 * @param animatorTarget
	 * @param fieldToAnimateName
	 * @param duration
	 */
	public ReflectionPropertyAnimatorTrack(
		String name,
		ParamaterisedFunction< PropertyType > animatorFunction,
		Object animatorTarget,
		String fieldToAnimateName,
		long duration )
	{
		super( name, duration );

		this.animatorFunction = animatorFunction;
		this.animatorTarget = animatorTarget;

		Class< ? extends Object > objClass = animatorTarget.getClass();

		String setterMethodName = fieldToAnimateName;

		if (!setterMethodName.startsWith( "set" ))
		{
			// build the set accessor from the field name according to naming
			// convention
			setterMethodName = "set"
				+ Character.toUpperCase( fieldToAnimateName.charAt( 0 ) )
				+ ( ( fieldToAnimateName.length() > 1 ) ? fieldToAnimateName.substring( 1 )
					: "" );
		}

		// try to use the setter otherwise set the field directly
		try
		{
			Class< ? extends Object > currentClass = objClass;

			// walk up the superclass tree till we find one with the method we
			// want to call
			//
			while (currentClass != null)
			{
				this.fieldSetterMethod = getMethodNamed(
					currentClass,
					setterMethodName );
				if (this.fieldSetterMethod != null)
				{
					break;
				}
				currentClass = currentClass.getSuperclass();
			}
			if (this.fieldSetterMethod == null)
			{
				useAccessorMethod = false;
			}

		}
		catch (Exception ex)
		{
			useAccessorMethod = false;
		}

		if (useAccessorMethod == false)
		{
			// Couldn't find setter so use field itself
			try
			{

				this.fieldToAnimate = objClass.getField( fieldToAnimateName );
			}
			catch (Exception e)
			{
				UnhandledExceptionManager.handleException( e );
			}

		}

	}

	private Method getMethodNamed( Class< ? > c, String name )
	{
		for (Method eachMethod : c.getDeclaredMethods())
		{
			if (eachMethod.getName()
				.equals(
					name ))
			{
				return eachMethod;
			}
		}
		return null;
	}

	@Override
	public void start()
	{
		super.start();
		updateProperty( 0 );
	}

	/**
	 * 
	 */
	public void stop( boolean fireEvents )
	{
		updateProperty( 1.0f );
		super.stop( fireEvents );
	}

	private void updateProperty( final float t )
	{

		// System.out.println( "Update property:" + t );
		if (useAccessorMethod)
		{
			try
			{
				PropertyType newValue = animatorFunction.getPoint( t );
				fieldSetterMethod.invoke(
					animatorTarget,
					newValue );
			}
			catch (Exception e)
			{
				UnhandledExceptionManager.handleException( e );
			}
		}
		else
		{
			try
			{
				fieldToAnimate.set(
					animatorTarget,
					animatorFunction.getPoint( t ) );
			}
			catch (IllegalArgumentException e)
			{
				UnhandledExceptionManager.handleException( e );
			}
			catch (IllegalAccessException e)
			{
				UnhandledExceptionManager.handleException( e );
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.lib.animator.track.BasicTimerTrack#update(float)
	 */
	/**
	 * 
	 * @param t
	 */
	@Override
	public void update( float t )
	{
		updateProperty( t );

	}

}
