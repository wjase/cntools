package com.cybernostics.lib.svg.editor;

import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jasonw
 */
abstract public class TwoPointTool implements EditController
{

	protected SVGDrawing theDrawing;
	protected Line2D.Float theLine = new Line2D.Float();
	protected Point2D first = new Point2D.Double();
	protected Point2D second = new Point2D.Double();
	protected int clicks = 0;
	protected boolean dragged = false;

	private void showEvent( MouseEvent e, String sLabel )
	{
		System.out.println( String.format(
			"%s at %d,%d",
			sLabel,
			e.getX(),
			e.getY() ) );
	}

	@Override
	public void setDrawing( SVGDrawing theDrawing )
	{
		this.theDrawing = theDrawing;
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		++clicks;

		//System.out.println( String.format( "Clicks %d", clicks) );

		switch (clicks)
		{
			case 1:
				//updatePoint( first, e );
				break;
			case 2:
				publishBoundsUpdate( e );
				complete();
				clicks = 0;
				break;
			default:
				clicks = 0;

		}

	}

	private void updatePoint( Point2D toSet, MouseEvent e )
	{
		toSet.setLocation(
			scaler.scaleX( e.getX() ),
			scaler.scaleY( e.getY() ) );
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		if (clicks == 0)
		{
			updatePoint(
				first,
				e );
		}
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		publishBoundsUpdate( e );

		if (dragged)
		{
			complete();
			dragged = false;
		}
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		cancelDraw();
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		publishBoundsUpdate( e );
		dragged = true;
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		if (clicks == 1)
		{
			publishBoundsUpdate( e );
		}
	}

	protected PointScaler scaler;

	@Override
	public void setPointScaler( PointScaler scaler )
	{
		this.scaler = scaler;
	}

	private void publishBoundsUpdate( MouseEvent e )
	{
		updatePoint(
			second,
			e );
		theLine.setLine(
			first,
			second );
		updateBounds( theLine.getBounds2D() );
	}

	abstract protected void updateBounds( Rectangle2D bounds2D );

	abstract protected void cancelDraw();
}
