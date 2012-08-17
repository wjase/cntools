package com.cybernostics.examples.lib.gui.autocomplete;

import com.cybernostics.laf.wood.WoodLookAndFeel;
import com.cybernostics.lib.gui.autocomplete.CustomComboArrowButton;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;


public class CustomComboArrowButtonExample 
{

	public static void main( String[] args )
	{
		WoodLookAndFeel.setUI();

		JFrame jf = new JFrame( "Test" );

		jf.setSize( 400, 400 );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JPanel p = new JPanel();
		CustomComboArrowButton but = new CustomComboArrowButton( SwingConstants.SOUTH );
		System.out.println( but.getUIClassID() );
		p.add( but );
		jf.getContentPane().add( p );
		jf.setVisible( true );

		for (Entry<Object, Object> eachAssoc : UIManager.getLookAndFeelDefaults().entrySet())
		{
			String key = eachAssoc.getKey().toString();
			if (key.indexOf( "ombo" ) != -1 || key.indexOf( "rrow" ) != -1)
			{
				System.out.printf( "%s=%s\n", key, eachAssoc.getValue() );

			}
		}

	}
}
