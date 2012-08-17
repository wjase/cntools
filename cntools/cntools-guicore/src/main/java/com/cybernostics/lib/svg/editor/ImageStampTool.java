package com.cybernostics.lib.svg.editor;

import com.kitfox.svg.elements.SVGElement;
import java.awt.geom.Rectangle2D;
import java.net.URL;

/**
 * Maintains edit state for a drawing
 * @author jasonw
 */
public class ImageStampTool extends TwoPointTool
{

	protected SVGElement currentElement = null;
	protected URL stampURL = null;

	public URL getStampURL()
	{
		return stampURL;
	}

	public void setStampURL( URL stampURL )
	{
		this.stampURL = stampURL;
	}

	@Override
	protected void updateBounds( Rectangle2D bounds2D )
	{
		if (stampURL != null)
		{
			if (currentElement == null)
			{
				currentElement = theDrawing.createBitmapRef(
					stampURL,
					bounds2D );
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
