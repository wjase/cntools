package com.cybernostics.lib.template;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class TemplateProcessorTest
{

	public TemplateProcessorTest()
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
	 * Test of process method, of class TemplateProcessor.
	 */
	@Test
	public void testProcess()
	{
		String endl = System.getProperty( "line.separator" );
		String textInput = "this is a text of the |%item_1%| text." + endl
			+ "which is |%item_1%| contained on [%item_2%] several lines"
			+ endl
			+ "That are contained in |%item_3%| a file";
		System.out.println( "process" );
		Map< String, String > replacements = new HashMap< String, String >();
		replacements.put(
			"item_1",
			"boo" );
		replacements.put(
			"item_2",
			"moo" );

		String[] start =
		{ "|%", "[%" };
		String[] end =
		{ "%|", "%]" };
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		InputStream is = new ByteArrayInputStream( textInput.getBytes() );

		TemplateProcessor.process(
			is,
			bos,
			start,
			end,
			replacements );
		String outText = bos.toString();

		System.out.println( "output<" + outText + ">" );

		String textOutput = "this is a text of the boo text." + endl
			+ "which is boo contained on moo several lines"
			+ endl + "That are contained in |%item_3%| a file";

		System.out.println( "expected<" + textOutput + ">" );

		assertEquals(
			textOutput,
			outText );

	}

}
