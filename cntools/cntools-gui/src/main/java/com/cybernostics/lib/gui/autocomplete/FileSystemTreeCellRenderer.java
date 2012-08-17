package com.cybernostics.lib.gui.autocomplete;

import com.cybernostics.lib.gui.IconFactory;
import com.cybernostics.lib.gui.IconFactory.StdButtonType;
import com.cybernostics.lib.media.icon.AttributedScalableIcon;
import com.cybernostics.lib.media.icon.ScalableIcon;
import com.cybernostics.lib.media.icon.ScalableIconPanel;
import com.cybernostics.lib.media.icon.ScalableImageIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeCellRenderer;

/**
 * 
 * @author jasonw
 */
/**
 * This class is used to render FileSystem Nodes
 * 
 * @author jasonw
 * 
 */
public class FileSystemTreeCellRenderer extends JPanel
	implements
	TreeCellRenderer
{

	final static FileSystemView myFileView;

	static
	{
		myFileView = FileSystemView.getFileSystemView();
	}
	Color background = null;
	Color foreground = null;

	enum FileIconType
	{

		FILE, FOLDER, DOCUMENTS, DESKTOP, COMPUTER
	}

	ScalableIconPanel sip = new ScalableIconPanel();
	JLabel text = new JLabel();
	private Dimension rendererMinSize = new Dimension( 40, 400 );

	public FileSystemTreeCellRenderer()
	{
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
		add( sip );
		add( text );

		setMinimumSize( rendererMinSize );
	}

	ScalableIcon folder = null;
	ScalableIcon folder_open = null;
	Dimension iconSize = new Dimension( 30, 30 );

	ScalableIcon getFolderIcon()
	{
		if (folder == null)
		{
			folder = IconFactory.getStdIcon( StdButtonType.FOLDER );
			folder.setMinimumSize( iconSize );
			folder.setSize( iconSize );

		}
		return folder;
	}

	ScalableIcon getOpenFolderIcon()
	{
		if (folder_open == null)
		{
			folder_open = IconFactory.getStdIcon( StdButtonType.FOLDER_OPEN );
			folder_open.setMinimumSize( iconSize );
			folder_open.setSize( iconSize );

		}
		return folder_open;
	}

	@Override
	public Component getTreeCellRendererComponent( JTree tree,
		Object value,
		boolean selected,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus )
	{
		if (value instanceof File)
		{
			File asFile = (File) value;
			text.setText( FileSystemTreeModel.getName( asFile ) );
			//jl.setText( asFile.getAbsolutePath() );
			// System.out.printf("%s\n",asFile.getAbsolutePath());
			//jl.setText( myFileView.getSystemDisplayName( asFile ) );
			// jl.setText( asFile.getName() );
			ScalableIcon ic = null;
			if (asFile.isDirectory())
			{
				ScalableIcon si = expanded ? getOpenFolderIcon()
					: getFolderIcon();

				ic = si;

			}
			else
			{
				ic = new ScalableImageIcon( myFileView.getSystemIcon( asFile ) );
			}
			sip.setIcon( ic );
			//Dimension iconSize = new Dimension( ic.getIconWidth(), ic.getIconWidth() );
			Dimension dMin = getMinimumSize();
			Insets i = getInsets();
			int fullHeight = iconSize.height + i.bottom + i.top;
			if (dMin.height < fullHeight)
			{
				dMin.height = 200;
				setMinimumSize( dMin );
				setSize( dMin );
			}
		}
		else
		{
			text.setText( "" );
			sip.setIcon( null );
		}

		if (background == null)
		{
			JRootPane root = tree.getRootPane();
			if (root != null)
			{
				Container con = root.getContentPane();
				background = con.getBackground();
				foreground = con.getForeground();
			}
		}

		setOpaque( selected );

		setBackground( selected ? foreground : background );
		setForeground( selected ? background : foreground );
		invalidate();
		return this;
	}
}