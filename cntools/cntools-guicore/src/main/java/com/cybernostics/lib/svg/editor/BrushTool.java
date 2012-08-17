package com.cybernostics.lib.svg.editor;

import com.kitfox.svg.elements.Path;
import com.kitfox.svg.elements.SVGElement;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class BrushTool extends MultiPointTool
{

	Path currentObject = null;

	@Override
	public void mousePressed( MouseEvent e )
	{
		Point2D nextPoint = scaler.getPoint( e.getPoint() );
		addPoint( nextPoint );
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		Point2D nextPoint = scaler.getPoint( e.getPoint() );

		if (currentObject == null)
		{
			currentObject = theDrawing.createShape(
				op_path,
				false,
				true );
			theDrawing.addElement(
				currentObject,
				null );

			//theDrawing.setAttribute( currentObject, "d", theDrawing.getSVGPathSpec( op_path ) );
		}
		else
		{
			theDrawing.addPathPoint(
				currentObject,
				nextPoint );
		}
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		complete();
	}

	@Override
	public void complete()
	{
		currentObject = null;
		op_path.reset();
	}

}
