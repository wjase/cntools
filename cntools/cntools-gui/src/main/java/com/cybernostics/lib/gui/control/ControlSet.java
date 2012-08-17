package com.cybernostics.lib.gui.control;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;

/**
 * Implements Named Groups of Controls
 * @author jasonw
 *
 */
public class ControlSet
{
	private static Map< String, ArrayList< JComponent >> namedSets = new TreeMap< String, ArrayList< JComponent >>();

	public static void addToGroup( String groupName, JComponent compToAdd )
		throws Exception
	{
		if (compToAdd.getName() == null)
		{
			throw new Exception( "Must name components added to control sets." );
		}

		ArrayList< JComponent > thisSet = getNamedGroup( groupName );
		thisSet.add( compToAdd );
	}

	public static ArrayList< JComponent > getNamedGroup( String groupName )
	{
		ArrayList< JComponent > thisSet = namedSets.get( groupName );

		if (thisSet == null)
		{
			thisSet = new ArrayList< JComponent >();
			namedSets.put(
				groupName,
				thisSet );
		}

		return thisSet;
	}

	public static void setVisible( String groupName, boolean showFlag )
	{
		ArrayList< JComponent > thisSet = getNamedGroup( groupName );

		for (JComponent eachComponent : thisSet)
		{
			eachComponent.setVisible( showFlag );
		}
	}

}
