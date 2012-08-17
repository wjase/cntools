package com.cybernostics.examples.lib.gui;

import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.icon.SelectedIcon;
import com.cybernostics.lib.gui.layout.RelativeLayout;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class IconFactoryExample
{

    public static void main( String[] args )
    {
        
        JFrame jf = new JFrame( "IconTest" );
        jf.setSize( 200, 200 );

        JButton jb = ButtonFactory.getStdButton( StdButtonType.DICE );

        System.out.printf( "%s\n", jb.getModel().getClass().getName() );
        jb.setSelectedIcon( new SelectedIcon( jb.getIcon() ) );
        jb.setOpaque( true );
        jb.setContentAreaFilled( true );
        JPanel jp = new JPanel( new RelativeLayout() );
        jp.add( jb, new Rectangle2D.Double(0.2,0.2,0.6,0.6) );
        jf.getContentPane().add( jp );
        jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        jf.setVisible( true );
    }

}
