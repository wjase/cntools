package com.cybernostics.lib.svg.editor;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author jasonw
 */
public interface EditController extends MouseListener, MouseMotionListener
{
	public void setDrawing( SVGDrawing theDrawing );

	/**
	 * This is called to flag an edit action as completed.
	 * If the esc key is pressed or the complete button.
	 */
	public void complete();

	public void setPointScaler( PointScaler scaler );
}
