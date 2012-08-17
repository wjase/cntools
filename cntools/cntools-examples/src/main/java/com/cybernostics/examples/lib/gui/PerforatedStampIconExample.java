package com.cybernostics.examples.lib.gui;

import com.cybernostics.lib.gui.icon.PerforatedStampIcon;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.icon.LoadingIcon;
import com.cybernostics.lib.media.icon.ScalableIcon;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * The Stamp Icon implements a postage-stamp-like effect around an image.
 * 
 * @author jasonw
 * 
 */
public class PerforatedStampIconExample 
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		JFrame myApp = new JFrame( "Stamp Test" );

		myApp.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		myApp.setBackground( Color.black );

		

		// ImageIcon test = new
		// ImageIcon("file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg");
		ScalableIcon test = null;
		test = ( ScalableIcon ) LoadingIcon.get(); // ResourceLoader.current.loadImage( "file:///C:/data/images/Photographs/2009/2009_01_01/IMG_5184.jpg" );

		PerforatedStampIcon stamp = new PerforatedStampIcon( test );

		JLabel jb = new JLabel( stamp );
		jb.setBackground( Color.gray );
		// jb.setContentAreaFilled(false);
		// jb.setBorderPainted(false);
		jb.setOpaque( true );
		myApp.add( jb );

		myApp.pack();
		myApp.setVisible( true );
	}

}
