/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.svg.editor;

import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.svg.ReferencedURLRewriter;
import com.cybernostics.lib.svg.SVGWriter;
import com.kitfox.svg.elements.SVGElement;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jasonw
 */
public class SVGDrawingTest
{

	public SVGDrawingTest()
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
	public void createEllipseTest()
	{
		SVGDrawing dwg = new SVGDrawing( "DrawTest" );

		dwg.setPaperColor( Color.blue );
		//dwg.setCurrentStrokeWidth( 5 );
		dwg.setFillColour( Color.red );
		SVGElement el = dwg.createEllipse(
			new Ellipse2D.Double( 0.2, 0.2, 0.4, 0.4 ),
			true,
			true );
		dwg.addElement(
			el,
			null );
		IStringAttribute x = el.getPresAbsolute( "cx" );
		Assert.assertNotNull( x );
		Assert.assertTrue( x instanceof CSSQuantityAttribute );

		CSSQuantityAttribute fx = (CSSQuantityAttribute) x;
		Assert.assertEquals(
			0.4,
			fx.getFloatValue(),
			0.0001 );

	}

	public static void main( String[] args )
	{
		GUIEventThread.run( new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					SVGDrawing dwg = new SVGDrawing( "DrawTest" );

					dwg.setPaperColor( Color.blue );
					//dwg.setCurrentStrokeWidth( 5 );
					dwg.setFillColour( Color.red );
					dwg.addElement(
						dwg.createEllipse(
							new Ellipse2D.Double( 0.2, 0.2, 0.4, 0.4 ),
							true,
							true ),
						null );

					dwg.setFillColour( Color.yellow );
					GeneralPath s = new GeneralPath();
					s.moveTo(
						0.2,
						0.2 );
					s.lineTo(
						0.4,
						0.4 );

					SVGElement el = dwg.createShape(
						s,
						false,
						true );
					dwg.addElement(
						el,
						null );

					dwg.addPathPoint(
						el,
						new Point2D.Double( 0.0, 1.0 ) );

					SVGElement im = dwg.createBitmapRef(
						new URL( "file:///C:\\temp\\spanner.png" ),
															new Rectangle2D.Double( 0,
																0,
																0.2,
																0.2 ) );
					dwg.addElement(
						im,
						null );

					JFrame jf = new JFrame();
					jf.getContentPane()
						.setLayout(
							new GridLayout() );
					jf.getContentPane()
						.add(
							new ImagePanel( dwg ) );

					jf.setVisible( true );
					jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

					PrintWriter pw = new PrintWriter( System.out );
					SVGWriter.writeSVGFIle(
						dwg.getDiagram(),
						pw,
						new ReferencedURLRewriter() );
					pw.flush();
					//System.exit( 0);
				}
				catch (MalformedURLException ex)
				{
					Logger.getLogger(
						SVGDrawing.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}

			}

		} );

	}

}
