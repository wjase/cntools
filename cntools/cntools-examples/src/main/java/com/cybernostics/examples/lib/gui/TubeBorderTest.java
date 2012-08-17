
package com.cybernostics.examples.lib.gui;

import com.cybernostics.lib.gui.shapeeffects.ShapedPanel;
import com.cybernostics.lib.gui.border.TubeBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jasonw
 */
public class TubeBorderTest
{

    public static void main( String[] args )
    {
        JFrame jf = new JFrame( "ShapedPanelTest" );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setSize( 400, 400 );
        jf.getContentPane().setLayout( new FlowLayout() );
        jf.getContentPane().setBackground( Color.red );
        JPanel jp = new ShapedPanel();
        jp.setBackground( Color.blue );

        jp.setPreferredSize( new Dimension( 200, 200 ) );
        jp.setBorder( new TubeBorder( 50 ) );
        jf.getContentPane().add( jp );
        jf.setVisible( true );
    }
}
