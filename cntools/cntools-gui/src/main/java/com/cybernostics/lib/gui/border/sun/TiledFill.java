package com.cybernostics.lib.gui.border.sun;

/*
 * Copyright 2002 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL EXPRESS OR IMPLIED
 * CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS
 * SHALL NOT BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR ITS DERIVATIVES. IN NO EVENT
 * WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS
 * OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF
 * SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for use in the design,
 * construction, operation or maintenance of any nuclear facility.
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

public class TiledFill extends Fill
{
	private Fill tile;
	private int tileWidth;
	private int tileHeight;

	public TiledFill( Fill tile, int tileWidth, int tileHeight )
	{
		this.tile = tile;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public TiledFill()
	{
		this.tile = null;
		this.tileWidth = -1;
		this.tileHeight = -1;
	}

	public void setTileWidth( int tileWidth )
	{
		// assert tileWidth > 0
		this.tileWidth = tileWidth;
	}

	public int getTileWidth()
	{
		return tileWidth;
	}

	public void setTileHeight( int tileHeight )
	{
		// assert tileHeight > 0
		this.tileHeight = tileHeight;
	}

	public int getTileHeight()
	{
		return tileHeight;
	}

	public void setTile( Fill tile )
	{
		this.tile = tile;
	}

	public Fill getTile()
	{
		return tile;
	}

	public void paintFill( Component c, Graphics g, Rectangle r )
	{
		int x = r.x, y = r.y, w = r.width, h = r.height;
		int tileWidth = getTileWidth();
		int tileHeight = getTileHeight();
		if (tile != null)
		{
			Graphics clippedG = g.create(
				x,
				y,
				w,
				h );
			Rectangle tr = new Rectangle( tileWidth, tileHeight );
			for (tr.x = 0; tr.x < w; tr.x += tileWidth)
			{
				for (tr.y = 0; tr.y < h; tr.y += tileHeight)
				{
					tile.paintFill(
						c,
						clippedG,
						tr );
				}
			}
			clippedG.dispose();
		}
	}
}
