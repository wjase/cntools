package com.cybernostics.lib.svg.editor;

import com.kitfox.svg.elements.SVGElement;
import java.awt.geom.Rectangle2D;

/**
 * Maintains edit state for a drawing
 * @author jasonw
 */
public class RectangleTool extends TwoPointTool
{

	SVGElement currentElement = null;

	@Override
	protected void updateBounds( Rectangle2D bounds2D )
	{
		if (currentElement == null)
		{
			currentElement = theDrawing.createRect(
				bounds2D,
				theDrawing.isFilled(),
				theDrawing.isStroked() );
			theDrawing.addElement(
				currentElement,
				null );
			theDrawing.update( currentElement );
		}
		else
		{
			theDrawing.updateBounds(
				currentElement,
				bounds2D );
			theDrawing.update( currentElement );
		}
	}

	@Override
	public void complete()
	{
		// unlink from editing
		currentElement = null;
	}

	@Override
	protected void cancelDraw()
	{
		theDrawing.remove( currentElement );
		currentElement = null;
	}

}
