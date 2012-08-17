package com.cybernostics.lib.gui.autocomplete;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileSystemTreeModel implements TreeModel
{

	class DirectoryOnly implements FilenameFilter
	{

		@Override
		public boolean accept( File dir, String name )
		{
			File asFile = new File( dir, name );
			return asFile.isDirectory() || myFileView.get()
				.isComputerNode(
					asFile );
		}
	}

	private Vector< TreeModelListener > listeners = new Vector< TreeModelListener >();
	private DirectoryOnly foldersOnly = new DirectoryOnly();
	private FileSystemRoot dummyRoot = new FileSystemRoot();
	private static Map< File, String > rootNames = null;
	private static Vector< File > rootArray = null;

	private static Vector< File > getRootArray()
	{
		if (rootArray == null)
		{
			rootArray = new Vector< File >();
			rootArray.addAll( getRootNames().keySet() );
		}
		return rootArray;
	}

	private static Map< File, String > getRootNames()
	{
		if (rootNames == null)
		{
			rootNames = new TreeMap< File, String >();

			if (rootNames.isEmpty())
			{
				// Add My Documents as a Root if we are in Windows
				String userProfilePath = System.getenv( "USERPROFILE" );
				if (userProfilePath != null)
				{
					File MyDocs = new File( userProfilePath + "\\My Documents" );
					if (MyDocs.exists())
					{
						rootNames.put(
							MyDocs,
							myFileView.get()
								.getSystemDisplayName(
									MyDocs ) );
					}
				}

				for (File eachFile : File.listRoots())
				{
					// ignore the A:\\ drive on windows as it has a huge delay
					if (!eachFile.getAbsolutePath()
						.equals(
							"A:\\" ))
					{
						rootNames.put(
							eachFile,
							myFileView.get()
								.getSystemDisplayName(
									eachFile ) );
					}
				}
				File homeFolder = myFileView.get()
					.getHomeDirectory();
				rootNames.put(
					homeFolder,
					myFileView.get()
						.getSystemDisplayName(
							homeFolder ) );
			}
		}
		return rootNames;
	}

	public static String getDisplayName( File toShow )
	{
		return myFileView.get()
			.getSystemDisplayName(
				toShow );
	}

	private static SingletonInstance< FileSystemView > myFileView = new SingletonInstance< FileSystemView >()
	{

		@Override
		protected FileSystemView createInstance()
		{
			return FileSystemView.getFileSystemView();
		}

	};

	// private static Map<File,Icon> specialIcons = new HashMap< File, Icon >();
	public static boolean isRoot( File test )
	{
		return getRootNames().containsKey(
			test );
	}

	public static String getName( File f )
	{
		if (isRoot( f ))
		{
			return getRootNames().get(
				f );
		}
		return f.getName();
	}

	public FileSystemTreeModel()
	{
		listeners = new Vector< TreeModelListener >();
	}

	@Override
	public void addTreeModelListener( TreeModelListener l )
	{
		if (( l != null ) && !listeners.contains( l ))
		{
			listeners.addElement( l );
		}
	}

	public void fireTreeNodesChanged( TreeModelEvent e )
	{
		Enumeration< TreeModelListener > listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements())
		{
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesChanged( e );
		}
	}

	public void fireTreeNodesInserted( TreeModelEvent e )
	{
		Enumeration< TreeModelListener > listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements())
		{
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesInserted( e );
		}
	}

	public void fireTreeNodesRemoved( TreeModelEvent e )
	{
		Enumeration< TreeModelListener > listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements())
		{
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeNodesRemoved( e );
		}
	}

	Map< File, String[] > childMap = new HashMap< File, String[] >();

	private String[] getChildren( File parent )
	{
		String[] kidList = childMap.get( parent );

		if (kidList == null)
		{

			kidList = parent.list( foldersOnly );
			if (kidList != null)
			{
				Arrays.sort(
					kidList,
					String.CASE_INSENSITIVE_ORDER );
				//System.out.println( String.format( "put children for %s", parent.getPath() ) );
				childMap.put(
					parent,
					kidList );
			}
		}
		return kidList;
	}

	public void fireTreeStructureChanged( TreeModelEvent e )
	{
		Enumeration< TreeModelListener > listenerCount = listeners.elements();
		while (listenerCount.hasMoreElements())
		{
			TreeModelListener listener = listenerCount.nextElement();
			listener.treeStructureChanged( e );
		}
	}

	@Override
	public Object getChild( Object parent, int index )
	{
		if (parent instanceof File)
		{
			File directory = (File) parent;
			String[] directoryMembers = getChildren( directory );
			File newFile = new File( directory, directoryMembers[ index ] );
			return ( newFile );
		}
		File newFile = getRootArray().get(
			index );

		return ( newFile );

	}

	@Override
	public int getChildCount( Object parent )
	{
		if (parent instanceof File)
		{
			File fileSystemMember = (File) parent;
			if (fileSystemMember.isDirectory())
			{
				String[] directoryMembers = getChildren( fileSystemMember );
				return directoryMembers == null ? 0 : directoryMembers.length;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return getRootArray().size();
		}
	}

	@Override
	public int getIndexOfChild( Object parent, Object child )
	{
		int result = -1;
		if (parent instanceof File)
		{
			File directory = (File) parent;
			File directoryMember = (File) child;
			String[] directoryMemberNames = getChildren( directory );

			String toFind = directoryMember.getName();

			result = Arrays.binarySearch(
				directoryMemberNames,
				toFind,
				String.CASE_INSENSITIVE_ORDER );
			if (result >= 0)
			{
				return result;
			}
			else
			{
				result = -1;
			}

			return result;
		}
		int count = getRootArray().size();
		for (int i = 0; i < count; ++i)
		{
			if (child.equals( getRootArray().get(
				i ) ))
			{
				result = i;
				break;
			}
		}
		return result;
	}

	@Override
	public Object getRoot()
	{
		return ( dummyRoot );
	}

	@Override
	public boolean isLeaf( Object node )
	{
		if (node instanceof File)
		{
			File asFile = (File) node;
			boolean isLeaf = !asFile.isDirectory();
			return isLeaf;
		}
		return false;
	}

	@Override
	public void removeTreeModelListener( TreeModelListener l )
	{
		if (l != null)
		{
			listeners.removeElement( l );
		}
	}

	@Override
	public void valueForPathChanged( TreePath path, Object newValue )
	{
		// Does Nothing!
	}

	/**
	 * @return
	 */
	public Object getDefaultDirectory()
	{
		return myFileView.get()
			.getDefaultDirectory();
	}
}
