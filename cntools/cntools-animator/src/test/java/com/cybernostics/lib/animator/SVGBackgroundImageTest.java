/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.animator;

import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.media.icon.IconLoadClient;
import com.cybernostics.lib.media.icon.SVGIconLoader;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import com.cybernostics.lib.svg.SVGUtil;
import com.cybernostics.lib.test.JFrameTest;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author jasonw
 */
public class SVGBackgroundImageTest
{
	public static void main( String[] args )
	{
		try
		{
			JFrameTest jf = JFrameTest.create( "bgimage" );

			final JPanel panl = new JPanel();
			final ShapedPanel sp = new ShapedPanel();
			URL bgURL = new URL( "file:///C:/data/Code/Java/projects/cntools-resources/src/main/resources/com/cybernostics/lib/gui/control/icons/slider.svg" );
			// create your component
			SVGIconLoader.load(
				bgURL,
				new IconLoadClient()
			{

				@Override
				public void iconLoaded( ScalableSVGIcon svgsi )
				{
					sp.setBackgroundPainter( new SVGBackgroundImage( svgsi ) );
					sp.add(
						panl,
						SVGUtil.getSubItemRectangle(
							"slider",
							svgsi.getDiagram() ) );
					sp.revalidate();
					sp.repaint();
				}

			} );

			jf.getContentPane()
				.setLayout(
					new GridLayout() );
			jf.getContentPane()
				.add(
					sp );
			jf.go();
		}
		catch (MalformedURLException ex)
		{
			Logger.getLogger(
				SVGBackgroundImageTest.class.getName() )
				.log(
					Level.SEVERE,
					null,
					ex );
		}
	}

}
