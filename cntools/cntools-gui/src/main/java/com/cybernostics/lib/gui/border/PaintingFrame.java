package com.cybernostics.lib.gui.border;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.cybernostics.lib.resourcefinder.ResourceFinder;

/**
 * @author jasonw
 *
 */
public class PaintingFrame extends BorderFill
{
	static
	{

	}

	public PaintingFrame()
	{
		super( "images/pictureframe_%s.svg" );
	}

	public static void main( String[] args )
	{
		JFrame jf = new JFrame( "svg fill" );
		jf.setSize(
			400,
			400 );
		jf.getContentPane()
			.setLayout(
				new GridLayout() );

		JPanel p = new JPanel();
		p.setBorder( new PaintingFrame() );

		jf.getContentPane()
			.add(
				p );
		jf.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jf.setVisible( true );
	}

}
