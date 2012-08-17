package com.cybernostics.lib.svg.editor;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Used to scale pixel point into user space point 
 * from 0.0 to 1.0 for width and height
 * @author jasonw
 */
public interface PointScaler
{

	Point2D getPoint( Point p );

	double scaleX( int x );

	double scaleY( int y );

}
