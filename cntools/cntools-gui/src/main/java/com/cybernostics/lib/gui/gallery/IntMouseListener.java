package com.cybernostics.lib.gui.gallery;

import java.awt.event.MouseAdapter;

public class IntMouseListener extends MouseAdapter
{

	int itemToNotify;

	public IntMouseListener( int itemNumber )
	{
		this.itemToNotify = itemNumber;
	}

}
