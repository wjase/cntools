
package com.cybernostics.examples.lib.gui;

import com.cybernostics.lib.gui.ParentSize;
import com.cybernostics.lib.test.JFrameTest;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author jasonw
 */
public class ParentSizeBinding
{

    public static void main( String[] args )
    {
        JFrameTest jf = JFrameTest.create( "partent size" );
        // create your component
        JPanel tstObject = new JPanel();
        tstObject.setBackground( Color.red );
        jf.getContentPane().setLayout( null );
        jf.getContentPane().add( tstObject );
        ParentSize.bind( jf, tstObject );
        jf.go();
    }

}
