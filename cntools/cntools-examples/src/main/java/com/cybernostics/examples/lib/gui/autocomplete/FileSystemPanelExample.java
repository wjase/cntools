package com.cybernostics.examples.lib.gui.autocomplete;

import com.cybernostics.laf.wood.WoodLookAndFeel;
import com.cybernostics.lib.gui.autocomplete.FileSystemPanel;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;


public class FileSystemPanelExample 
{


	public static void main( String[] a )
	{
		// System.out.printf("start\n");
		WoodLookAndFeel.setUI();
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		try
		{
			Thread.sleep( 600 );
		}
		catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final FileSystemPanel fsp = new FileSystemPanel();
		frame.add( fsp );
		// fsp.selectPath( "C:\\data\\java" );

		// System.out.println( "selectd path is now " +
		// fsp.getSelectedPathText() );

		frame.setSize( 300, 200 );
		frame.setVisible( true );

		fsp.getTree().addTreeSelectionListener( new TreeSelectionListener()
		{

			@Override
			public void valueChanged( TreeSelectionEvent e )
			{
				TreePath tp = e.getNewLeadSelectionPath();
				if (tp != null)
				{
					// System.out.println( tp.getLastPathComponent().toString()
					// );
					String[] kids = fsp.getCurrentChildren( "" );
					if (kids != null)
					{
						for (String eachKid : kids)
						{
							//System.out.println( eachKid );
						}
					}
				}

			}
		} );

	}

}
