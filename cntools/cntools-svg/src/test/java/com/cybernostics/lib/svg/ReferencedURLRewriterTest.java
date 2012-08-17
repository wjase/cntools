package com.cybernostics.lib.svg;

import org.junit.*;

/**
 *
 * @author jasonw
 */
public class ReferencedURLRewriterTest
{

	public ReferencedURLRewriterTest()
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

	@Before
	public void setUp()
	{
	}

	@After
	public void tearDown()
	{
	}

	/**
	 * Test of transformURL method, of class ReferencedURLRewriter.
	 */
	@Test
	public void testTransformURL()
	{
		String[] inputs =
		{ "url(file:///c:\\path\\to\\file\\file.ext)",
			"url(resloader://some.class.Name/file/file.ext)",
			"url(http://somehost.com/path/to/file/file.ext)", };

		String[] expected =
		{ "path/to/file/file.ext",
			"url(resloader://some.class.Name/file/file.ext)",
			"path/to/file/file.ext", };

		//System.out.println( "transformURL" );

		for (int index = 0; index < inputs.length; ++index)
		{
			ReferencedURLRewriter instance = new ReferencedURLRewriter();
			String result = instance.transformURL( inputs[ index ] );
			Assert.assertEquals(
				expected[ index ],
				result );

		}
	}
}
