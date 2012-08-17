
package com.cybernostics.examples.gui.autocomplete;

import com.cybernostics.lib.gui.autocomplete.GenericComboBox;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;


/**
 * @author jasonw
 *
 */
public class GenericComboboxExample
{
	public static void main( String[] args )
	{
		
		// SystemLook.set();
		JFrame jf = new JFrame( "Generic Combo Test" );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		GenericComboBox gcb = ( new GenericComboBox()
		{

			/**
             * 
             */
			private static final long serialVersionUID = 5898275947990571898L;

			JTextField myTextComponent = null;

			@Override
			protected JTextField getComboTextComponent()
			{
				if ( myTextComponent == null )
				{
					myTextComponent = new JTextField();
					// myTextComponent.setColumns( 40 );
				}
				return myTextComponent;
			}

			@Override
			protected void initPopupComponent( final JPanel popUpContentPanel )
			{
				popUpContentPanel.setBorder( new LineBorder( Color.BLACK ) );
				popUpContentPanel.setOpaque( true );
				popUpContentPanel.setBackground( myTextComponent.getBackground() );
				popUpContentPanel.setLayout( new FlowLayout() );
				popUpContentPanel.add( new JButton( "Hello" ) );

			}
		} );
        
        jf.getContentPane().add( gcb );
		// jf.getContentPane().setBackground( Color.blue );
		jf.validate();
		jf.setVisible( true );
		jf.pack();
	}

}
