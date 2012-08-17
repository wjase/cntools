/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg;

import java.awt.Shape;

/**
 *
 * @author jasonw
 */
public interface IMutablePath
{

	public void quadTo( double x1, double y1, double x2, double y2 );

	public void quadToRelative( double x1, double y1, double x2, double y2 );

	public void smoothQuadTo( double x1, double y1 );

	public void smoothQuadToRelative( double x1, double y1 );

	public void moveTo( double x, double y );

	public void moveToRelative( double x, double y );

	public void lineTo( double x, double y );

	public void lineToRelative( double x, double y );

	public void curveTo( double x1,
		double y1,
		double x2,
		double y2,
		double x3,
		double y3 );

	public void curveToRelative( double x1,
		double y1,
		double x2,
		double y2,
		double x3,
		double y3 );

	public void smoothCurveTo( double ctrl2X,
		double ctrl2Y,
		double toX,
		double toY );

	public void smoothCurveToRelative( double ctrl2X,
		double ctrl2Y,
		double toX,
		double toY );

	public void append( Shape s, boolean connect );

	public void closePath();

}
