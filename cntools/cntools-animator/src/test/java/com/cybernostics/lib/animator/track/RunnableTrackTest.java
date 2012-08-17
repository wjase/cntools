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

import com.cybernostics.lib.animator.track.ordering.SerialTrack;
import com.cybernostics.lib.concurrent.ConcurrentStringBuilder;
import com.cybernostics.lib.concurrent.RunnableObject;
import java.util.concurrent.TimeoutException;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class RunnableTrackTest
{

	public RunnableTrackTest()
	{
	}

	class TestRunnable extends RunnableObject< String >
	{

		ConcurrentStringBuilder sb = null;

		public TestRunnable( String param, ConcurrentStringBuilder sb )
		{
			super( param );
			this.sb = sb;
		}

		@Override
		public void run()
		{
			sb.append( getObject() );
		}

		@Override
		public String toString()
		{
			return "Runable(" + getObject() + ")";
		}

	}

	/**
	 * Test of update method, of class RunnableTrack.
	 */
	@Test
	public void testUpdate() throws InterruptedException, TimeoutException
	{

		ConcurrentStringBuilder sb = new ConcurrentStringBuilder();
		Sequencer seq = Sequencer.get();

		BasicTrack tracks = SerialTrack.connect(
			RunnableTrack.wrap( new TestRunnable( "first", sb ) ),
			RunnableTrack
				.wrap( new TestRunnable( "second", sb ) ),
			RunnableTrack.wrap( new TestRunnable( "third", sb ) ) );
		seq.addAndStartTrack( tracks );
		tracks.await( 300 );
		Assert.assertEquals(
			"firstsecondthird",
			sb.toString() );

	}

}
