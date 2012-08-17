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

import java.util.Vector;
import java.util.concurrent.TimeoutException;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class BasicTimerTrackTest
{

	class TestTrack extends BasicTimerTrack
	{

		Vector< Float > samples = new Vector( 500, 500 );

		TestTrack( String name, long duration )
		{
			super( name, duration );
		}

		@Override
		public void update( float t )
		{
			samples.add( t );
		}

		public Vector< Float > getSamples()
		{
			return samples;
		}

	}

	public BasicTimerTrackTest()
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

	//    /**
	//     * Test of getTimeCode method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testGetTimeCode()
	//    {
	//        System.out.println( "getTimeCode" );
	//        BasicTimerTrack instance = null;
	//        TimeStamp expResult = null;
	//        TimeStamp result = instance.getTimeCode();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of setDuration method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testSetDuration()
	//    {
	//        System.out.println( "setDuration" );
	//        long duration = 0L;
	//        BasicTimerTrack instance = null;
	//        instance.setDuration( duration );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of setPeriodic method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testSetPeriodic()
	//    {
	//        System.out.println( "setPeriodic" );
	//        boolean flag = false;
	//        BasicTimerTrack instance = null;
	//        BasicTimerTrack expResult = null;
	//        BasicTimerTrack result = instance.setPeriodic( flag );
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of setHeadstart method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testSetHeadstart()
	//    {
	//        System.out.println( "setHeadstart" );
	//        long timeInMillis = 0L;
	//        BasicTimerTrack instance = null;
	//        instance.setHeadstart( timeInMillis );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of isPeriodic method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testIsPeriodic()
	//    {
	//        System.out.println( "isPeriodic" );
	//        BasicTimerTrack instance = null;
	//        boolean expResult = false;
	//        boolean result = instance.isPeriodic();
	//        assertEquals( expResult, result );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of update method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testUpdate_TimeStamp()
	//    {
	//        System.out.println( "update" );
	//        TimeStamp timeCodeFromSequencer = null;
	//        BasicTimerTrack instance = null;
	//        instance.update( timeCodeFromSequencer );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of setElapsedOffset method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testSetElapsedOffset()
	//    {
	//        System.out.println( "setElapsedOffset" );
	//        long offset = 0L;
	//        BasicTimerTrack instance = null;
	//        instance.setElapsedOffset( offset );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of doUpdate method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testDoUpdate()
	//    {
	//        System.out.println( "doUpdate" );
	//        float t = 0.0F;
	//        BasicTimerTrack instance = null;
	//        instance.doUpdate( t );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	/**
	 * Test of update method, of class BasicTimerTrack.
	 */
	@Test
	public void testUpdate_float() throws InterruptedException,
		TimeoutException
	{

		TestTrack instance = new TestTrack( "barry", 200 );
		instance.setHeadstart( 100 );
		Sequencer.get()
			.start();
		Sequencer.get()
			.addAndStartTrack(
				instance );
		instance.await( 500 );
		Vector< Float > result = instance.getSamples();
		Assert.assertTrue( result.size() > 0 );
		Float firstSample = result.get( 0 );
		Assert.assertTrue( firstSample >= 0.5 && firstSample <= 1.0 );

		for (Float eachOne : result)
		{
			System.out.printf(
				"%f\n",
				eachOne );
		}
	}
	//
	//    /**
	//     * Test of start method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testStart()
	//    {
	//        System.out.println( "start" );
	//        BasicTimerTrack instance = null;
	//        instance.start();
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of stop method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testStop()
	//    {
	//        System.out.println( "stop" );
	//        boolean fireUpdates = false;
	//        BasicTimerTrack instance = null;
	//        instance.stop( fireUpdates );
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of reset method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testReset()
	//    {
	//        System.out.println( "reset" );
	//        BasicTimerTrack instance = null;
	//        instance.reset();
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of pause method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testPause()
	//    {
	//        System.out.println( "pause" );
	//        BasicTimerTrack instance = null;
	//        instance.pause();
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    /**
	//     * Test of resume method, of class BasicTimerTrack.
	//     */
	//    @Test
	//    public void testResume()
	//    {
	//        System.out.println( "resume" );
	//        BasicTimerTrack instance = null;
	//        instance.resume();
	//        // TODO review the generated test code and remove the default call to fail.
	//        fail( "The test case is a prototype." );
	//    }
	//
	//    public class BasicTimerTrackImpl extends BasicTimerTrack
	//    {
	//
	//        public BasicTimerTrackImpl()
	//        {
	//            super( "", 0L );
	//        }
	//
	//        public void update( float t )
	//        {
	//        }
	//
	//    }
}
