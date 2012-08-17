package com.cybernostics.examples.lib.gui.autocomplete;

import com.cybernostics.lib.gui.autocomplete.FileSystemComboBox;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import com.cybernostics.lib.gui.lookandfeel.NimbusLook;

/**
 * Implements a dropdown combo with a filesystem tree as a popup. In addition,
 * it will suggest the current list of sub folders whenever a file path
 * separator eg '/' is pressed. Once the suggestions are displayed, they will be
 * refined as more text is typed
 * 
 * @author jasonw
 * 
 */
public class FileSystemComboBoxExample
{


	public static void main( String[] args )
	{

		NimbusLook.set();
		JFrame jf = new JFrame( "Generic Combo Test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		FileSystemComboBox fscb = new FileSystemComboBox();
		fscb.setColumns( 60 );
		jf.getContentPane().setLayout( new FlowLayout() );
		jf.getContentPane().add( fscb );

		// AutoCompleteTextField autoField = new AutoCompleteTextField( "" );
		// autoField.setAutoCompleteOptionProvider( new
		// AutoCompleteOptionSource()
		// {
		//
		// @Override
		// public String[] getOptions( String currentInput )
		// {
		// final String[] options =
		// { "Option1", "Option2", "Option2" };
		// return options;
		// }
		// } );
		// autoField.setColumns( 40 );

		// jf.getContentPane().add( autoField );
		jf.pack();
		jf.setVisible( true );

	}

}
