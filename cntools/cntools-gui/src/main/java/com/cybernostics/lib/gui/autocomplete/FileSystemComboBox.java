package com.cybernostics.lib.gui.autocomplete;

import com.cybernostics.lib.collections.IterableArray;
import java.awt.AWTEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.cybernostics.lib.gui.lookandfeel.NimbusLook;
import com.cybernostics.lib.gui.declarative.events.WhenMadeVisible;
import com.cybernostics.lib.gui.declarative.events.WhenResized;

/**
 * Implements a dropdown combo with a filesystem tree as a popup. In addition,
 * it will suggest the current list of sub folders whenever a file path
 * separator eg '/' is pressed. Once the suggestions are displayed, they will be
 * refined as more text is typed
 * 
 * @author jasonw
 * 
 */
public class FileSystemComboBox extends GenericComboBox
{

	ArrayList< PathListener > pathListeners = new ArrayList< PathListener >();
	/**
	 * 
	 */
	private static final long serialVersionUID = 3939608961695766787L;

	final private AutoCompleteTextField pathTextField = new AutoCompleteTextField()
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6015194499225826958L;

		@Override
		protected void autoCompleteTextSelected( String selectedText )
		{

			String current = getCurrent().toString();
			if (current.charAt( current.length() - 1 ) != fileSeparator.charAt( 0 ))
			{
				String newpath = current + fileSeparator + selectedText;
				folderPanel.selectPath( newpath );
			}
			else
			{
				String newpath = current + selectedText;
				folderPanel.selectPath( newpath );

			}

		}
	};
	final private FileSystemPanel folderPanel = new FileSystemPanel();
	String fileSeparator = System.getProperty(
		"file.separator",
		"/" );
	boolean adjustingPathOnShow = false;

	public FileSystemComboBox()
	{
		super();
		initComponent();
	}

	public FileSystemComboBox( String path )
	{
		super( path );
		initComponent();
	}

	public void addPathListener( PathListener listener )
	{
		pathListeners.add( listener );
	}

	public void firePathChanged( File newPath )
	{
		for (PathListener eachListener : pathListeners)
		{
			eachListener.pathChanged( newPath );
		}
	}

	@Override
	protected JTextField getComboTextComponent()
	{
		return pathTextField;
	}

	@Override
	public Object getCurrent()
	{
		return new File( folderPanel.getSelectedPathText() );

	}

	private FileSystemPanel getFileSystemPanel()
	{
		return folderPanel;
	}

	public File goHome()
	{

		folderPanel.selecttHomePath();
		return folderPanel.getSelectedPathFile();
	}

	public void initComponent()
	{
		// for ( Object item : UIManager.getDefaults().keySet() )
		// {
		// System.out.println( item.toString() );
		// }
		// setBorder( new LineBorder(ComboBoxUI.) )
		pathTextField.setAutoCompleteOptionProvider( getFileSystemPanel() );
		pathTextField.setPopupTrigger( System.getProperty(
			"file.separator" )
			.charAt(
				0 ) );
		// pathTextField.setColumns( 150 );

		new WhenMadeVisible( folderPanel )
		{

			@Override
			public void doThis( AWTEvent e )
			{
				String s = pathTextField.getText();
				if (s.length() > 0)
				{
					folderPanel.selectPath( s );
				}

			}
		};

		folderPanel.getTree()
			.getSelectionModel()
			.setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION );

		folderPanel.getTree()
			.addTreeSelectionListener(
				new TreeSelectionListener()
			{

				@Override
				public void valueChanged( final TreeSelectionEvent e )
			{
				TreePath tp = e.getNewLeadSelectionPath();
				if (tp != null)
				{
					Object newPath = tp.getLastPathComponent();
					if (newPath instanceof File)
					{
						File asFile = (File) newPath;

						boolean hasFolders = false;

						for (String name : IterableArray.get( asFile.list() ))
						{
							File child = new File( asFile, name );
							if (child.isDirectory())
							{
								hasFolders = true;
								break;
							}
						}

						if (!hasFolders)
						{
							if (isPopupShown())
							{
								hidePopup();
							}

						}

						getComboTextComponent().setText(
							asFile.getAbsolutePath() );

						firePathChanged( asFile );
					}

				}

			}
			} );

		new WhenResized( pathTextField )
		{

			@Override
			public void doThis( ComponentEvent e )
			{
				folderPanel.setMinimumWidth( pathTextField.getSize().width );

			}
		};

	}

	@Override
	protected void initPopupComponent( JPanel contentPanel )
	{
		contentPanel.setLayout( new GridLayout() );
		contentPanel.add( getFileSystemPanel() );
		contentPanel.validate();

	}

	public void removePathListener( PathListener listener )
	{
		pathListeners.remove( listener );
	}

	public void setColumns( int cols )
	{
		pathTextField.setColumns( cols );
	}

	@Override
	public void setCurrent( Object value )
	{
		folderPanel.selectPath( ( (File) value ).getAbsolutePath() );
	}

	public void upFolder()
	{
		TreePath tp = folderPanel.getTree()
			.getSelectionPath();

		folderPanel.getTree()
			.setSelectionPath(
				tp.getParentPath() );
	}

	/**
	 * Sets the folder location to a sensible default
	 * 
	 * windows: My Documents everything else use.dir
	 */
	public void setDefaultLocation()
	{
		folderPanel.selecttHomePath();

	}
}
