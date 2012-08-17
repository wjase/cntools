package com.cybernostics.examples.lib.gui.autocomplete;

import com.cybernostics.lib.gui.autocomplete.FileSystemTreeModel;
import java.io.File;

class FileSystemTreeModelExample
{

	public static void main( String[] args )
	{

		FileSystemTreeModel fst = new FileSystemTreeModel();

		Object oRoot = fst.getRoot();
		int count = fst.getChildCount( oRoot );
		for (int index = 0; index < count; ++index)
		{
            FileSystemTreeModel.getDisplayName((File) fst.getChild( oRoot, index ));
		}
	}
}
