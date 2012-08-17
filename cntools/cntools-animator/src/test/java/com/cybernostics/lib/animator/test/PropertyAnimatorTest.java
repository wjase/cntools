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
package com.cybernostics.lib.animator.test;

import com.cybernostics.lib.animator.paramaterised.LinearChange;
import com.cybernostics.lib.animator.track.BasicTimerTrack;
import com.cybernostics.lib.animator.track.ReflectionPropertyAnimatorTrack;
import com.cybernostics.lib.animator.track.Sequencer;
import com.cybernostics.lib.animator.track.Track;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.animator.track.ordering.TrackStartedListener;
import com.cybernostics.lib.concurrent.NanoTimeStamp;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author jasonw
 *
 */
public class PropertyAnimatorTest
{

	boolean endValueMatches = false;

	LinearChange linearChange = new LinearChange( 1.0, 0.0 );

	public PropertyAnimatorTest()
	{
	}

	/**
	 * Test method for
	 * {@link com.cybernostics.lib.animator.track.PropertyAnimatorTrack#update(com.cybernostics.lib.animator.TimeStamp)}
	 * .
	 */
	@Test
	public void testSimplePropertyUpdate()
	{
		endValueMatches = false;
		final ExampleObject obj = new ExampleObject();
		Sequencer animation = Sequencer.get();
		animation.start();
		final NanoTimeStamp tsTaskTime = new NanoTimeStamp();

		try
		{
			BasicTimerTrack theTrack = new ReflectionPropertyAnimatorTrack< Double >( "simpleProp",
				linearChange,
																					obj,
				"aSimpleProperty",
				200 );

			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					if (obj.aSimpleProperty <= 0.0f)
					{
						endValueMatches = obj.aSimpleProperty <= 0.0;
					}
					else
					{
						throw new RuntimeException( "aSimpleProperty: "
							+ obj.aSimpleProperty );
					}
				}

			} );

			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.await( 1000 );

		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}
		tsTaskTime.update();
		long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
		Assert.assertTrue(
			String.format(
				"Elapsed too great or small:%d",
				time ),
			( time > 160 ) && ( time < 400 ) );
		Assert.assertEquals(
			String.format(
				"Wrong end value:%f\n Log:\n%s",
				obj.aSimpleProperty,
				obj.getLog() ),
								true,
			endValueMatches );
	}

	boolean completed = false;

	@Test
	public void testBoundPropertyUpdate()
	{
		endValueMatches = false;
		final ExampleObject obj = new ExampleObject();
		Sequencer animation = Sequencer.get();
		animation.start();
		final NanoTimeStamp tsTaskTime = new NanoTimeStamp();

		try
		{
			BasicTimerTrack theTrack = new ReflectionPropertyAnimatorTrack< Double >( "boundProp",
				linearChange,
				obj,
																					"aBoundProperty",
				200 );

			theTrack.addTrackStartedListener( new TrackStartedListener()
			{

				@Override
				public void trackStarted( Track source )
				{
					tsTaskTime.start();
				}

			} );

			// System.out.print( "Started" );
			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					// System.out.print( "Finished" );
					// System.out.print( obj.getABoundProperty() );
					if (obj.getABoundProperty() <= 0.0)
					{
						endValueMatches = true;
					}
					else
					{
						throw new RuntimeException( "unexpected value:"
							+ obj.getABoundProperty() );
					}
					completed = true;
				}

			} );
			animation.addAndStartTrack( theTrack );
			theTrack.await( 2000 );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		tsTaskTime.update();
		long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
		Assert.assertTrue(
			String.format(
				"Elapsed too great or small:%d",
				time ),
			( time > 160 ) && ( time < 400 ) );
		Assert.assertEquals(
				String.format(
					"Wrong end value:%f\n Log:\n%s",
					obj.getABoundProperty(),
					obj.getLog() ),
			true,
				endValueMatches );
	}

	/**
	 * Test method for
	 * {@link com.cybernostics.lib.animator.track.PropertyAnimatorTrack#update(com.cybernostics.lib.animator.TimeStamp)}
	 * .
	 */
	@Test
	public void testInheritedSimplePropertyUpdate()
	{
		endValueMatches = false;
		final DerivedClass obj = new DerivedClass();
		Sequencer animation = Sequencer.get();
		animation.start();

		final NanoTimeStamp tsTaskTime = new NanoTimeStamp();
		try
		{

			BasicTimerTrack theTrack = new ReflectionPropertyAnimatorTrack< Double >( "simpleProp",
				linearChange,
																					obj,
				"aSimpleProperty",
				200 );
			// System.out.print( "Started" );
			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					if (obj.aSimpleProperty <= 0.0)
					{
						endValueMatches = true;
					}
					else
					{
						throw new RuntimeException( String.format(
								"InheritedSimplePropertyUpdate Finished. Unexpected value:%f",
							obj.aSimpleProperty ) );
					}
				}

			} );

			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.await( 1000 );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		tsTaskTime.update();
		long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
		Assert.assertTrue(
			String.format(
				"Elapsed too great or small:%d",
				time ),
			( time > 160 ) && ( time < 400 ) );

		Assert.assertEquals(
			String.format(
				"End value wrong:%f",
				obj.aSimpleProperty ),
			true,
			endValueMatches );
	}

	/**
	 * Test method for
	 * {@link com.cybernostics.lib.animator.track.PropertyAnimatorTrack#update(com.cybernostics.lib.animator.TimeStamp)}
	 * .
	 */
	@Test
	public void testInheritedBoundPropertyUpdate()
	{
		endValueMatches = false;
		final DerivedClass obj = new DerivedClass();
		Sequencer animation = Sequencer.get();
		animation.start();
		final NanoTimeStamp tsTaskTime = new NanoTimeStamp();

		try
		{
			BasicTimerTrack theTrack = new ReflectionPropertyAnimatorTrack< Double >( "boundProp",
				linearChange,
				obj,
																					"aBoundProperty",
				200 );
			// System.out.print( "Started" );
			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					double value = obj.getABoundProperty();
					System.out.print( "InheritedBoundPropertyUpdate Finished" );
					System.out.print( value );
					endValueMatches = value == 0.0;
					if (endValueMatches == false)
					{
						System.out.print( "Ooops" );
					}
				}

			} );

			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.await( 1000 );
		}
		catch (Exception e)
		{
			UnhandledExceptionManager.handleException( e );
		}

		tsTaskTime.update();
		long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
		Assert.assertTrue(
			String.format(
				"Elapsed too great or small:%d",
				time ),
			( time > 160 ) && ( time < 400 ) );

		double value = obj.getABoundProperty();
		System.out.println( value );

		Assert.assertEquals(
			true,
			endValueMatches );
	}

}
