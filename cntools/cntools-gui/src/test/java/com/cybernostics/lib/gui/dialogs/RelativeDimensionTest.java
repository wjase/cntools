package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.gui.ScreenRelativeDimension;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author jasonw
 * 
 */
public class RelativeDimensionTest
{

	@Test
	public void testDimension()
	{
		ScreenRelativeDimension rd = new ScreenRelativeDimension( 0.5f, 0.7f );

		Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

		Assert.assertEquals(
			(int) ( screenSize.width * 0.5f ),
			rd.width );
		Assert.assertEquals(
			(int) ( screenSize.height * 0.7f ),
			rd.height );
	}
}
