package com.cybernostics.lib.persist.xml;

import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Wraps a PAth2D.Float object in a jaxb xml-aware class. Restrictions: only
 * wraps line segments assumes one move to at the start
 * 
 * @author jasonw
 * 
 */
@XmlJavaTypeAdapter(value = PathAdapter.class, type = Path2D.Float.class)
@XmlRootElement
@XmlType(name = "point")
public class PathFloat extends ShapeFloat
{

	public static Path2D.Float toPath2D( PathFloat pf )
	{
		Path2D.Float ptf = new Path2D.Float();
		ArrayList< PointFloat > points = pf.getPoints();
		PointFloat eachPoint = points.get( 0 );
		ptf.moveTo(
			eachPoint.getX(),
			eachPoint.getY() );

		for (int index = 1; index < points.size(); ++index) // skip the first
		// point
		{
			eachPoint = points.get( index );
			ptf.lineTo(
				eachPoint.getX(),
				eachPoint.getY() );
		}

		return ptf;
	}

	private ArrayList< PointFloat > points = new ArrayList< PointFloat >();

	public PathFloat()
	{

	}

	public PathFloat( Path2D.Float pf )
	{
		double[] coords =
		{ 0, 0, 0, 0, 0, 0 };
		PathIterator pit = pf.getPathIterator( null );
		while (!pit.isDone())
		{
			pit.currentSegment( coords );
			points.add( new PointFloat( (float) coords[ 0 ], (float) coords[ 1 ] ) );
			pit.next();

		}
	}

	@XmlElementWrapper
	// @XmlJavaTypeAdapter(value = PathAdapter.class, type = Path2D.Float.class)
	// @XmlElement
	@XmlElementRef(name = "point", type = PointFloat.class)
	public ArrayList< PointFloat > getPoints()
	{
		return points;
	}

	public void setPoints( ArrayList< PointFloat > points )
	{
		this.points = points;
	}
}
