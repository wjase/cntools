package com.cybernostics.lib.svg.editor.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileFilter;
import com.cybernostics.lib.svg.editor.ImageStampTool;
import com.cybernostics.lib.svg.editor.SVGEditor;
import javax.swing.AbstractAction;

import javax.swing.JFileChooser;

/**
 *
 * @author jasonw
 */
public class ChooseImageAction extends ControlSelectAction
{
	public static final String ACTION_NAME_KEY = "Stamps";

	public ChooseImageAction( SVGEditor editor )
	{
		super( editor, new ImageStampTool() );
		putValue(
			AbstractAction.SHORT_DESCRIPTION,
			ACTION_NAME_KEY );
	}

	public static final String IMAGE_PICKER = "IMAGE_PICKER";
	private ImagePicker picker = null;

	public void setPicker( ImagePicker picker )
	{
		putValue(
			IMAGE_PICKER,
			picker );
	}

	ImagePicker getPicker()
	{
		if (picker == null)
		{
			ImagePicker value = (ImagePicker) getValue( IMAGE_PICKER );
			if (value != null)
			{
				picker = value;
			}
			else
			{
				return getDefault();
			}
		}
		return picker;
	}

	@Override
	public void onActionPerformed( ActionEvent e )
	{
		URL chosen = getPicker().getImage();
		if (chosen != null)
		{
			setImage( chosen );
			super.onActionPerformed( e );
		}

	}

	private ImagePicker defaultPicker = null;

	private ImagePicker getDefault()
	{
		if (defaultPicker == null)
		{
			defaultPicker = new ImagePicker()
			{

				@Override
				public URL getImage()
				{
					JFileChooser chooser = new JFileChooser();
					// Note: source for ExampleFileFilter can be found in FileChooserDemo,
					// under the demo/jfc directory in the JDK.
					FileFilter filter = new FileFilter()
					{

						@Override
						public boolean accept( File pathname )
						{
							return pathname.getName()
								.endsWith(
									"jpg" ) || pathname.getName()
								.endsWith(
									"png" )
								|| pathname.getName()
									.endsWith(
										"svg" );
						}

						@Override
						public String getDescription()
						{
							return "Image Files";
						}
					};
					chooser.setFileFilter( filter );
					int returnVal = chooser.showOpenDialog( getEditor() );
					if (returnVal == JFileChooser.APPROVE_OPTION)
					{
						try
						{
							URL toLoad = chooser.getSelectedFile()
								.toURI()
								.toURL();
							return toLoad;
						}
						catch (MalformedURLException ex)
						{
							Logger.getLogger(
								ChooseImageAction.class.getName() )
								.log(
									Level.SEVERE,
									null,
									ex );
						}
					}
					return null;
				}
			};
		}
		return defaultPicker;
	}

	public void setImage( URL toSet )
	{
		( (ImageStampTool) getModeController() ).setStampURL( toSet );
	}

	public URL getCurrent()
	{
		return ( (ImageStampTool) getModeController() ).getStampURL();
	}
}
