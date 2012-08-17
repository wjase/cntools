/*
 * 11/19/2004 : 1.0 moved to LGPL. 01/01/2004 : Initial version by E.B javalayer@javazoom.net
 * ----------------------------------------------------------------------- This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU Library General
 * Public License as published by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Library General Public License for more details.
 * 
 * You should have received a copy of the GNU Library General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139,
 * USA.----------------------------------------------------------------------
 */

package javazoom.jl.player;

import java.io.InputStream;
import java.util.Properties;
import javazoom.jl.JLTestResources;
import javazoom.jl.decoder.JavaLayerException;
import junit.framework.TestCase;

/**
 * Simple player unit test. It takes around 3-6% of CPU and 10MB RAM under Win2K/PIII/1GHz/JDK1.5.0 It takes around
 * 10-12% of CPU and 10MB RAM under Win2K/PIII/1GHz/JDK1.4.1 It takes around 08-10% of CPU and 10MB RAM under
 * Win2K/PIII/1GHz/JDK1.3.1
 * 
 * @since 0.4
 */
public class jlpTest extends TestCase
{
	private String filename = null;

	private final Properties props = null;

	/**
	 * Constructor for jlpTest.
	 * 
	 * @param arg0
	 */
	public jlpTest( String arg0 )
	{
		super( arg0 );
	}

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		String basefile = "";
		String name = "Test.mp3";
		filename = basefile + name;
		// out = System.out;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testPlay()
	{
		String[] args = new String[ 1 ];
		args[ 0 ] = filename;
		jlp player = jlp.createInstance( args );
		try
		{
			InputStream is = JLTestResources.class.getResourceAsStream( "Test.mp3" );
			player.play( is );
			assertTrue(
				"Play",
				true );
		}
		catch (JavaLayerException e)
		{
			e.printStackTrace();
			assertTrue(
				"JavaLayerException : " + e.getMessage(),
				false );
		}
	}
}
