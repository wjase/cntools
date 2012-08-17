package com.cybernostics.lib.io.file;

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
import com.cybernostics.lib.io.FileTools;
import java.io.File;
import java.net.URL;
import com.cybernostics.lib.io.platform.HomeFolder;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class RecursiveURLProcessorTest
{

	public RecursiveURLProcessorTest()
	{
	}

	static File testFolder = null;

	static File jarFile = null;

	@BeforeClass
	public static void setUpClass() throws Exception
	{
		// create a test file hierarchy to iterate
		testFolder = HomeFolder.createChild( "testFolder" );
		new File( testFolder, "dir1" ).mkdirs();
		new File( testFolder, "dir2" ).mkdirs();
		File txt1 = new File( testFolder, "dir1/test.txt" );
		txt1.createNewFile();
		File txt2 = new File( testFolder, "dir1/test1.txt" );
		txt2.createNewFile();
		File txt3 = new File( testFolder, "dir2/test1.txt" );
		txt3.createNewFile();

		// create a test jar file to iterate
		jarFile = new File( testFolder, "test.jar" );
		FileOutputStream stream = new FileOutputStream( jarFile );
		JarOutputStream out = new JarOutputStream( stream, new Manifest() );
		// Add archive entry
		JarEntry jarAdd = new JarEntry( "jardir/file1.txt" );
		out.putNextEntry( jarAdd );
		JarEntry jarAdd2 = new JarEntry( "jardir/file2.txt" );
		out.putNextEntry( jarAdd2 );
		out.write( "This is some test data".getBytes() );
		out.close();
		stream.close();

	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
		FileTools.deleteDir( testFolder );
	}

	/**
	 * Test of relativise method, of class RelativePath.
	 */
	@Test
	public void testFileVisitor() throws Exception
	{
		RecursiveURLProcessor.process(
			testFolder.toURL(),
			new URLVisitor()
		{

			@Override
			public void visit( URL eachURL )
			{
				System.out.println( eachURL );

			}

		} );
	}

	@Test
	public void testJarFileVisitor() throws Exception
	{
		RecursiveURLProcessor.process(
			new URL( "jar:" + jarFile.toURL()
				.toString() + "!/" ),
			new URLVisitor()
		{

			@Override
			public void visit( URL eachURL )
			{
				System.out.println( eachURL );

			}

		} );
	}

}
