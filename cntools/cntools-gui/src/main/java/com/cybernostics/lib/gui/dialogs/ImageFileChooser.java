package com.cybernostics.lib.gui.dialogs;

import com.cybernostics.lib.gui.ButtonFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.gui.ScreenRelativeDimension;
import com.cybernostics.lib.gui.autocomplete.FileSystemComboBox;
import com.cybernostics.lib.gui.autocomplete.PathListener;
import com.cybernostics.lib.gui.declarative.events.WhenClicked;
import com.cybernostics.lib.gui.grouplayoutplus.GroupLayoutPlus;
import com.cybernostics.lib.gui.grouplayoutplus.PARALLEL;
import com.cybernostics.lib.gui.grouplayoutplus.SEQUENTIAL;
import com.cybernostics.lib.gui.grouplayoutplus.SIZING;
import com.cybernostics.lib.gui.panel.ImagePane;
import com.cybernostics.lib.gui.windowcore.DialogResponses;
import com.cybernostics.lib.media.image.ImageLoader;
import com.cybernostics.lib.resourcefinder.ResourceFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout.Alignment;
import javax.swing.*;

public class ImageFileChooser extends JDialog
{

	// once we have all our icons loaded then select it.
	PropertyChangeListener whenPathRefreshDone = new PropertyChangeListener()
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			if (( (Boolean) evt.getNewValue() ) == true)
			{
				try
				{
					if (selectedFile != null)
					{
						imageList.setSelectedItems( selectedFile.toURI()
							.toURL() );
						if (imageList.getSelectedItems()
							.isEmpty())
						{
							imageList.selectFirstItem();
						}

					}
				}
				catch (MalformedURLException ex)
				{
					Logger.getLogger(
						ImageFileChooser.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}
			}

		}
	};
	PropertyChangeListener whenSelectionChanged = new PropertyChangeListener()
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			// if item selected then update text
			if (( (Boolean) evt.getNewValue() ) == true)
			{
				Set< Object > theSet = imageList.getSelectedItems();
				if (theSet.size() > 0)
				{
					URL url = (URL) theSet.iterator()
						.next();
					File f;
					try
					{
						f = new File( url.toURI() );
						setSelectedFile( f );
					}
					catch (URISyntaxException e)
					{
						e.printStackTrace();
					}

				}

			}
		}
	};

	public static ImageFileChooser getChooser( Component parent )
	{
		Window winancestor = SwingUtilities.getWindowAncestor( parent );

		ImageFileChooser ch = new ImageFileChooser();
		ch.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );

		if (winancestor != null)
		{
			ch.setLocationRelativeTo( winancestor );

			return ch;
		}

		return new ImageFileChooser();

	}

	private DialogResponses result = DialogResponses.CANCEL_ANSWER;
	private File selectedFile;
	private JTextField selectedField = new JTextField( "" );
	private JButton backBtn = ButtonFactory.getStdButton( StdButtonType.PREV );
	private JButton upFolderBtn = ButtonFactory.getStdButton( StdButtonType.PARENT );
	/**
	 * 
	 */
	private static final long serialVersionUID = 6149874618298857994L;

	/**
	 * @param args
	 */

	private Dimension iconSize = new Dimension( upFolderBtn.getIcon()
		.getIconWidth(), upFolderBtn.getIcon()
		.getIconHeight() );
	private FileSystemComboBox folderPicker = new FileSystemComboBox();
	private ImagePane imageList = null;
	private String path = folderPicker.getCurrent()
		.toString();
	private JButton okBut = ButtonFactory.getStdButton(
		StdButtonType.YES,
		iconSize );
	private JButton cancelBut = ButtonFactory.getStdButton(
		StdButtonType.CANCEL,
		iconSize );

	public ImageFileChooser()
	{
		init();
	}

	private static File lastPickedImage = null;

	private void init()
	{

		folderPicker.setOpaque( false );
		setModalityType( ModalityType.DOCUMENT_MODAL );

		//        okBut.setOpaque( true );
		okBut.setContentAreaFilled( true );
		//        okBut.setBorderPainted( true );
		//        cancelBut.setOpaque( true );
		//        cancelBut.setBorderPainted( true );

		GroupLayoutPlus glp = new GroupLayoutPlus( getContentPane() );
		JLabel lookInLbl = new JLabel( "Look in:" );
		JLabel fileNameLbl = new JLabel( "File name:" );
		imageList = new ImagePane();

		imageList.addPropertyChangeListener(
			ImagePane.ITEMS_LOADED,
			whenPathRefreshDone );
		imageList.addPropertyChangeListener(
			ImagePane.SELECTION_CHANGED,
			whenSelectionChanged );

		imageList.setPreferredSize( new ScreenRelativeDimension( 0.6f, 0.5f ) );
		// Here's the layout of the gui:

		// +----------------------------------------------------------------------------+
		// | [Look in] [ file combo (fill-X) ] [back] [up] |
		// |
		// +-------------------------------------------------------------------------+|
		// | | (Fill-XY ) ||
		// | | ||
		// | | ||
		// | | ||
		// | +-----------------------------------------------------------+|
		// | |
		// | [Filename:] [ (fill-X) ] [OK] [Cancel]|
		// +----------------------------------------------------------------------------+

		glp.setHorizontalGroup( SEQUENTIAL.group(
			PARALLEL.group(
				Alignment.TRAILING,
				lookInLbl,
				fileNameLbl ),
			PARALLEL.group(
				SEQUENTIAL.group(
					SIZING.fill( folderPicker ),
					backBtn,
					upFolderBtn ),
				SIZING
					.fill( imageList ),
				SEQUENTIAL.group(
					SIZING.fill( selectedField ),
					okBut,
					cancelBut ) ) ) );

		glp.linkSize(
			backBtn,
			upFolderBtn );
		glp.setVerticalGroup( SEQUENTIAL.group(
			PARALLEL.group(
				Alignment.CENTER,
				PARALLEL.group(
					Alignment.BASELINE,
					lookInLbl,
					SIZING.fixed( folderPicker ) ),
				backBtn,
				upFolderBtn ),
			SIZING.fill( imageList ),
			PARALLEL
				.group(
					Alignment.BASELINE,
					fileNameLbl,
					SIZING.fixed( selectedField ),
					SIZING.fill( okBut ),
					SIZING
						.fill( cancelBut ) ) ) );
		folderPicker.setDefaultLocation();

		folderPicker.addPathListener( new PathListener()
		{

			@Override
			public void pathChanged( File newPath )
			{
				updateCurrentPath( newPath );

			}
		} );

		backBtn.setVisible( false );

		okBut.setEnabled( false );

		new WhenClicked( okBut )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				// setModalityType( ModalityType.MODELESS );
				setVisible( false );
				result = DialogResponses.OK_ANSWER;
			}
		};

		new WhenClicked( cancelBut )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				// setModalityType( ModalityType.MODELESS );
				setVisible( false );

			}
		};

		new WhenClicked( upFolderBtn )
		{

			@Override
			public void doThis( ActionEvent e )
			{
				folderPicker.upFolder();

			}
		};

		pack();
		setLocationRelativeTo( null );

	}

	public ImageFileChooser( Window owner )
	{
		super( owner );

		init();
	}

	@Override
	public Rectangle getBounds()
	{

		return getBounds( null );
	}

	@Override
	public Rectangle getBounds( Rectangle rv )
	{
		rv = super.getBounds( rv );
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getCenterPoint();
		Dimension d = getSize();
		rv.x = center.x - d.width / 2;
		rv.y = center.y - d.height / 2;

		return rv;
	}

	@Override
	public Point getLocation( Point rv )
	{
		setLocation( rv );
		return super.getLocation( rv );
	}

	public DialogResponses getResult()
	{
		return result;
	}

	public File getSelectedFile()
	{
		return selectedFile;
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getCenterPoint();
		Dimension d = getSize();
		super.setBounds(
			center.x - d.width / 2,
			center.y - d.height / 2,
			width,
			height );
	}

	@Override
	public void setBounds( Rectangle r )
	{
		setBounds(
			r.x,
			r.y,
			r.width,
			r.height );
	}

	@Override
	public void setLocation( int x, int y )
	{
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getCenterPoint();
		Dimension d = getSize();
		super.setLocation(
			center.x - d.width / 2,
			center.y - d.height / 2 );
	}

	@Override
	public void setLocation( Point p )
	{
		setLocation(
			p.x,
			p.y );
	}

	private void setModel( ListModel icons )
	{
		imageList.setModel( icons );

	}

	public void setSelectedFile( File selectedFile )
	{
		File currentParent = ( this.selectedFile != null ) ? this.selectedFile.getParentFile()
			: null;
		this.selectedFile = selectedFile;

		lastPickedImage = selectedFile;

		if (selectedFile == null)
		{
			setDefaultLocation();
		}
		else
		{

			String parentPath = selectedFile.getParentFile()
				.getPath();
			// are we jumping to a new parent folder?
			if (( currentParent == null ) || !currentParent.getPath()
				.equals(
					parentPath ))
			{
				updateCurrentPath( parentPath );

				// if so update the selected item once it is all loaded
			}

			selectedField.setText( selectedFile.getName() );

			okBut.setEnabled( true );
		}

	}

	public void setSelectedFile( String selectedFile )
	{
		if (selectedFile != null)
		{
			setSelectedFile( new File( selectedFile ) );
		}
	}

	public DialogResponses showDialog()
	{
		pack();
		setVisible( true );
		// setModalityType(null);
		dispose();
		return result;
	}

	public DialogResponses showImagePicker()
	{

		return showDialog();
	}

	private File toFile( String locationString )
	{

		URL location = null;

		try
		{
			location = new URL( locationString );
		}
		catch (MalformedURLException e)
		{
		}

		File currentFile = null;
		try
		{
			if (location != null)
			{
				currentFile = new File( location.toURI() );
				return currentFile;
			}

		}
		catch (URISyntaxException ex)
		{
			if (location != null)
			{
				currentFile = new File( location.getPath() );
			}

		}

		if (currentFile == null)
		{
			currentFile = new File( locationString );
		}

		return currentFile;

	}

	private ResourceFilter itemFilter = null;

	public ResourceFilter getItemFilter()
	{
		return itemFilter;
	}

	public void setItemFilter( ResourceFilter itemFilter )
	{
		this.itemFilter = itemFilter;
		String temp = this.path;
		this.path = null;
		updateCurrentPath( temp );
	}

	public void updateCurrentPath( File currentFile )
	{
		if (!currentFile.isDirectory())
		{
			File parentPath = currentFile.getParentFile();
			if (parentPath != null)
			{
				updateCurrentPath( parentPath );
			}
		}
		else
		{
			String newPath = currentFile.getAbsolutePath();
			if (this.path == null || !this.path.equals( newPath ))
			{
				try
				{
					this.path = newPath;
					setModel( ImageLoader.getThumbIcons(
						currentFile.toURI()
							.toURL(),
						null,
						itemFilter ) );
					folderPicker.setCurrent( currentFile );
				}
				catch (MalformedURLException ex)
				{
					Logger.getLogger(
						ImageFileChooser.class.getName() )
						.log(
							Level.SEVERE,
							null,
							ex );
				}
			}

		}

	}

	public void updateCurrentPath( String pathToStart )
	{
		File pathFile = pathToStart != null ? toFile( pathToStart )
			: folderPicker.goHome();

		updateCurrentPath( pathFile );
		validate();
	}

	/**
	 * 
	 */
	public void setDefaultLocation()
	{
		if (lastPickedImage != null)
		{
			setSelectedFile( lastPickedImage.getPath() );
			okBut.setEnabled( true );
		}
		else
		{
			folderPicker.setDefaultLocation();
		}

	}

	/**
	 * @return
	 */
	public static File getLastPicked()
	{
		return lastPickedImage;
	}
}
