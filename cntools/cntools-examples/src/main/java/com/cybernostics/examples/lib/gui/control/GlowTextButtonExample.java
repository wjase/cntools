
package com.cybernostics.examples.lib.gui.control;

import com.cybernostics.lib.gui.control.GlowTextButton;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Implements a transparent glowing text togglebutton
 *
 * @author jasonw
 *
 */
public class GlowTextButtonExample
{


	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "Test" );
		jf.setSize( 400, 400 );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		JPanel p = new JPanel();
		GlowTextButton gb = new GlowTextButton( "Easy", Color.green.brighter().brighter(), 12 );
		gb.setMnemonic( 'E' );
		GlowTextButton gb2 = new GlowTextButton( "Medium", Color.green.brighter().brighter(), 12 );
		gb2.setMnemonic( 'M' );
		GlowTextButton gb3 = new GlowTextButton( "Hard", Color.green.brighter().brighter(), 12 );
		gb3.setMnemonic( 'H' );

		p.add( gb );
		p.add( gb2 );
		p.add( gb3 );

		p.validate();
		ButtonGroup bg = new ButtonGroup();
		bg.add( gb );
		bg.add( gb2 );
		bg.add( gb3 );

		gb.setSelected( true );

		jf.getContentPane().add( p );
		jf.setVisible( true );
	}
}
