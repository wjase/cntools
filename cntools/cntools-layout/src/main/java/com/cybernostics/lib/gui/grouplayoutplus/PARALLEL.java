package com.cybernostics.lib.gui.grouplayoutplus;

import java.awt.Component;

import javax.swing.GroupLayout.Alignment;

public class PARALLEL extends LAYOUTGROUP
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7849980930604282742L;

	public PARALLEL( Alignment leading, boolean linked, Component[] comps )
	{
		super( leading, linked, comps );
	}

	public PARALLEL( Alignment leading, Component[] comps )
	{
		super( leading, false, comps );
	}

	public static LAYOUTGROUP groupLinked( Component... comps )
	{
		LAYOUTGROUP newGroup = new PARALLEL( Alignment.LEADING, true, comps );

		return newGroup;

	}

	public static LAYOUTGROUP group( Component... comps )
	{
		LAYOUTGROUP newGroup = new PARALLEL( Alignment.LEADING, comps );

		return newGroup;

	}

	public static LAYOUTGROUP group( Alignment alignment, Component... comps )
	{
		LAYOUTGROUP newGroup = new PARALLEL( alignment, comps );

		return newGroup;

	}

	public static Component groupLinked( Alignment trailing, Component... comps )
	{
		LAYOUTGROUP newGroup = new PARALLEL( Alignment.LEADING, true, comps );

		return newGroup;
	}

}
