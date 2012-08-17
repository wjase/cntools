/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cybernostics.examples.lib.gui.layouts;

import com.cybernostics.lib.gui.layout.RelativeLayout;
import com.cybernostics.lib.test.JFrameTest;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author jasonw
 */
public class RelativeLayoutTest
{
    
    
    public static void main( String[] args )
    {
        JFrameTest jf = JFrameTest.create( "Relative layout" );
        // create your component
        JPanel  tstObject = new JPanel();
        tstObject.setLayout( new RelativeLayout());
        
        tstObject.add( new JButton("First"), new Rectangle2D.Double(0,0,0.5,0.5));
        tstObject.add( new JButton("Second"), new Rectangle2D.Double(0.5,0.5,0.5,0.5));
        jf.setContentPane(tstObject);
        
        jf.go();
    }
    
}
