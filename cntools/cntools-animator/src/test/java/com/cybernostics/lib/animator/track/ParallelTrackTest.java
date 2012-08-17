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

import com.cybernostics.lib.animator.track.ordering.ParallelTrack;
import com.cybernostics.lib.animator.track.ordering.SerialTrack;
import com.cybernostics.lib.concurrent.ConcurrentStringBuilder;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.maths.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class ParallelTrackTest
{

	public ParallelTrackTest()
	{
	}

	class CharTrack extends BasicTrack
	{

		private ConcurrentStringBuilder sb;

		private char toAdd;
		boolean bFired = false;

		/**
		 * Random delay for the task
		 */
		private long duration = Random.intValue( 500 );

		public CharTrack( char c, ConcurrentStringBuilder sb )
		{
			super( "Track " + c );
			this.sb = sb;
			this.toAdd = c;
		}

		@Override
		public void update( TimeStamp timeCode )
		{
			if (timeCode.getElapsed() > duration)
			{
				if (!bFired)
				{
					sb.append( toAdd );
					stop( true );
				}
				else
				{
					Assert.fail( "Track called multiple times" );
				}
				bFired = true;
			}

		}

		@Override
		public void pause()
		{
			throw new UnsupportedOperationException( "Not supported yet." );
		}

		@Override
		public void resume()
		{
			throw new UnsupportedOperationException( "Not supported yet." );
		}

	}

	/**
	 * Test of connect method, of class SerialTrack.
	 */
	@Test
	public void testSerial() throws InterruptedException, TimeoutException
	{
		//BasicTrack.setDebug( true );
		Sequencer seq = Sequencer.get();
		seq.start();

		final ConcurrentStringBuilder sb1 = new ConcurrentStringBuilder();

		CharTrack trackA = new CharTrack( 'a', sb1 );
		CharTrack trackB = new CharTrack( 'b', sb1 );
		CharTrack trackC = new CharTrack( 'c', sb1 );

		BasicTrack serTracks = SerialTrack.connect(
			trackA,
			trackB,
			trackC );

		seq.addAndStartTrack( serTracks );
		serTracks.await( 100000 );
		Assert.assertEquals(
			"abc",
			sb1.toString() );
	}

	/**
	 * Test of connect method, of class SerialTrack.
	 */
	@Test
	public void testParallel() throws InterruptedException, TimeoutException
	{
		//BasicTrack.setDebug( true );
		Sequencer seq = Sequencer.get();
		seq.clear();
		seq.start();

		final ConcurrentStringBuilder sb2 = new ConcurrentStringBuilder();

		CharTrack trackA = new CharTrack( 'a', sb2 );
		CharTrack trackB = new CharTrack( 'b', sb2 );
		CharTrack trackC = new CharTrack( 'c', sb2 );

		BasicTrack parTracks = ParallelTrack.connect(
			trackA,
			trackB,
			trackC );

		seq.addAndStartTrack( parTracks );
		parTracks.await( 10000 );
		String result = sb2.toString();

		System.out.printf(
			"Result:%s\n",
			result );
		Assert.assertTrue( result.length() > 0 );

		Assert.assertTrue( result.indexOf( 'a' ) >= 0 );
		Assert.assertTrue( result.indexOf( 'b' ) >= 0 );
		Assert.assertTrue( result.indexOf( 'c' ) >= 0 );

	}

	/**
	 * Test of connect method, of class SerialTrack.
	 */
	@Test
	public void testSerialParallel() throws InterruptedException,
		TimeoutException
	{
		//BasicTrack.setDebug( true );
		Sequencer seq = Sequencer.get();
		seq.start();

		final ConcurrentStringBuilder sb3 = new ConcurrentStringBuilder();

		CharTrack trackA = new CharTrack( 'a', sb3 );
		CharTrack trackB = new CharTrack( 'b', sb3 );
		CharTrack trackC = new CharTrack( 'c', sb3 );

		Track parTracks = ParallelTrack.connect(
			trackA,
			trackB,
			trackC );

		BasicTrack allTracks = SerialTrack.connect(
			parTracks,
			new CharTrack( 'd', sb3 ) );

		seq.addAndStartTrack( allTracks );
		allTracks.await( 1000L );
		checkOrder( sb3.toString() );
		//seq.waitTillDone( 100000 );

	}

	private void checkOrder( String s )
	{
		System.out.println( s );
		Assert.assertTrue(
			"ad",
			s.indexOf( 'a' ) < s.indexOf( 'd' ) );
		Assert.assertTrue(
			"bd",
			s.indexOf( 'b' ) < s.indexOf( 'd' ) );
		Assert.assertTrue(
			"cd",
			s.indexOf( 'c' ) < s.indexOf( 'd' ) );
	}

}
