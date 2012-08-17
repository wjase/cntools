/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.IMutablePath;
import java.awt.Shape;

/**
 * Attribute for handling paths
 * @author jasonw
 */
public class Path2DAttribute extends NamedAttribute implements IMutablePath
{

	private boolean set = false;

	@Override
	public boolean isSet()
	{
		return set;
	}

	@Override
	public void quadTo( double x1, double y1, double x2, double y2 )
	{
		set = true;
		thePath.quadTo(
			x1,
			y1,
			x2,
			y2 );
	}

	private SVGPath2D thePath = new SVGPath2D.Float();

	public Shape getShape()
	{
		return thePath;
	}

	public Path2DAttribute( String name )
	{
		super( name );
	}

	public void smoothQuadToRelative( double x1, double y1 )
	{
		set = true;
		thePath.smoothQuadToRelative(
			x1,
			y1 );
	}

	public void smoothQuadTo( double x1, double y1 )
	{
		set = true;
		thePath.smoothQuadTo(
			x1,
			y1 );
	}

	public void smoothCurveToRelative( double ctrl2X,
		double ctrl2Y,
		double toX,
		double toY )
	{
		set = true;
		thePath.smoothCurveToRelative(
			ctrl2X,
			ctrl2Y,
			toX,
			toY );
	}

	public void smoothCurveTo( double ctrl2X,
		double ctrl2Y,
		double toX,
		double toY )
	{
		set = true;
		thePath.smoothCurveTo(
			ctrl2X,
			ctrl2Y,
			toX,
			toY );
	}

	@Override
	public String getStringValue()
	{
		return thePath != null ? thePath.asSVGString() : "";
	}

	@Override
	public void setStringValue( String value )
	{
		set = true;
		thePath = new SVGPath2D.Double();
		thePath.parseSVG( value );
	}

	@Override
	public void quadToRelative( double x1, double y1, double x2, double y2 )
	{
		set = true;
		thePath.quadToRelative(
			x1,
			y1,
			x2,
			y2 );
	}

	@Override
	public void moveToRelative( double x, double y )
	{
		set = true;
		thePath.moveToRelative(
			x,
			y );
	}

	@Override
	public void lineToRelative( double x, double y )
	{
		set = true;
		thePath.lineToRelative(
			x,
			y );
	}

	@Override
	public void curveToRelative( double x1,
		double y1,
		double x2,
		double y2,
		double x3,
		double y3 )
	{
		set = true;
		thePath.curveToRelative(
			x1,
			y1,
			x2,
			y2,
			x3,
			y3 );
	}

	@Override
	public void moveTo( double x, double y )
	{
		set = true;
		thePath.moveTo(
			x,
			y );
	}

	@Override
	public void lineTo( double x, double y )
	{
		set = true;
		thePath.lineTo(
			x,
			y );
	}

	@Override
	public synchronized void closePath()
	{
		set = true;
		thePath.closePath();
	}

	public void arcToRelative( float rx,
		float ry,
		float angle,
		boolean largeArcFlag,
		boolean sweepFlag,
		float x,
								float y )
	{
		set = true;
		thePath.arcToRelative(
			rx,
			ry,
			angle,
			largeArcFlag,
			sweepFlag,
			x,
			y );
	}

	public void arcTo( float rx,
		float ry,
		float angle,
		boolean largeArcFlag,
		boolean sweepFlag,
		float x,
		float y )
	{
		set = true;
		thePath.arcTo(
			rx,
			ry,
			angle,
			largeArcFlag,
			sweepFlag,
			x,
			y );
	}

	@Override
	public void append( Shape s, boolean connect )
	{
		set = true;
		thePath.append(
			s,
			connect );
	}

	@Override
	public void curveTo( double x1,
		double y1,
		double x2,
		double y2,
		double x3,
		double y3 )
	{
		set = true;
		thePath.curveTo(
			x1,
			y1,
			x2,
			y2,
			x3,
			y3 );
	}

	public void setWindingRule( int rule )
	{
		set = true;
		thePath.setWindingRule( rule );
	}

}
