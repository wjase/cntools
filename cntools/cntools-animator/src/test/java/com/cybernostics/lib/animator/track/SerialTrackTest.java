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

import com.cybernostics.lib.animator.track.ordering.LoopTrack;
import com.cybernostics.lib.animator.track.ordering.SerialTrack;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.concurrent.TimeStamp;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.TimeoutException;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class SerialTrackTest
{

	public SerialTrackTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	class CharTrack extends BasicTrack
	{

		StringBuilder sb;

		char toAdd;

		public CharTrack( char c, StringBuilder sb )
		{
			super( "Track " + c );
			this.sb = sb;
			this.toAdd = c;
		}

		@Override
		public void update( TimeStamp timeCode )
		{
			if (isRunning())
			{
				sb.append( toAdd );
			}
			stop( true );

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
	public void testConnect() throws InterruptedException, TimeoutException
	{
		Sequencer seq = Sequencer.get();

		final StringBuilder sb = new StringBuilder();

		BasicTrack t = SerialTrack
			.connect(
				new CharTrack( 'a', sb ),
				new CharTrack( 'b', sb ),
				new CharTrack( 'c', sb ) );

		t.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				Assert.assertEquals(
					"abc",
					sb.toString() );
			}

		} );
		seq.addAndStartTrack( t );
		t.await( 10000 );
		Assert.assertEquals(
			"abc",
			sb.toString() );
	}

	/**
	 * Test of connect method, of class SerialTrack.
	 */
	@Test
	public void testLoop() throws TimeoutException, InterruptedException
	{
		Sequencer seq = Sequencer.get();

		seq.addPropertyChangeListener( new PropertyChangeListener()
		{

			@Override
			public void propertyChange( PropertyChangeEvent evt )
			{
				System.out.printf(
					"%s:%s\n",
					evt.getPropertyName(),
					evt.getNewValue()
						.toString() );
			}
		} );

		final StringBuilder sb = new StringBuilder();

		BasicTrack t = SerialTrack
			.connect(
				new CharTrack( 'a', sb ),
				new CharTrack( 'b', sb ),
				new CharTrack( 'c', sb ) );

		t = LoopTrack.create(
			5,
			t ); // loop it

		t.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				Assert.assertEquals(
					"abcabcabcabcabc",
					sb.toString() );
			}

		} );
		seq.addAndStartTrack( t );

		t.await( 10000 );
		Assert.assertEquals(
			"abcabcabcabcabc",
			sb.toString() );

	}

}
