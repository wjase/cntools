package com.cybernostics.examples.lib.gui.control;

import com.cybernostics.lib.ResourcesRoot;
import com.cybernostics.lib.concurrent.GUIEventThread;
import com.cybernostics.lib.gui.control.StyledSlider;
import com.cybernostics.lib.media.icon.ScalableSVGIcon;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;


/**
 *
 * @author jasonw
 */
public class SliderTest
{

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "TEst" );
		jf.setSize( 800, 600 );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		jf.getContentPane().setLayout( new FlowLayout() );

		StyledSlider slider = new StyledSlider( "slider_box", ResourcesRoot
			.getResource( "gui/control/icons/slider.svg" ) );
		slider.setThumbHandle( new ScalableSVGIcon( ResourcesRoot.getResource( "gui/control/icons/handle.svg" ) ) );

		slider.setPreferredSize( new Dimension( 80, 40 ) );

		jf.getContentPane().add( slider );

		jf.invalidate();

		GUIEventThread.show( jf );

	}

}
