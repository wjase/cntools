package com.cybernostics.lib.svg.editor;

import java.awt.Color;

/**
 *
 * @author jasonw
 */
public class ColouredSVGDrawingBackground implements SVGDrawingBackground
{
	private Color current = null;

	public Color getCurrent()
	{
		return current;
	}

	public void setCurrent( Color current )
	{
		this.current = current;
	}

	@Override
	public void apply( SVGDrawing toChange )
	{
		toChange.setPaperColor( current );
	}

}
