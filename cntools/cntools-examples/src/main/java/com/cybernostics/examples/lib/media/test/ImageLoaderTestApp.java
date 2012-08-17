package com.cybernostics.examples.lib.media.test;

import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.cybernostics.lib.gui.border.GradientDropShadowBorder;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.ResourceFinder;

public class ImageLoaderTestApp
{

	public static void main(String[] args)
	{
		

		SwingUtilities.invokeLater( new Runnable()
		{

			@Override
			public void run()
			{
				JFrame jfTest = new JFrame( "test" );
				jfTest.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				jfTest.setSize( 200, 200 );

				jfTest.getContentPane().setLayout( new BoxLayout( jfTest.getContentPane(), BoxLayout.Y_AXIS ) );

				JList jl = new JList( ImageLoader.getIcons( "file:///C:/data/images/Photographs/2008/2008_01_17" ) );
				jl.setLayoutOrientation( JList.HORIZONTAL_WRAP );
				JScrollPane jsp = new JScrollPane( jl );
				// jsp.setSize( 400, 400 );

				jl.setCellRenderer( new ListCellRenderer()
				{

					@Override
					public Component getListCellRendererComponent(JList list, Object value, int index,
							boolean isSelected, boolean cellHasFocus)
					{
						JLabel imageLab = new JLabel( ( ImageIcon ) value );
						imageLab.setBorder( new GradientDropShadowBorder() );
						return imageLab;
					}
				} );

				jfTest.getContentPane().add( jsp );

				jfTest.validate();
				jfTest.setVisible( true );

			}
		} );
	}
}
