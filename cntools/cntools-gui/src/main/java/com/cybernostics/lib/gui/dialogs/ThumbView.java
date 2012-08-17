package com.cybernostics.lib.gui.dialogs;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.metal.MetalIconFactory;

// ThumbNailFileView.java
// A simple implementation of the FileView class that provides a 16x16 image of
// each GIF or JPG file for its icon. This could be SLOW for large images, as we
// simply load the real image and then scale it.
//

class ThumbNailFileView extends FileView
{

	private Icon fileIcon = MetalIconFactory.getTreeLeafIcon();

	private Icon folderIcon = MetalIconFactory.getTreeFolderIcon();

	private Component observer;

	public ThumbNailFileView( Component c )
	{
		// We need a component around to create our icon's image
		observer = c;
	}

	@Override
	public String getDescription( File f )
	{
		// We won't store individual descriptions, so just return the
		// type description.
		return getTypeDescription( f );
	}

	@Override
	public Icon getIcon( File f )
	{
		// Is it a folder?
		if (f.isDirectory())
		{
			return folderIcon;
		}

		// Ok, it's a file, so return a custom icon if it's an image file
		String name = f.getName()
			.toLowerCase();
		if (name.endsWith( ".jpg" ) || name.endsWith( ".gif" )
			|| name.endsWith( ".png" ))
		{
			return new Icon16( f.getAbsolutePath() );
		}

		// Return the generic file icon if it's not
		return fileIcon;
	}

	@Override
	public String getName( File f )
	{
		String name = f.getName();
		return name.equals( "" ) ? f.getPath() : name;
	}

	@Override
	public String getTypeDescription( File f )
	{
		String name = f.getName()
			.toLowerCase();
		if (f.isDirectory())
		{
			return "Folder";
		}
		if (name.endsWith( ".jpg" ))
		{
			return "JPEG Image";
		}
		if (name.endsWith( ".gif" ))
		{
			return "GIF Image";
		}
		return "Generic File";
	}

	@Override
	public Boolean isTraversable( File f )
	{
		// We'll mark all directories as traversable
		return f.isDirectory() ? Boolean.TRUE : Boolean.FALSE;
	}

	public class Icon16 extends ImageIcon
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 9049940437364870334L;

		public Icon16( String f )
		{
			super( f );
			Image i = observer.createImage(
				16,
				16 );
			i.getGraphics()
				.drawImage(
					getImage(),
					0,
					0,
					16,
					16,
					observer );
			setImage( i );
		}

		@Override
		public int getIconHeight()
		{
			return 16;
		}

		@Override
		public int getIconWidth()
		{
			return 16;
		}

		@Override
		public void paintIcon( Component c, Graphics g, int x, int y )
		{
			g.drawImage(
				getImage(),
				x,
				y,
				c );
		}
	}
}