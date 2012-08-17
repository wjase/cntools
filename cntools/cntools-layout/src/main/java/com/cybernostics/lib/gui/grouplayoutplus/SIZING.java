package com.cybernostics.lib.gui.grouplayoutplus;

import java.awt.Component;

import javax.swing.JComponent;

public class SIZING extends JComponent
{

	public static JComponent fixed( Component toAdd )
	{
		return new SIZING( toAdd,
			javax.swing.GroupLayout.DEFAULT_SIZE,
			javax.swing.GroupLayout.PREFERRED_SIZE,
			javax.swing.GroupLayout.DEFAULT_SIZE );

	}

	private int min;
	private int preferred;
	private int max;

	private Component toAdd;

	/**
	 * 
	 */
	private static final long serialVersionUID = 2172418368234824213L;

	public static JComponent add( Component toAdd,
		int min,
		int preferred,
		int max )
	{
		return new SIZING( toAdd, min, preferred, max );

	}

	public static JComponent fill( Component toAdd )
	{
		return new SIZING( toAdd,
			0,
			javax.swing.GroupLayout.PREFERRED_SIZE,
			Short.MAX_VALUE );

	}

	private SIZING( Component toAdd, int min, int preferred, int max )
	{
		this.setMin( min );
		this.setMax( max );
		this.setPreferred( preferred );
		this.setToAdd( toAdd );
	}

	public int getMax()
	{
		return max;
	}

	public int getMin()
	{
		return min;
	}

	public int getPreferred()
	{
		return preferred;
	}

	public Component getToAdd()
	{
		return toAdd;
	}

	private void setMax( int max )
	{
		this.max = max;
	}

	private void setMin( int min )
	{
		this.min = min;
	}

	private void setPreferred( int preferred )
	{
		this.preferred = preferred;
	}

	private void setToAdd( Component toAdd )
	{
		this.toAdd = toAdd;
	}
}
