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

import com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction;

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
public class AdapterAnimatorTrack< PropertyType > extends BasicTimerTrack
{

	private ParamaterisedFunction< PropertyType > animatorFunction;

	public ParamaterisedFunction< PropertyType > getAnimatorFunction()
	{
		return animatorFunction;
	}

	private AnimatedProperty< PropertyType > animateTarget;

	/**
	 *
	 * @param animatorFunction
	 */
	public void setAnimatorFunction( ParamaterisedFunction< PropertyType > animatorFunction )
	{
		this.animatorFunction = animatorFunction;
	}

	/**
	 *
	 * @param name
	 * @param animatorFunction
	 * @param animatorTarget
	 * @param fieldToAnimateName
	 * @param duration
	 */
	public AdapterAnimatorTrack(
		String name,
		long duration,
		ParamaterisedFunction< PropertyType > animatorFunction,
		AnimatedProperty< PropertyType > animateTarget )
	{
		super( name, duration );

		this.animatorFunction = animatorFunction;
		this.animateTarget = animateTarget;
	}

	@Override
	public void start()
	{
		super.start();
	}

	/**
	 *
	 */
	@Override
	public void stop( boolean fireEvents )
	{
		super.stop( fireEvents );
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
		animateTarget.update( animatorFunction.getPoint( t ) );
	}

}
