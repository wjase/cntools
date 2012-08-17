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

import com.cybernostics.lib.animator.paramaterised.LinearChange;
import com.cybernostics.lib.animator.paramaterised.ParamaterisedFunction;

/**
 * @author jasonw
 *
 */
public class DoublePropertyTrack extends
	ReflectionPropertyAnimatorTrack< Double >
{

	/**
	 *
	 * @param seq
	 * @param toAnimate
	 * @param property
	 * @param start
	 * @param stop
	 * @param duration
	 * @return
	 */
	public static DoublePropertyTrack animate( Sequencer seq,
		Object toAnimate,
		String property,
		float start,
		float stop,
		long duration )
	{
		LinearChange lcf = new LinearChange( start, stop );
		DoublePropertyTrack fpt = new DoublePropertyTrack( toAnimate.toString(),
			lcf,
			toAnimate,
			property,
			duration );
		if (seq != null)
		{
			seq.addAndStartTrack( fpt );
		}
		return fpt;

	}

	/**
	 * @param name
	 * @param animatorFunction
	 * @param animatorTarget
	 * @param fieldToAnimateName
	 * @param duration
	 */
	public DoublePropertyTrack(
		String name,
		ParamaterisedFunction< Double > animatorFunction,
		Object animatorTarget,
		String fieldToAnimateName,
		long duration )
	{
		super( name,
			animatorFunction,
			animatorTarget,
			fieldToAnimateName,
			duration );
	}

}
