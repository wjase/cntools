package com.cybernostics.lib.gui.layout;

import javax.swing.Box;
import javax.swing.JComponent;

/**
 *
 * @author jasonw
 */
public class BoxUtils
{

	public static void addAll( JComponent parent, JComponent... children )
	{
		for (JComponent eachChild : children)
		{
			parent.add( eachChild );
			parent.add( Box.createHorizontalStrut( 5 ) );
		}
	}

	public static JComponent createRelativeStrut( double horiz, double vert )
	{
		return new RelativeBox( horiz, vert );
	}
}
