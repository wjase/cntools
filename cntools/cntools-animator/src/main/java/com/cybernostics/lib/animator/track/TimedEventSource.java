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

/**
 * @author jasonw
 *
 */
public interface TimedEventSource
{

	/**
	 * Resets the sequence to start again
	 */
	public void reset();

	/**
	 * 
	 * 
	 * @return
	 */
	public TimedEvent getNext();

	/**
	 * Gets the pre-event if any. This is fired what the sequence starts.
	 * @return
	 */
	public TimedEvent getInitial();

	/**
	 * Gets the post event if any. This is fired after the sequence ends
	 * @return
	 */
	public TimedEvent getPost();

}
