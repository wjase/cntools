
package com.cybernostics.examples.lib.gui.control;

import com.cybernostics.lib.gui.control.NextLastListBox;
import javax.swing.JFrame;

/**
 * Implements a large formatted sing-item list for small lists with next/last
 * buttons to navigate
 * 
 * @author jasonw
 * 
 */

public class NextLastListBoxExample
{

	public static void main( String[] args )
	{
		JFrame test = new JFrame( "ListTest" );

		test.setSize( 400, 400 );
		NextLastListBox nlb = new NextLastListBox();
		nlb.addItems( "item one", "This is item 2", "Item 3 is the next one", "Followed by item four",
			"Rounding things off with item five" );

		test.getContentPane().add( nlb );
		test.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		test.setVisible( true );

	}

}
