package com.cybernostics.lib.gui.control;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.cybernostics.lib.support.Decrypter;

/**
 * Encrypted jlabel only decrypts when painting
 * 
 * @author jasonw
 * 
 */
public class CLabel extends JLabel
{

	private String aspiration = "";
	/**
	 * 
	 */
	private static final long serialVersionUID = 2779631610751184375L;

	public CLabel()
	{

	}

	public CLabel( String text )
	{
		super( text );
	}

	@Override
	public String getText()
	{
		String sEncr = super.getText();
		String output = sEncr;

		if (clearT)
		{
			Decrypter dc = new Decrypter( aspiration );
			output = dc.process( sEncr );
		}
		return output;
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		clearT = true;
		try
		{
			super.paintComponent( g );
		}
		finally
		{
			clearT = false;
		}
	}

	private boolean clearT = false;

	public void setAspiration( String aspiration )
	{
		this.aspiration = aspiration;
	}

	public String getAspiration()
	{
		return aspiration;
	}

}
