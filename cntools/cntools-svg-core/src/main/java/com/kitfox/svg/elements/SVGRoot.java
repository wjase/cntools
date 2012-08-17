/*
 * SVGRoot.java
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
 * Created on February 18, 2004, 5:33 PM
 */

package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.CSSUnits;
import com.kitfox.svg.xml.attributes.AspectRatioAttribute;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.HasBounds;
import com.kitfox.svg.xml.attributes.RectangleAttribute;
import com.kitfox.svg.xml.attributes.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * The root element of an SVG tree.
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class SVGRoot extends Group implements HasBounds
{
	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );
	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );
	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );
	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	private RectangleAttribute viewBox = new RectangleAttribute( "viewbox" );

	//    final Rectangle2D.Float viewBox = new Rectangle2D.Float();

	public CSSQuantityAttribute getHeight()
	{
		return height;
	}

	public CSSQuantityAttribute getWidth()
	{
		return width;
	}

	public CSSQuantityAttribute getX()
	{
		return x;
	}

	public CSSQuantityAttribute getY()
	{
		return y;
	}

	AspectRatioAttribute aspectRatio = new AspectRatioAttribute( "preserveAspectRatio" );

	final AffineTransform viewXform = new AffineTransform();
	final Rectangle2D.Float clipRect = new Rectangle2D.Float();

	/** Creates a new instance of SVGRoot */
	public SVGRoot()
	{
		addPresentationAttributes(
			x,
			y,
			width,
			height,
			viewBox );
	}

	/*
	 public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 {
	 //Load style string
	 super.loaderStartElement(helper, attrs, parent);

	 x = XMLParseUtil.parseNumberWithUnits(attrs.getValue("x"));
	 y = XMLParseUtil.parseNumberWithUnits(attrs.getValue("y"));
	 width = XMLParseUtil.parseNumberWithUnits(attrs.getValue("width"));
	 height = XMLParseUtil.parseNumberWithUnits(attrs.getValue("height"));

	 String viewBox = attrs.getValue("viewBox");
	 float[] coords = XMLParseUtil.parseFloatList(viewBox);

	 if (coords == null)
	 {
	 //this.viewBox.setRect(0, 0, width.getValue(), height.getValue());
	 this.viewBox = null;
	 }
	 else
	 {
	 this.viewBox = new Rectangle2D.Float(coords[0], coords[1], coords[2], coords[3]);
	 }

	 String par = attrs.getValue("preserveAspectRatio");
	 if (par != null)
	 {
	 String[] parList = XMLParseUtil.parseStringList(par);

	 if (parList[0].equals("none")) { parAlignX = PA_X_NONE; parAlignY = PA_Y_NONE; }
	 else if (parList[0].equals("xMinYMin")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MIN; }
	 else if (parList[0].equals("xMidYMin")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MIN; }
	 else if (parList[0].equals("xMaxYMin")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MIN; }
	 else if (parList[0].equals("xMinYMid")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MID; }
	 else if (parList[0].equals("xMidYMid")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MID; }
	 else if (parList[0].equals("xMaxYMid")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MID; }
	 else if (parList[0].equals("xMinYMax")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MAX; }
	 else if (parList[0].equals("xMidYMax")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MAX; }
	 else if (parList[0].equals("xMaxYMax")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MAX; }

	 if (parList[1].equals("meet")) parSpecifier = PS_MEET;
	 else if (parList[1].equals("slice")) parSpecifier = PS_SLICE;
	 }

	 build();
	 }
	 */

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'x':
				if (addPresentationAttribute(
					name,
					value,
					x ))
				{
					return true;
				}
			case 'y':
				if (addPresentationAttribute(
					name,
					value,
					y ))
				{
					return true;
				}
			case 'w':
				if (addPresentationAttribute(
					name,
					value,
					width ))
				{
					return true;
				}
			case 'h':
				if (addPresentationAttribute(
					name,
					value,
					height ))
				{
					return true;
				}
			case 'v':
				if (addPresentationAttribute(
					name,
					value,
					viewBox ))
				{
					return true;
				}
			case 'p':
				if (addPresentationAttribute(
					name,
					value,
					aspectRatio ))
				{
					return true;
				}
		}
		return super.initAtttributeValue(
			name,
			value );
	}

	public void build() throws SVGException
	{
		super.build();

		StyleAttribute sty = new StyleAttribute();

		//        if (getPres(sty.setName("x"))) x = sty.getNumberWithUnits();
		//        
		//        if (getPres(sty.setName("y"))) y = sty.getNumberWithUnits();
		//        
		//        if (getPres(sty.setName("width"))) width = sty.getNumberWithUnits();
		//        
		//        if (getPres(sty.setName("height"))) height = sty.getNumberWithUnits();
		//        
		//        if (getPres(sty.setName("viewBox"))) 
		//        {
		//            float[] coords = sty.getFloatList();
		//            viewBox = new Rectangle2D.Float(coords[0], coords[1], coords[2], coords[3]);
		//        }

		//        if (getPres(sty.setName("preserveAspectRatio")))
		//        {
		//            String preserve = sty.getStringValue();
		//            
		//            if (contains(preserve, "none")) { parAlignX = PA_X_NONE; parAlignY = PA_Y_NONE; }
		//            else if (contains(preserve, "xMinYMin")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MIN; }
		//            else if (contains(preserve, "xMidYMin")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MIN; }
		//            else if (contains(preserve, "xMaxYMin")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MIN; }
		//            else if (contains(preserve, "xMinYMid")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MID; }
		//            else if (contains(preserve, "xMidYMid")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MID; }
		//            else if (contains(preserve, "xMaxYMid")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MID; }
		//            else if (contains(preserve, "xMinYMax")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MAX; }
		//            else if (contains(preserve, "xMidYMax")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MAX; }
		//            else if (contains(preserve, "xMaxYMax")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MAX; }
		//
		//            if (contains(preserve, "meet")) parSpecifier = PS_MEET;
		//            else if (contains(preserve, "slice")) parSpecifier = PS_SLICE;
		//            
		/*
		String[] parList = sty.getStringList();

		if (parList[0].equals("none")) { parAlignX = PA_X_NONE; parAlignY = PA_Y_NONE; }
		else if (parList[0].equals("xMinYMin")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MIN; }
		else if (parList[0].equals("xMidYMin")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MIN; }
		else if (parList[0].equals("xMaxYMin")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MIN; }
		else if (parList[0].equals("xMinYMid")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MID; }
		else if (parList[0].equals("xMidYMid")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MID; }
		else if (parList[0].equals("xMaxYMid")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MID; }
		else if (parList[0].equals("xMinYMax")) { parAlignX = PA_X_MIN; parAlignY = PA_Y_MAX; }
		else if (parList[0].equals("xMidYMax")) { parAlignX = PA_X_MID; parAlignY = PA_Y_MAX; }
		else if (parList[0].equals("xMaxYMax")) { parAlignX = PA_X_MAX; parAlignY = PA_Y_MAX; }

		if (parList[1].equals("meet")) parSpecifier = PS_MEET;
		else if (parList[1].equals("slice")) parSpecifier = PS_SLICE;
		 */
		//       }

		prepareViewport();
	}

	private boolean contains( String text, String find )
	{
		return ( text.indexOf( find ) != -1 );
	}

	protected void prepareViewport()
	{
		Rectangle deviceViewport = diagram.getDeviceViewport();

		Rectangle2D defaultBounds;
		try
		{
			defaultBounds = getBoundingBox();
		}
		catch (SVGException ex)
		{
			defaultBounds = new Rectangle2D.Float();
		}

		//Determine destination rectangle
		float xx, yy, ww, hh;
		if (width.isSet())
		{

			xx = x.getNormalisedValue( deviceViewport.width );
			ww = width.getNormalisedValue( deviceViewport.width );
			//(x == null) ? 0 : StyleAttribute.convertUnitsToPixels(x.getUnits(), x.getValue());
			//            setAttribute("x", AnimationElement.AT_XML, "" + xx);
			//            setAttribute("width", AnimationElement.AT_XML, "" + ww);
		}
		else
			if (viewBox.isSet())
			{
				Rectangle2D.Float rect = viewBox.getRect();
				xx = (float) rect.x;
				ww = (float) rect.width;
				width.setValue(
					ww,
					CSSUnits.UT_PX );// = new NumberWithUnits(ww, NumberWithUnits.UT_PX);
				x.setValue(
					xx,
					CSSUnits.UT_PX );// = new NumberWithUnits(xx, NumberWithUnits.UT_PX);
			}
			else
			{
				//Estimate size from scene bounding box
				xx = (float) defaultBounds.getX();
				ww = (float) defaultBounds.getWidth();
				width.setValue(
					ww,
					CSSUnits.UT_PX );// = new NumberWithUnits(ww, NumberWithUnits.UT_PX);
				x.setValue(
					xx,
					CSSUnits.UT_PX );// = new NumberWithUnits(xx, NumberWithUnits.UT_PX);
			}

		if (height.isSet())
		{
			yy = y.getNormalisedValue( deviceViewport.height );//y == null) ? 0 : StyleAttribute.convertUnitsToPixels(y.getUnits(), y.getValue());
			hh = height.getNormalisedValue( deviceViewport.height );
		}
		else
			if (viewBox.isSet())
			{
				Rectangle2D rect = viewBox.getRect();
				yy = (float) rect.getMinY();
				hh = (float) rect.getHeight();
				height.setValue(
					hh,
					CSSUnits.UT_PX );// = new NumberWithUnits(hh, NumberWithUnits.UT_PX);
				y.setValue(
					yy,
					CSSUnits.UT_PX );// = new NumberWithUnits(yy, NumberWithUnits.UT_PX);
			}
			else
			{
				//Estimate size from scene bounding box
				yy = (float) defaultBounds.getY();
				hh = (float) defaultBounds.getHeight();
				height.setValue(
					hh,
					CSSUnits.UT_PX );// = new NumberWithUnits(hh, NumberWithUnits.UT_PX);
				y.setValue(
					yy,
					CSSUnits.UT_PX );// = new NumberWithUnits(yy, NumberWithUnits.UT_PX);
			}

		clipRect.setRect(
			xx,
			yy,
			ww,
			hh );

		if (viewBox.isSet())
		{
			Rectangle2D.Float rect = viewBox.getRect();
			viewXform.setToTranslation(
				clipRect.x,
				clipRect.y );
			viewXform.scale(
				clipRect.width,
				clipRect.height );
			viewXform.scale(
				1 / rect.width,
				1 / rect.height );
			viewXform.translate(
				-rect.x,
				-rect.y );
		}
		else
		{
			viewXform.setToIdentity();
		}

		//For now, treat all preserveAspectRatio as 'none'
		//        viewXform.setToTranslation(viewBox.x, viewBox.y);
		//        viewXform.scale(clipRect.width / viewBox.width, clipRect.height / viewBox.height);
		//        viewXform.translate(-clipRect.x, -clipRect.y);
	}

	public void render( Graphics2D g ) throws SVGException
	{
		prepareViewport();

		AffineTransform cachedXform = g.getTransform();
		g.transform( viewXform );

		super.render( g );

		g.setTransform( cachedXform );
	}

	public Shape getShape()
	{
		Shape shape = super.getShape();
		return viewXform.createTransformedShape( shape );
	}

	public Rectangle2D getBoundingBox() throws SVGException
	{
		Rectangle2D bbox = super.getBoundingBox();
		return viewXform.createTransformedShape(
			bbox )
			.getBounds2D();
	}

	public float getDeviceWidth()
	{
		return clipRect.width;
	}

	public float getDeviceHeight()
	{
		return clipRect.height;
	}

	public Rectangle2D getDeviceRect( Rectangle2D rect )
	{
		rect.setRect( clipRect );
		return rect;
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        boolean changeState = super.updateTime(curTime);
		//        
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        
		//        if (getPres(sty.setName("x")))
		//        {
		//            NumberWithUnits newVal = sty.getNumberWithUnits();
		//            if (!newVal.equals(x))
		//            {
		//                x = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("y")))
		//        {
		//            NumberWithUnits newVal = sty.getNumberWithUnits();
		//            if (!newVal.equals(y))
		//            {
		//                y = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("width")))
		//        {
		//            NumberWithUnits newVal = sty.getNumberWithUnits();
		//            if (!newVal.equals(width))
		//            {
		//                width = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("height")))
		//        {
		//            NumberWithUnits newVal = sty.getNumberWithUnits();
		//            if (!newVal.equals(height))
		//            {
		//                height = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("viewBox"))) 
		//        {
		//            float[] coords = sty.getFloatList();
		//            Rectangle2D.Float newViewBox = new Rectangle2D.Float(coords[0], coords[1], coords[2], coords[3]);
		//            if (!newViewBox.equals(viewBox))
		//            {
		//                viewBox = newViewBox;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if (shapeChange)
		//        {
		//            build();
		//        }
		//
		//        return changeState || shapeChange;
		return false;
	}

}
