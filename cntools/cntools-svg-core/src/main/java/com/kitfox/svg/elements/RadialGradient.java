/*
 * RadialGradient.java
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
 * Created on January 26, 2004, 1:55 AM
 */

package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.CSSQuantityDefaultConstant;
import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class RadialGradient extends Gradient
{

	CSSQuantityAttribute cx = new CSSQuantityAttribute( "cx" );//0.5f;
	CSSQuantityAttribute cy = new CSSQuantityAttribute( "cy" );//0.5f;
	CSSQuantityAttribute fx = new CSSQuantityAttribute( "fx" );//0.5f;
	CSSQuantityAttribute fy = new CSSQuantityAttribute( "cy" );//0.5f;
	CSSQuantityAttribute r = new CSSQuantityAttribute( "r" );//0.5f;

	/** Creates a new instance of RadialGradient */
	public RadialGradient()
	{
		CSSQuantityDefaultConstant pointFive = new CSSQuantityDefaultConstant( 0.5f );
		cx.setDefault( pointFive );
		cy.setDefault( pointFive );
		fx.setDefault( pointFive );
		fy.setDefault( pointFive );
		r.setDefault( pointFive );

	}

	/*
	 public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 {
	 //Load style string
	 super.loaderStartElement(helper, attrs, parent);

	 String cx = attrs.getValue("cx");
	 String cy = attrs.getValue("cy");
	 String fx = attrs.getValue("fx");
	 String fy = attrs.getValue("fy");
	 String r = attrs.getValue("r");

	 if (cx != null) this.cx = (FloatUnitAttribute)XMLParseUtil.parseRatio(cx);
	 if (cy != null) this.cy = (FloatUnitAttribute)XMLParseUtil.parseRatio(cy);
	 if (fx != null) this.fx = (FloatUnitAttribute)XMLParseUtil.parseRatio(fx);
	 if (fy != null) this.fy = (FloatUnitAttribute)XMLParseUtil.parseRatio(fy);
	 if (r != null) this.r = (FloatUnitAttribute)XMLParseUtil.parseRatio(r);
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
			case 'f':
				if (addPresentationAttribute(
					name,
					value,
					fx,
					fy ))
				{
					return true;
				}
			case 'r':
				if (addPresentationAttribute(
					name,
					value,
					r ))
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
	//        
	//        StyleAttribute sty = new StyleAttribute();
	//        
	//        if (getPres(sty.setName("cx"))) cx = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("cy"))) cy = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("fx"))) fx = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("fy"))) fy = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("r"))) r = sty.getFloatValueWithUnits();
	//    }

	public Paint getPaint( Rectangle2D bounds, AffineTransform xform )
	{
		com.kitfox.svg.batik.MultipleGradientPaint.CycleMethodEnum method;
		switch (spreadMethod.getIntValue())
		{
			default:
			case SM_PAD:
				method = com.kitfox.svg.batik.MultipleGradientPaint.NO_CYCLE;
				break;
			case SM_REPEAT:
				method = com.kitfox.svg.batik.MultipleGradientPaint.REPEAT;
				break;
			case SM_REFLECT:
				method = com.kitfox.svg.batik.MultipleGradientPaint.REFLECT;
				break;
		}

		com.kitfox.svg.batik.RadialGradientPaint paint;

		if (gradientUnits.getIntValue() == GU_USER_SPACE_ON_USE)
		{
			paint = new com.kitfox.svg.batik.RadialGradientPaint(
				new Point2D.Float( cx.getNormalisedValue(),
					cy.getNormalisedValue() ),
				r.getNormalisedValue(),
				new Point2D.Float( fx.getNormalisedValue(),
					fy.getNormalisedValue() ),
				getStopFractions(),
				getStopColors(),
				method,
				com.kitfox.svg.batik.MultipleGradientPaint.SRGB,
				gradientTransform.getXform() );
		}
		else
		{
			AffineTransform viewXform = new AffineTransform();
			viewXform.translate(
				bounds.getX(),
				bounds.getY() );
			viewXform.scale(
				bounds.getWidth(),
				bounds.getHeight() );

			viewXform.concatenate( gradientTransform.getXform() );

			Color[] stopColorSteps = getStopColors();

			if (stopColorSteps.length == 0)
			{
				throw new RuntimeException( "no stop colors " );
			}

			paint = new com.kitfox.svg.batik.RadialGradientPaint(
				new Point2D.Float( cx.getNormalisedValue(),
					cy.getNormalisedValue() ),
				r.getNormalisedValue(),
				new Point2D.Float( fx.getNormalisedValue(),
					fy.getNormalisedValue() ),
				getStopFractions(),
				stopColorSteps,
				method,
				com.kitfox.svg.batik.MultipleGradientPaint.SRGB,
				viewXform );
		}

		return paint;
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		////        if (trackManager.getNumTracks() == 0) return false;
		//        boolean changeState = super.updateTime(curTime);
		//
		//        //Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        
		//        if (getPres(sty.setName("cx")))
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if (newVal != cx)
		//            {
		//                cx = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("cy")))
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if (newVal != cy)
		//            {
		//                cy = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("fx")))
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if (newVal != fx)
		//            {
		//                fx = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("fy")))
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if (newVal != fy)
		//            {
		//                fy = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("r")))
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if (newVal != r)
		//            {
		//                r = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        return changeState;
		return false;
	}
}
