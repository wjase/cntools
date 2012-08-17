package com.cybernostics.lib.gui.autocomplete;

import com.cybernostics.lib.gui.declarative.events.RunLater;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class FileSystemPanel extends JPanel implements AutoCompleteOptionSource
{

	private int minimumWidth = -1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4652671561613232044L;

	private FileSystemTreeModel fileSystemDataModel = new FileSystemTreeModel();
	private JTree tree = new JTree( fileSystemDataModel );
	char pathSeparator = System.getProperty(
		"file.separator" )
		.charAt(
			0 );

	public FileSystemPanel()
	{
		new RunLater()
		{

			@Override
			public void run( Object... args )
			{
				initui();

			}
		};

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#addNotify()
	 */
	@Override
	public void addNotify()
	{

		super.addNotify();

	}

	/**
	 * 
	 */
	private void initui()
	{
		setLayout( new GridLayout() );

		FileSystemTreeCellRenderer renderer = new FileSystemTreeCellRenderer();
		tree.setExpandsSelectedPaths( true );
		// tree.clearSelection();
		tree.setRootVisible( false );
		tree.setVisibleRowCount( 25 );
		tree.setShowsRootHandles( true );
		tree.setCellRenderer( renderer );
		tree.setRowHeight( -1 );

		JScrollPane scrollPane = new JScrollPane( tree );
		add( scrollPane );
		validate();
	}

	public String[] getCurrentChildren( String matching )
	{
		ArrayList< String > filtered = new ArrayList< String >();
		String[] children = null;
		TreePath current = tree.getSelectionPath();

		if (current != null)
		{
			Object lastBit = current.getLastPathComponent();
			int childCount = tree.getModel()
				.getChildCount(
					lastBit );

			for (int index = 0; index < childCount; ++index)
			{
				String thisName = ( (File) tree.getModel()
					.getChild(
						lastBit,
						index ) ).getName();
				if (thisName.startsWith( matching ))
				{
					filtered.add( thisName );
				}

			}

		}
		children = new String[ filtered.size() ];
		filtered.toArray( children );
		return children;
	}

	@Override
	public Dimension getMinimumSize()
	{
		Dimension d = super.getMinimumSize();
		d.width = Math.max(
			d.width,
			minimumWidth );
		return d;
	}

	public int getMinimumWidth()
	{
		return minimumWidth;
	}

	@Override
	public String[] getOptions( String currentInput )
	{
		// Split the path into a path and file part
		// i.e. c:\\somefolder\\afile would produce:
		// pathPart -> c:\\somefolder
		// filePart -> afile
		// =========================================
		//
		// Special Cases:
		// / - unix root directory - pathPartWill be empty - return top level
		// folders
		// c:\\ dos drive root - path part will end in : - returen top level
		// folders under that drive
		//
		int lastPos = currentInput.lastIndexOf( pathSeparator );
		String pathPart = "";
		String filePart = "";

		if (lastPos != -1)
		{
			pathPart = currentInput.substring(
				0,
				lastPos );
			if (currentInput.charAt( currentInput.length() - 1 ) != pathSeparator)
			{
				filePart = currentInput.substring( lastPos + 1 );
			}
			else
			{
			}

		}

		selectPath( pathPart + pathSeparator );
		String[] kids = getCurrentChildren( filePart );
		// now find the tree node

		return kids;
	}

	@Override
	public Dimension getPreferredSize()
	{
		Dimension d = super.getPreferredSize();
		d.width = Math.max(
			d.width,
			minimumWidth );
		return d;
	}

	public File getSelectedPathFile()
	{
		TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			if (tp.getLastPathComponent() instanceof File)
			{
				File selected = (File) tp.getLastPathComponent();
				if (selected != null)
				{
					return selected;
				}

			}

		}
		return null;
	}

	String getSelectedPathText()
	{
		TreePath tp = tree.getSelectionPath();
		if (tp != null)
		{
			if (tp.getLastPathComponent() instanceof File)
			{
				File selected = (File) tp.getLastPathComponent();
				if (selected != null)
				{
					return selected.getAbsolutePath();
				}

			}

		}
		return "";
	}

	public JTree getTree()
	{
		return tree;
	}

	/**
	 * Selects the nominated path or selects the roots if the path isn't found.
	 * 
	 * @param sPath
	 * @return
	 */
	public boolean selectPath( String sPath )
	{
		File leadFile = new File( sPath );
		TreePath path = new TreePath( tree.getModel()
			.getRoot() );

		boolean foundIt = leadFile.exists();
		if (foundIt)
		{
			ArrayList< Object > paths = new ArrayList< Object >();

			while (leadFile != null)
			{
				paths.add(
					0,
					leadFile ); // add at the beginning
				leadFile = leadFile.getParentFile();
			}
			for (Object eachObject : paths)
			{
				path = path.pathByAddingChild( eachObject );
			}
		}

		tree.setSelectionPath( path );
		tree.scrollPathToVisible( path );
		return foundIt;
	}

	public void selecttHomePath()
	{

		TreePath path = new TreePath( tree.getModel()
			.getRoot() );
		path = path.pathByAddingChild( fileSystemDataModel.getDefaultDirectory() );
		tree.setSelectionPath( path );
	}

	public void setMinimumWidth( int minimumWidth )
	{
		this.minimumWidth = minimumWidth;
	}
}

/**
 * This class is a humble attempt to unify the universe of Microsoft and *nix. I
 * impose a virtual root to rule them all to avoid the microsoft dilema of
 * multiple file roots. When converting paths it is just ignored or appended as
 * required. It never appears in the tree by virtue of the custom cell renderer.
 * 
 * @author jasonw
 * 
 */
class FileSystemRoot
{

	public FileSystemRoot()
	{
	}

	@Override
	public String toString()
	{
		return "+";
	}
}
