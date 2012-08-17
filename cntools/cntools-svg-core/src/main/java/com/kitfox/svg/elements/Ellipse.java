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
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Ellipse extends ShapeElement
{

	private CSSQuantityAttribute cx = new CSSQuantityAttribute( "cx" );

	public CSSQuantityAttribute getCx()
	{
		return cx;
	}

	public CSSQuantityAttribute getCy()
	{
		return cy;
	}

	public CSSQuantityAttribute getRx()
	{
		return rx;
	}

	public CSSQuantityAttribute getRy()
	{
		return ry;
	}

	private CSSQuantityAttribute cy = new CSSQuantityAttribute( "cy" );

	private CSSQuantityAttribute rx = new CSSQuantityAttribute( "rx" );

	private CSSQuantityAttribute ry = new CSSQuantityAttribute( "ry" );

	private boolean ellipseDirty = true;

	Ellipse2D.Float ellipse = new Ellipse2D.Float();

	/** Creates a new instance of Rect */
	public Ellipse()
	{
		addPresentationAttributes(
			cx,
			cy,
			rx,
			ry );
	}

	/*
	 protected void init(String idIn, Style parentStyle, String cx, String cy, String rx, String ry) {
	 super.init(idIn, parentStyle);

	 this.cx = parseDouble(cx);
	 this.cy = parseDouble(cy);
	 this.rx = parseDouble(rx);
	 this.ry = parseDouble(ry);

	 setBounds(this.cx - this.rx, this.cy - this.ry, this.rx * 2.0, this.ry * 2.0);
	 }
	 */
	/*
	public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	{
		//Load style string
	    super.loaderStartElement(helper, attrs, parent);

	    String cx = attrs.getValue("cx");
	    String cy = attrs.getValue("cy");
	    String rx = attrs.getValue("rx");
	    String ry = attrs.getValue("ry");

	    this.cx = XMLParseUtil.parseDouble(cx);
	    this.cy = XMLParseUtil.parseDouble(cy);
	    this.rx = XMLParseUtil.parseDouble(rx);
	    this.ry = XMLParseUtil.parseDouble(ry);

	    build();
	}
	 */

	/*
	public void loaderEndElement(SVGLoaderHelper helper)
	{
	    super.loaderEndElement(helper);

	    build();
	}
	 */
	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'c':
				if (addPresentationAttribute(
					name,
					value,
					cx,
					cy ))
				{
					return true;
				}
			case 'r':
				if (addPresentationAttribute(
					name,
					value,
					rx,
					ry ))
				{
					return true;
				}
		}

		return super.initAtttributeValue(
			name,
			value );

	}

	//    protected void build() throws SVGException
	//    {
	//        super.build();

	//        StyleAttribute sty = new StyleAttribute();
	//        
	//        if (getPres(sty.setName("cx"))) cx = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("cy"))) cy = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("rx"))) rx = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("ry"))) ry = sty.getFloatValueWithUnits();

	//ellipse.setFrame(cx - rx, cy - ry, rx * 2f, ry * 2f);
	//    }

	public Ellipse2D getEllipse()
	{
		if (ellipseDirty)
		{
			float ryf = ry.getNormalisedValue();
			float rxf = rx.getNormalisedValue();

			ellipse.setFrame(
				cx.getNormalisedValue() - rxf,
				cy.getNormalisedValue() - ryf,
				rxf * 2f,
				ryf * 2f );
		}
		return ellipse;
	}

	@Override
	public void render( Graphics2D g ) throws SVGException
	{
		beginLayer( g );
		renderShape(
			g,
			getEllipse() );
		finishLayer( g );
	}

	@Override
	public Shape getShape()
	{
		return shapeToParent( getEllipse() );
	}

	@Override
	public Rectangle2D getBoundingBox() throws SVGException
	{
		return boundsToParent( includeStrokeInBounds( ellipse.getBounds2D() ) );
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	@Override
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        if (trackManager.getNumTracks() == 0) return false;
		boolean changeState = super.updateTime( curTime );

		//Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        
		//        if (getPres(sty.setName("cx")))
		//        {
		//            float newCx = sty.getFloatValueWithUnits();
		//            if (newCx != cx)
		//            {
		//                cx = newCx;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("cy")))
		//        {
		//            float newCy = sty.getFloatValueWithUnits();
		//            if (newCy != cy)
		//            {
		//                cy = newCy;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("rx")))
		//        {
		//            float newRx = sty.getFloatValueWithUnits();
		//            if (newRx != rx)
		//            {
		//                rx = newRx;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("ry")))
		//        {
		//            float newRy = sty.getFloatValueWithUnits();
		//            if (newRy != ry)
		//            {
		//                ry = newRy;
		//                shapeChange = true;
		//            }
		//        }

		//        if (shapeChange)
		//        {
		//            build();
		////            ellipse.setFrame(cx - rx, cy - ry, rx * 2f, ry * 2f);
		////            return true;
		//        }

		return changeState;//|| shapeChange;
	}
}
