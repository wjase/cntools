/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.laf.wood;

import java.awt.FlowLayout;
import javax.swing.*;
import junit.framework.TestCase;

/**
 *
 * @author jasonw
 */
public class WoodLookAndFeelTest
{

	private WoodLookAndFeelTest()
	{

	}

	public static void main( String[] args )
	{
		WoodLookAndFeel.setUI();
		JFrame jf = new JFrame( "Wood Example" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		// create your component
		JPanel jp = new JPanel();
		jp.add( new JButton( "Button" ) );
		jp.add( new JRadioButton( "Radio Button" ) );
		jp.add( new JLabel( "Label" ) );
		jp.setLayout( new FlowLayout() );
		jf.setContentPane( jp );

		jf.setVisible( true );
	}

}
