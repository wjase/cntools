package com.cybernostics.lib.gui.grouplayoutplus;

import java.awt.Component;

import javax.swing.GroupLayout.Alignment;

public class SEQUENTIAL extends LAYOUTGROUP
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7849980930604282742L;

	public SEQUENTIAL( Alignment leading, boolean linked, Component[] comps )
	{
		super( leading, linked, comps );
	}

	public SEQUENTIAL( Alignment leading, Component[] comps )
	{
		super( leading, false, comps );
	}

	public static LAYOUTGROUP group( Component... comps )
	{
		LAYOUTGROUP newGroup = new SEQUENTIAL( Alignment.LEADING, comps );

		return newGroup;

	}

	public static LAYOUTGROUP groupLinked( Component... comps )
	{
		LAYOUTGROUP newGroup = new SEQUENTIAL( Alignment.LEADING, true, comps );

		return newGroup;

	}

	public static LAYOUTGROUP group( Alignment alignment, Component... comps )
	{
		LAYOUTGROUP newGroup = new SEQUENTIAL( alignment, comps );

		return newGroup;

	}

}
