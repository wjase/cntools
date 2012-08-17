package com.cybernostics.lib.gui;

/**
 * From this tutorial:
 * 
 * http://www.apl.jhu.edu/~hall/java/Swing-Tutorial/Swing-Tutorial-Printing...
 * 
 */

import com.cybernostics.lib.gui.graphics.StateSaver;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

/**
 * Static class to provide print support for Java Components
 * 
 * @author jasonw
 * 
 */
public class PrintUtilities implements Printable
{
	private Component componentToBePrinted;

	public static void printComponent( Component c )
	{
		new PrintUtilities( c ).print();
	}

	public PrintUtilities( Component componentToBePrinted )
	{
		this.componentToBePrinted = componentToBePrinted;
	}

	public void print()
	{
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable( this );
		if (printJob.printDialog())
		{
			try
			{
				printJob.print();
			}
			catch (PrinterException pe)
			{
				System.out.println( "Error printing: " + pe );
			}
		}
	}

	@Override
	public int print( Graphics g, PageFormat pf, int pageIndex )
	{
		int response = NO_SUCH_PAGE;

		StateSaver g2 = StateSaver.wrap( g );

		// double screenResolution =
		// Toolkit.getDefaultToolkit().getScreenResolution();
		// double printerResolution = 72; // nominal image scale

		// double dpiScale = printerResolution / screenResolution;

		try
		{
			// System.out.printf( "Page %d\n", pageIndex );
			// for faster printing, turn off double buffering
			disableDoubleBuffering( componentToBePrinted );

			Dimension d = componentToBePrinted.getSize(); // get size of
			// document
			double panelWidth = d.width; // width in pixels scaled to 400dpi
			double panelHeight = d.height; // height in pixels scaled to 400 dpi

			// System.out.printf( "panel w,h = %g,%g\n", panelWidth, panelHeight
			// );

			double pageHeight = pf.getImageableHeight(); // height of printer
			// page
			double pageWidth = pf.getImageableWidth(); // width of printer page

			// System.out.printf( "page w,h = %g,%g\n", pageWidth, pageHeight );

			double scale = pageWidth / panelWidth;

			// System.out.printf("Scale: %g\n",scale);
			int totalNumPages = (int) Math.ceil( scale * panelHeight
				/ pageHeight );
			// System.out.printf("Total pages: %d\n",totalNumPages);

			// make sure not print empty pages
			if (pageIndex >= totalNumPages)
			{
				response = NO_SUCH_PAGE;
			}
			else
			{

				// shift Graphic to line up with beginning of print-imageable
				// region
				g2.translate(
					pf.getImageableX(),
					pf.getImageableY() );

				// shift Graphic to line up with beginning of next page to print
				g2.translate(
					0f,
					-pageIndex * pageHeight );

				// scale the page so the width fits...
				g2.scale(
					scale,
					scale );

				componentToBePrinted.print( g2 ); // repaint the page for
				// printing

				enableDoubleBuffering( componentToBePrinted );
				response = Printable.PAGE_EXISTS;
			}

		}
		finally
		{
			g2.restore();
		}

		return response;
	}

	public static void disableDoubleBuffering( Component c )
	{
		RepaintManager currentManager = RepaintManager.currentManager( c );
		currentManager.setDoubleBufferingEnabled( false );
	}

	public static void enableDoubleBuffering( Component c )
	{
		RepaintManager currentManager = RepaintManager.currentManager( c );
		currentManager.setDoubleBufferingEnabled( true );
	}
}
