package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.lib.gui.dialogs.RoundColorButton;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ToolTipManager;

public class RoundColorButtonExample
{

	// Test routine.
	public static void main( String[] args )
	{
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled( true );
		

		// Create a button with the label "Jackpot".
		JButton bluebutton = new RoundColorButton( Color.blue, "Blue" );
		JButton pinkbutton = new RoundColorButton( Color.pink, "Pink" );
		JButton graybutton = new RoundColorButton( Color.gray, "Gray" );

		// Create a frame in which to show the button.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.getContentPane().setBackground( Color.gray );
		frame.getContentPane().add( bluebutton );
		frame.getContentPane().add( pinkbutton );
		frame.getContentPane().add( graybutton );
		frame.getContentPane().setLayout( new FlowLayout() );
		frame.setSize( 150, 150 );
		frame.setVisible( true );
	}
}