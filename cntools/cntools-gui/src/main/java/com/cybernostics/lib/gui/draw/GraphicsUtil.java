/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.gui.draw;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;

/**
 *
 * @author jasonw
 */
public class GraphicsUtil
{

	private GraphicsUtil()
	{
	}

	public static float getAlpha( Graphics2D g2 )
	{
		Composite c = g2.getComposite();

		if (c instanceof AlphaComposite)
		{
			return ( (AlphaComposite) c ).getAlpha();
		}
		return 1.0f;

	}

}
