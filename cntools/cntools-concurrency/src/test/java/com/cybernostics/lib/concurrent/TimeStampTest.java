package com.cybernostics.lib.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class TimeStampTest
{

	public TimeStampTest()
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

	/**
	 * Test of getHeadstart method, of class TimeStamp.
	 */
	@Test
	public void testGetHeadstart()
	{

		TimeStamp instance = new TimeStamp();
		instance.setHeadstart( 5000 );
		instance.start();
		long elapsed = instance.getElapsed();

		Assert.assertTrue( elapsed >= 5000 );

	}

	public void testGetHeadstart2()
	{

		TimeStamp instance = new TimeStamp();
		instance.setHeadstart( 5000 );
		instance.start();
		long elapsed = instance.getElapsed();
		try
		{
			Thread.sleep( 1000 );
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(
				TimeStampTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
		Assert.assertTrue( elapsed >= 6000 );

	}

}
