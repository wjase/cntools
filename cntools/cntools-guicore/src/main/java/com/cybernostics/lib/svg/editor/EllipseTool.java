package com.cybernostics.lib.svg.editor;

import com.kitfox.svg.elements.Ellipse;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Maintains edit state for a drawing
 * @author jasonw
 */
public class EllipseTool extends TwoPointTool
{

	Ellipse currentElement = null;

	@Override
	protected void updateBounds( Rectangle2D bounds2D )
	{
		if (currentElement == null)
		{

			Ellipse2D e2 = new Ellipse2D.Double();
			e2.setFrame(
				bounds2D.getMinX(),
				bounds2D.getMinY(),
				bounds2D.getWidth(),
				bounds2D.getHeight() );
			currentElement = theDrawing.createEllipse(
				e2,
				theDrawing.isFilled(),
				theDrawing.isStroked() );
			theDrawing.addElement(
				currentElement,
				null );
			theDrawing.update( currentElement );
		}
		else
		{
			theDrawing.updateEllipseBounds(
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
