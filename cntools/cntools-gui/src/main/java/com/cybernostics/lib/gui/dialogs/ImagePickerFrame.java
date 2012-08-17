package com.cybernostics.lib.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ListModel;

import com.cybernostics.lib.gui.panel.ImagePane;
import com.cybernostics.lib.gui.window.FullScreenFrame;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.ResourceFinder;

public class ImagePickerFrame extends FullScreenFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5818440912922871737L;

	/**
	 * @param args
	 */
	private String selectedImageURL;

	public JFrame myFrame = null;

	public ImagePickerFrame( String pathFrom )
	{
		super( "Image Picker" );
		myFrame = this;
		ListModel lm = ImageLoader.getIcons( pathFrom );

		this.setExtendedState( this.getExtendedState() | Frame.MAXIMIZED_BOTH );
		setResizable( false );
		setUndecorated( true );
		add(
			new ImagePane( lm, new ActionListener()
			{

				@Override
				public void actionPerformed( ActionEvent e )
			{
				JButton jb = (JButton) e.getSource();
				ImageIcon ii = (ImageIcon) jb.getIcon();
				setSelectedImageURL( ii.toString() );
				setVisible( false );
			}
			} ),
			BorderLayout.CENTER );
		// setVisible(true);
		// validateTree();
		slideIn();
	}

	public String getSelectedImageURL()
	{
		return selectedImageURL;
	}

	public void setSelectedImageURL( String selectedImageURL )
	{
		this.selectedImageURL = selectedImageURL;
	}

}
