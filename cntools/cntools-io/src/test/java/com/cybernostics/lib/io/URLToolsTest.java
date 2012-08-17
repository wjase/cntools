/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.io;

import java.net.MalformedURLException;
import java.net.URL;
import junit.framework.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class URLToolsTest
{

	public URLToolsTest()
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

	@Test
	public void testSomeMethod() throws MalformedURLException
	{
		URL child1 = new URL( "file:///c:/somepath/next/afile.svg" );
		URL parent = URLTools.getParent( child1 );
		Assert.assertEquals(
			"check parent",
			parent.toString(),
			"file:/c:/somepath/next/" );
		parent = URLTools.getParent( parent );
		Assert.assertEquals(
			"check grandparent",
			"file:/c:/somepath/",
			parent.toString() );
	}

}
