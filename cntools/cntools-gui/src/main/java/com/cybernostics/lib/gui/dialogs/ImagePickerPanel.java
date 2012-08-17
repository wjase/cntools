package com.cybernostics.lib.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListModel;

import com.cybernostics.lib.gui.panel.ImagePane;
import com.cybernostics.lib.resourcefinder.ResourceFinder;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import java.net.URL;

public class ImagePickerPanel extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5818440912922871737L;

	private String selectedImageURL;
	private AbstractButton selectedButton = null;

	public AbstractButton getSelectedButton()
	{
		return selectedButton;
	}

	public JFrame myFrame = null;

	public ImagePickerPanel( URL pathFrom )
	{
		this( pathFrom, null );
	}

	public ImagePickerPanel( URL pathFrom, ResourceFilter filt )
	{
		ListModel lm = ImageLoader.getThumbIcons(
			pathFrom,
			null,
			filt );

		setLayout( new GridLayout() );
		add(
			new ImagePane( lm, new ActionListener()
			{

				@Override
				public void actionPerformed( ActionEvent e )
			{
				AbstractButton jb = (AbstractButton) e.getSource();
				if (jb.getIcon() instanceof ImageIcon)
				{
					ImageIcon ii = (ImageIcon) jb.getIcon();
					setSelectedImageURL( ii.toString() );
				}
				selectedButton = jb;
				setVisible( false );
			}
			} ),
			BorderLayout.CENTER );
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
