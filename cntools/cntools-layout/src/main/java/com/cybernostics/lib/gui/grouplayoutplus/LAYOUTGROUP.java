package com.cybernostics.lib.gui.grouplayoutplus;

import java.awt.Component;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.GroupLayout.Alignment;

public class LAYOUTGROUP extends JComponent
{

	private Component[] comps;
	private Alignment alignment;
	private boolean linkSizes = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7849980930604282742L;

	protected LAYOUTGROUP(
		Alignment alignment,
		boolean linkSizes,
		Component... comps )
	{
		this.setComps( comps );
		this.setAlignment( alignment );
		this.setLinkSizes( linkSizes );
	}

	public void addToGroup( Component... comps )
	{
		Component[] mergedComps = Arrays.copyOf(
			this.comps,
			comps.length + this.comps.length );
		int offset = this.comps.length;
		for (int index = 0; index < comps.length; ++index)
		{
			mergedComps[ index + offset ] = comps[ index ];
		}

		this.comps = mergedComps;
	}

	public Alignment getAlignment()
	{
		return alignment;
	}

	public Component[] getComps()
	{
		return comps;
	}

	public boolean isLinkSizes()
	{
		return linkSizes;
	}

	private void setAlignment( Alignment alignment )
	{
		this.alignment = alignment;
	}

	private void setComps( Component[] comps )
	{
		this.comps = comps;
	}

	private void setLinkSizes( boolean linkSizes )
	{
		this.linkSizes = linkSizes;
	}
}
