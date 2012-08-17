package com.cybernostics.lib.media;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.cybernostics.lib.concurrent.WorkerDoneListener;
import com.cybernostics.lib.exceptions.UnhandledExceptionManager;
import com.cybernostics.lib.media.HttpImageRequest;

/**
 * To start a load an image in a separate thread and set the icon
 * when it is done do this:
 * 
 * JLabel myLabel = new JLabel();
 * LabelSetter.setIcon( myLabel, new URL( "myimage.png" ));
 *
 */
public class LabelSetter implements WorkerDoneListener
{

	public static void setIcon( JLabel client, URL imageSource )
	{
		HttpImageRequest.submit(
			imageSource,
			new LabelSetter( client ) );
	}

	JLabel imageClient = null;

	public LabelSetter( JLabel imageClient )
	{
		this.imageClient = imageClient;
	}

	/* (non-Javadoc)
	 * @see com.cybernostics.lib.concurrent.WorkerDoneListener#taskDone(java.util.concurrent.Future)
	 */
	@Override
	public void taskDone( Future< Object > completed )
	{

		try
		{
			imageClient.setIcon( new ImageIcon( (BufferedImage) completed.get() ) );
		}
		catch (InterruptedException e)
		{
			// if it was interrupted then who are we to argue?
		}
		catch (ExecutionException e)
		{
			UnhandledExceptionManager.handleException( e );
		}

	}

}
