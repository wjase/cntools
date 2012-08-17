package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.concurrent.GUIEventThread;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;

import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.control.CropControl;
import com.cybernostics.lib.gui.control.CropInfo;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.gui.grouplayoutplus.SIZING;
import com.cybernostics.lib.resourcefinder.RegexURLFilter;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import java.net.URI;

public final class CropDialog extends JDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7885549636662995650L;

	private DialogResponses result = DialogResponses.CANCEL_ANSWER;
	CropControl theCropper = new CropControl();
	private JButton okBut = ButtonFactory.getStdButton( StdButtonType.YES );
	private JButton cancelBut = ButtonFactory.getStdButton( StdButtonType.CANCEL );
	private JButton browseBut = ButtonFactory.getStdButton( StdButtonType.OPEN );
	private JLabel dialogDescription = new JLabel(
		"Drag the red handles to select the part of the image you wish to use." );
	private int maximumWidth = 500;

	public CropDialog()
	{
		setTitle( "Crop Image" );
		getLastPicked();
	}

	public CropDialog( Component owner, CropInfo face )
	{
		super( SwingUtilities.getWindowAncestor( owner ) );
		setTitle( "Change Image" );
		if (face != null)
		{
			theCropper.setImage( face );
		}
		else
		{
			getLastPicked();
		}
		setModalityType( ModalityType.DOCUMENT_MODAL );

	}

	public CropDialog( Component owner, URI loc )
	{
		super( SwingUtilities.getWindowAncestor( owner ) );
		setTitle( "Crop Image" );
		theCropper.setImage( new CropInfo( loc ) );
		setModalityType( ModalityType.DOCUMENT_MODAL );

	}

	public void setImage( CropInfo image )
	{
		theCropper.setImage( image );
	}

	@Override
	public void addNotify()
	{
		super.addNotify();
		init();
	}

	public void getLastPicked()
	{
		File lastPicked = ImageFileChooser.getLastPicked();

		if (lastPicked == null)
		{
			changeImage();
		}
		else
		{
			theCropper.setImage( new CropInfo( lastPicked.toURI() ) );
		}
	}

	private static ResourceFilter bitmapsOnly = RegexURLFilter.getExcluder( "svg$" );

	public void changeImage()
	{

		CropInfo currentImage = theCropper.getCropped();
		ImageFileChooser chooser = ImageFileChooser.getChooser( this );
		chooser.setItemFilter( bitmapsOnly );
		if (currentImage != null)
		{
			URI toLoad = currentImage.getLocation();
			if (toLoad != null)
			{
				chooser.setSelectedFile( new File( toLoad ) );
			}
		}
		if (chooser.showDialog() == DialogResponses.OK_ANSWER)
		{
			currentImage.setLocation( chooser.getSelectedFile() );
			theCropper.setImage( currentImage );
		}
		validate();
	}

	public CropInfo getImage()
	{
		return theCropper.getCropped();
	}

	public int getMaximumWidth()
	{
		return maximumWidth;
	}

	public DialogResponses getResult()
	{
		return result;
	}

	private void init()
	{
		setMinimumSize( new ScreenRelativeDimension( .6f, .6f ) );
		okBut.setContentAreaFilled( true );
		cancelBut.setContentAreaFilled( true );

		GroupLayoutPlus glp = new GroupLayoutPlus( getContentPane() );
		glp.setHorizontalGroup( PARALLEL.group(
			Alignment.TRAILING,
			PARALLEL.group(
				Alignment.CENTER,
				dialogDescription,
				theCropper ),
			SEQUENTIAL.group(
				browseBut,
				okBut,
				cancelBut ) ) );
		glp.setVerticalGroup( SEQUENTIAL.group(
			dialogDescription,
			SIZING.fill( theCropper ),
			PARALLEL.group(
				browseBut,
				okBut,
				cancelBut ) ) );
		glp.linkSize(
			cancelBut,
			okBut );
		pack();

		new WhenClicked( okBut )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				setVisible( false );
				result = DialogResponses.OK_ANSWER;
			}
		};

		new WhenClicked( cancelBut )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				setVisible( false );

			}
		};

		new WhenClicked( browseBut )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				changeImage();
			}
		};

		new WhenMadeVisible( this )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				if (theCropper.getImage() == null)
				{
					GUIEventThread.run( new Runnable()
					{

						@Override
						public void run()
						{
							changeImage();
						}
					} );

				}
			}
		};

		pack();
		setLocationRelativeTo( null );
	}

	public void setMaximumWidth( int maximumWidth )
	{
		this.maximumWidth = maximumWidth;
	}
}
