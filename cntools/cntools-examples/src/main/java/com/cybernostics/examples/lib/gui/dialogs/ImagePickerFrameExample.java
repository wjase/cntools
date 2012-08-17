package com.cybernostics.examples.lib.gui.dialogs;

import com.cybernostics.lib.gui.dialogs.ImagePickerFrame;

public class ImagePickerFrameExample
{

	/**
	 * @param args
	 */
	public static void main( String[] args )
	{
		// ImagePicker ip = new ImagePicker(
		// "C:/data/images/Photographs/2008/2008_01_17" );
		ImagePickerFrame ip = new ImagePickerFrame( "images/stamps" );
		ip.setVisible( true );
	}


}
