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

package com.cybernostics.lib.animator.track.sound;

import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.animator.track.ordering.TrackStartedListener;
import com.cybernostics.lib.animator.track.ordering.TrackUpdatedListener;
import com.cybernostics.lib.concurrent.TimeStamp;
import com.cybernostics.lib.media.SoundEffect;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.net.URL;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class SoundEffectTrackTest
{

	public SoundEffectTrackTest()
	{
	}

	boolean called = false;

	static Finder res()
	{
		return ResourceFinder.get( SoundEffectTrackTest.class );
	}

	/**
	 * Test of create method, of class SoundEffectTrack.
	 */
	@Test
	public void testFullPlay() throws TimeoutException, InterruptedException
	{
		final TimeStamp stTest = new TimeStamp();
		Sequencer seq = Sequencer.get();
		seq.start();

		URL toPlay = res().getResource(
			"twosecondtest.mp3" );
		SoundEffectTrack set = new SoundEffectTrack( new SoundEffect( toPlay ) );
		set.addTrackStartedListener( new TrackStartedListener()
		{

			@Override
			public void trackStarted( Track source )
			{
				stTest.start();
			}

		} );

		set.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				stTest.update();
				called = true;
				Assert.assertTrue( stTest.getElapsed() > 1800 );
			}

		} );

		seq.addAndStartTrack( set );
		set.await( 5000 );
		Assert.assertTrue( called );

	}

	/**
	 * Test of create method, of class SoundEffectTrack.
	 */
	@Test
	public void testPartPlay() throws TimeoutException
	{
		final TimeStamp stTestFull = new TimeStamp();
		called = false;

		final TimeStamp stTest = new TimeStamp();
		final Sequencer seq = Sequencer.get();
		seq.start();

		//		SoundEffectTrack set = new SoundEffectTrack( new SoundEffect( finder
		//			.getResource( "sound/twosecondtest.mp3" ) ) );
		SoundEffectTrack set = new SoundEffectTrack( new SoundEffect( res()
			.getResource(
				"twosecondtest.mp3" ) ) );
		set.addTrackStartedListener( new TrackStartedListener()
		{

			@Override
			public void trackStarted( Track source )
			{
				stTest.start();
				stTestFull.start();
			}

		} );
		set.addTrackEndedListener( new TrackEndedListener()
		{

			@Override
			public void trackEnded( Track source )
			{
				stTest.update();
				called = true;
				Assert.assertTrue( stTest.getElapsed() < 700 );
			}

		} );

		set.addTrackUpdatedListener( new TrackUpdatedListener()
		{

			@Override
			public void trackUpdated( TimeStamp now, Track source )
			{
				if (now.getElapsed() > 500)
				{
					source.stop( true );
				}
			}

		} );
		seq.addAndStartTrack( set );
		try
		{
			set.await( 2000 );
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				SoundEffectTrackTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		stTestFull.update();
		Assert.assertTrue( stTest.getElapsed() < 600 );
		//Assert.assertTrue( called );

	}

}
