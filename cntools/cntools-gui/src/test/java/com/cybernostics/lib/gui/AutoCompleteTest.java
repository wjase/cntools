package com.cybernostics.lib.gui;

import com.cybernostics.lib.gui.autocomplete.AutoCompleteOptionSource;
import com.cybernostics.lib.gui.autocomplete.AutoCompleteTextField;
import com.cybernostics.lib.gui.declarative.events.RunLater;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

/**
 *
 * @author jasonw
 */
public class AutoCompleteTest
{
	// private static final PopupFactory factory =
	// PopupFactory.getSharedInstance();
	public static void main( String[] args )
	{
		new RunLater()
		{
			@Override
			public void run( Object... args )
			{
				final JFrame jf = new JFrame( "autocomplete test" );
				jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

				AutoCompleteOptionSource source = new OptionSource();
				jf.getContentPane()
					.setLayout(
						new FlowLayout() );
				AutoCompleteTextField autoField = new AutoCompleteTextField( "" );
				autoField.setAutoCompleteOptionProvider( source );
				autoField.setColumns( 40 );

				jf.getContentPane()
					.add(
						autoField );
				jf.setVisible( true );
				// jf.pack();
				jf.getContentPane()
					.validate();
				Dimension ps = jf.getContentPane()
					.getPreferredSize();
				Dimension ps2 = new Dimension( ps.width, ps.height );

				jf.setSize(
					ps2.width + 100,
					ps2.height + 160 );

			}
		};

	}

}
