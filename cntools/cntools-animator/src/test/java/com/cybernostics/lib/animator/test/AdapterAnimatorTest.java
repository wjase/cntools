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
import com.cybernostics.lib.animator.track.*;
import com.cybernostics.lib.animator.track.ordering.TrackEndedListener;
import com.cybernostics.lib.concurrent.NanoTimeStamp;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.Test;

/**
 * @author jasonw
 *
 */
public class AdapterAnimatorTest
{

	public static final int MIN_TIME = 430;

	boolean endValueMatches = false;

	boolean trackendedCalled = false;

	LinearChange linearChange = new LinearChange( 1.0f, 0.0f );

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
			BasicTrack theTrack = new AdapterAnimatorTrack< Double >( "simpleProp",
				500,
				linearChange,
					new AnimatedProperty< Double >()
					{

						@Override
						public void update( Double value )
						{
							obj.aSimpleProperty = value.floatValue();
						}

					} );

			theTrack.addTrackEndedListener( new TrackEndedListener()
				{

					@Override
					public void trackEnded( Track source )
					{
						trackendedCalled = true;
						if (obj.aSimpleProperty <= 0.0f)
						{
							endValueMatches = obj.aSimpleProperty <= 0.0f;
						}
						else
						{
							System.out.println( "aSimpleProperty: "
								+ obj.aSimpleProperty );

						}
					}

				} );

			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.await( 2000 );
		}
		catch (Exception e)
		{
			Assert.fail( e.getLocalizedMessage() );
		}

		tsTaskTime.update();
		long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
		Assert.assertTrue(
			String.format(
				"Elapsed too great or small:%d",
				time ),
			( time > MIN_TIME )
				&& ( time < 600 ) );

		//		try
		//		{
		//			Thread.sleep( 1000 );
		//		}
		//		catch ( InterruptedException e )
		//		{
		//			UnhandledExceptionManager.handleException( e );
		//		}
		Assert.assertTrue( trackendedCalled );
		Assert.assertEquals(
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
		final StringBuilder sbOut = new StringBuilder();

		try
		{
			BasicTrack theTrack = new AdapterAnimatorTrack< Double >(
				"simpleProp", 500, linearChange,
				new AnimatedProperty< Double >()
				{

					@Override
					public void update( Double value )
					{
						sbOut.append( String.format(
							"update:%f\n",
							value ) );
						obj.aSimpleProperty = value.floatValue();
					}

				} )
			{

				@Override
				public void stop( boolean fireEvents )
				{
					tsTaskTime.update();
					long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
					if (time < 485)
					{
						Assert.assertTrue(
							"Time too short:" + time,
							time >= 600 );
					}
					super.stop( fireEvents );
				}

			};

			// System.out.print( "Started" );
			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					trackendedCalled = true;
					if (obj.aSimpleProperty <= 0.0f)
					{
						endValueMatches = true;
					}
					else
					{
						System.out.print( "InheritedSimplePropertyUpdate Finished. Unexpected value" );
						System.out.print( obj.aSimpleProperty );
					}
				}

			} );
			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.await( 700 );
			tsTaskTime.update();
			//			long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
			//			Assert.assertTrue( String.format( "Elapsed too great or small:%d\nLog:\n%s", time, sbOut.toString() ),
			//				(time > MIN_TIME) && (time < 600) );
		}
		catch (InterruptedException ie)
		{
			Logger.getLogger(
				"Test timed out" + AdapterAnimatorTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ie );
			Assert.fail( "Task timed out" );

		}
		catch (TimeoutException ex)
		{
			Logger.getLogger(
				"Test timed out" + AdapterAnimatorTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
			Assert.fail( "Task timed out" );
		}

		Assert.assertTrue( trackendedCalled );
		Assert.assertEquals(
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

		final StringBuilder sbOut = new StringBuilder();

		try
		{
			BasicTrack theTrack = new AdapterAnimatorTrack< Double >( "boundProp",
				500,
				linearChange,
				new AnimatedProperty< Double >()
				{

					@Override
					public void update( Double value )
					{
						obj.setABoundProperty( value.floatValue() );
					}

				} );

			// System.out.print( "Started" );
			theTrack.addTrackEndedListener( new TrackEndedListener()
			{

				@Override
				public void trackEnded( Track source )
				{
					trackendedCalled = true;
					double value = obj.getABoundProperty();
					sbOut.append( String.format(
						"InheritedBoundPropertyUpdate Finished:%f",
						value ) );
					endValueMatches = value <= 0.0f;
					Assert.assertTrue( value <= 0.0f );
				}

			} );

			tsTaskTime.start();
			animation.addAndStartTrack( theTrack );
			theTrack.waitForAllListeners( 1000 );
			tsTaskTime.update();
			long time = NanoTimeStamp.toMilliSeconds( tsTaskTime.getElapsed() );
			Assert.assertTrue(
				String.format(
					"Elapsed too great or small:%d\nLog:\n%s",
					time,
					sbOut.toString() ),
				( time > MIN_TIME ) && ( time < 600 ) );

		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				"Test timed out" + AdapterAnimatorTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
			Assert.fail();
		}

		System.out.println( sbOut.toString() );
		double value = obj.getABoundProperty();
		System.out.print( "Outsideloop:" + value );
		Assert.assertTrue( trackendedCalled );
	}

}
