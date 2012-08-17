/*
 * Rect.java
 * 
 * 
 * The Salamander Project - 2D and 3D graphics libraries in Java Copyright (C) 2004 Mark McKay
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA
 * 
 * Mark McKay can be contacted at mark@kitfox.com. Salamander and other projects can be found at
 * http://www.kitfox.com
 * 
 * Created on January 26, 2004, 5:25 PM
 */

package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Line extends ShapeElement
{

	CSSQuantityAttribute x1 = new CSSQuantityAttribute( "x1" );
	CSSQuantityAttribute y1 = new CSSQuantityAttribute( "y1" );
	CSSQuantityAttribute x2 = new CSSQuantityAttribute( "x2" );
	CSSQuantityAttribute y2 = new CSSQuantityAttribute( "y2" );

	Line2D.Float line = new Line2D.Float();

	//    RectangularShape rect;

	/** Creates a new instance of Rect */
	public Line()
	{
		addPresentationAttributes(
			x1,
			y1,
			x2,
			y2 );
	}

	@Override
	public void attributeUpdated( IStringAttribute source )
	{
		if (source == x1)
		{
			line.x1 = x1.getNormalisedValue();
			return;
		}
		if (source == y1)
		{
			line.y1 = y1.getNormalisedValue();
			return;
		}
		if (source == x2)
		{
			line.x2 = x2.getNormalisedValue();
			return;
		}
		if (source == y2)
		{
			line.y2 = y2.getNormalisedValue();
			return;
		}
		super.attributeUpdated( source );
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'x':
				if (addPresentationAttribute(
					name,
					value,
					x1,
					x2 ))
				{
					return true;
				}
			case 'y':
				if (addPresentationAttribute(
					name,
					value,
					y1,
					y2 ))
				{
					return true;
				}
		}
		return super.initAtttributeValue(
			name,
			value );
	}

	/*
	public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	{
		//Load style string
	    super.loaderStartElement(helper, attrs, parent);

	    String x1 = attrs.getValue("x1");
	    String y1 = attrs.getValue("y1");
	    String x2 = attrs.getValue("x2");
	    String y2 = attrs.getValue("y2");

	    this.x1 = XMLParseUtil.parseFloat(x1);
	    this.y1 = XMLParseUtil.parseFloat(y1);
	    this.x2 = XMLParseUtil.parseFloat(x2);
	    this.y2 = XMLParseUtil.parseFloat(y2);

	    build();
	}
	 */

	public void render( Graphics2D g ) throws SVGException
	{
		beginLayer( g );
		renderShape(
			g,
			line );
		finishLayer( g );
	}

	public Shape getShape()
	{
		return shapeToParent( line );
	}

	public Rectangle2D getBoundingBox() throws SVGException
	{
		return boundsToParent( includeStrokeInBounds( line.getBounds2D() ) );
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        if (trackManager.getNumTracks() == 0) return false;
		boolean changeState = super.updateTime( curTime );

		//Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        
		//        if (getPres(sty.setName("x1")))
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if (newVal != x1)
		//            {
		//                x1 = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("y1")))
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if (newVal != y1)
		//            {
		//                y1 = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("x2")))
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if (newVal != x2)
		//            {
		//                x2 = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("y2")))
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if (newVal != y2)
		//            {
		//                y2 = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (shapeChange)
		//        {
		//            build();
		////            line = new Line2D.Float(x1, y1, x2, y2);
		////            return true;
		//        }

		return changeState;//|| shapeChange;
	}
}
