package com.cybernostics.lib.svg.editor;

import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 *
 * @author jasonw
 */
public class MultiPointTool implements EditController
{

	protected Path2D.Float op_path = new Path2D.Float();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cybernostics.munchkinpaint.Drawable#addPoint(java.awt.Point)
	 */
	protected void addPoint( Point2D currentPoint )
	{
		Point2D lastPoint = op_path.getCurrentPoint();
		if (lastPoint == null)
		{
			op_path.moveTo(
				currentPoint.getX(),
				currentPoint.getY() );
		}
		else
		{
			if (!lastPoint.equals( currentPoint ))
			{
				op_path.lineTo(
					currentPoint.getX(),
					currentPoint.getY() );
			}
		}
	}

	protected void clear()
	{
		op_path.reset();
	}

	SVGDrawing theDrawing;

	@Override
	public void setDrawing( SVGDrawing theDrawing )
	{
		this.theDrawing = theDrawing;
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
	}

	@Override
	public void complete()
	{

	}

	PointScaler scaler = null;

	@Override
	public void setPointScaler( PointScaler scaler )
	{
		this.scaler = scaler;
	}
}
