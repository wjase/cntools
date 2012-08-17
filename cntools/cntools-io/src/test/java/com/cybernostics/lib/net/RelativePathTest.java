package com.cybernostics.lib.net;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.URISyntaxException;
import com.cybernostics.lib.collections.IterableArray;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jasonw
 */
public class RelativePathTest
{

	public RelativePathTest()
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
	 * Test of relativise method, of class RelativePath.
	 */
	@Test
	public void testRelativise_String_String() throws Exception
	{
		String[] inputRoots =
		{ "/var/data/aFile.html", "/var/data", "/var/data/" };

		String path = "/var/data/stuff/xyz.dat";
		String expected = "stuff/xyz.dat";
		for (String eachRoot : IterableArray.get( inputRoots ))
		{
			String rel = RelativePath.get(
				path,
				eachRoot );
			System.out.print( String.format(
				"root:%s\npath:%s\nrelative:%s\n\n",
				eachRoot,
				path,
				rel ) );
			assertEquals(
				expected,
				rel );
		}

	}

	/**
	 * Test of relativise method, of class RelativePath.
	 */
	@Test
	public void testRelativise_Jar_String_String() throws Exception
	{
		String[] inputRoots =
		{
			"jar:file:/C:/Documents%20and%20Settings/jasonw/.m2/com/cybernostics/lib/joeymail-corethemes/1.0-SNAPSHOT/joeymail-corethemes-1.0-SNAPSHOT.jar!/com/cybernostics/app/joeymail/themes/space/body/",
			"jar:file:/C:/Documents%20and%20Settings/jasonw/.m2/com/cybernostics/lib/joeymail-corethemes/1.0-SNAPSHOT/joeymail-corethemes-1.0-SNAPSHOT.jar!/com/cybernostics/app/joeymail/themes/space/body",
			"jar:file:/C:/Documents%20and%20Settings/jasonw/.m2/com/cybernostics/lib/joeymail-corethemes/1.0-SNAPSHOT/joeymail-corethemes-1.0-SNAPSHOT.jar!/com/cybernostics/app/joeymail/themes/space/body/file.html" };

		String path = "jar:file:/C:/Documents%20and%20Settings/jasonw/.m2/com/cybernostics/lib/joeymail-corethemes/1.0-SNAPSHOT/joeymail-corethemes-1.0-SNAPSHOT.jar!/com/cybernostics/app/joeymail/themes/space/body/images/bg.jpg";
		String expected = "images/bg.jpg";
		for (String eachRoot : IterableArray.get( inputRoots ))
		{
			String rel = RelativePath.get(
				path,
				eachRoot );
			System.out.print( String.format(
				"root:%s\npath:%s\nrelative:%s\n\n",
				eachRoot,
				path,
				rel ) );
			assertEquals(
				expected,
				rel );
		}

	}

}
