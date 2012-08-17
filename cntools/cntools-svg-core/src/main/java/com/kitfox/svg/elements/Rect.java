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
import com.kitfox.svg.xml.attributes.HasBounds;
import com.kitfox.svg.xml.attributes.IStringAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Rect extends ShapeElement implements HasBounds
{

	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );

	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	private CSSQuantityAttribute rx = new CSSQuantityAttribute( "rx" );

	private CSSQuantityAttribute ry = new CSSQuantityAttribute( "ry" );

	private RectangularShape rect;

	public CSSQuantityAttribute getHeight()
	{
		return height;
	}

	public CSSQuantityAttribute getRx()
	{
		return rx;
	}

	public CSSQuantityAttribute getRy()
	{
		return ry;
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

	private boolean isDirty = true;

	/**
	 * Creates a new instance of Rect
	 */
	public Rect()
	{
		addPresentationAttributes(
			x,
			y,
			width,
			height,
			rx,
			ry );
	}

	//    private void writeObject(ObjectOutputStream out) throws IOException    
	//    {
	//        out.writeFloat(x);
	//        out.writeFloat(y);
	//        out.writeFloat(width);
	//        out.writeFloat(height);
	//        out.writeFloat(rx);
	//        out.writeFloat(ry);
	//    }
	//    
	//    private void readObject(ObjectInputStream in) throws IOException
	//    {
	//        x = in.readFloat();
	//        y = in.readFloat();
	//        width = in.readFloat();
	//        height = in.readFloat();
	//        rx = in.readFloat();
	//        ry = in.readFloat();
	//        
	//        if (rx == 0f && ry == 0f)
	//        {
	//            rect = new Rectangle2D.Float(x, y, width, height);
	//        }
	//        else
	//        {
	//            rect = new RoundRectangle2D.Float(x, y, width, height, rx * 2, ry * 2);
	//        }
	//    }
	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * String x = attrs.getValue("x");
	 * String y = attrs.getValue("y");
	 * String width = attrs.getValue("width");
	 * String height = attrs.getValue("height");
	 * String rx = attrs.getValue("rx");
	 * String ry = attrs.getValue("ry");
	 *
	 * if (rx == null) rx = ry;
	 * if (ry == null) ry = rx;
	 *
	 * this.x = XMLParseUtil.parseFloat(x);
	 * this.y = XMLParseUtil.parseFloat(y);
	 * this.width = XMLParseUtil.parseFloat(width);
	 * this.height = XMLParseUtil.parseFloat(height);
	 * if (rx != null)
	 * {
	 * this.rx = XMLParseUtil.parseFloat(rx);
	 * this.ry = XMLParseUtil.parseFloat(ry);
	 * }
	 *
	 * build();
	 * // setBounds(this.x, this.y, this.width, this.height);
	 * }
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
			case 'r':
				if (addPresentationAttribute(
					name,
					value,
					rx,
					ry ))
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
		}

		return super.initAtttributeValue(
			name,
			value );

	}

	RectangularShape getRect()
	{
		if (rect == null || isDirty)
		{
			isDirty = false;
			float rxVal = rx.getNormalisedValue();
			float ryVal = ry.getNormalisedValue();
			float xVal = x.getNormalisedValue();
			float yVal = y.getNormalisedValue();
			float widthVal = width.getNormalisedValue();
			float heightVal = height.getNormalisedValue();
			if (rxVal == 0f && ryVal == 0f)
			{
				rect = new Rectangle2D.Float( xVal, yVal, widthVal, heightVal );
			}
			else
			{
				rect = new RoundRectangle2D.Float( xVal,
					yVal,
					widthVal,
					heightVal,
					rxVal * 2,
					ryVal * 2 );
			}
		}
		return rect;
	}

	@Override
	public void attributeUpdated( IStringAttribute source )
	{
		if (source == x || source == y || source == rx || source == ry
			|| source == width || source == height)
		{
			isDirty = true;
			//            System.out.printf( "IS dirty %g %g %g %g\n",
			//                               x.getFloatValue(),
			//                               y.getFloatValue(),
			//                               width.getFloatValue(),
			//                               height.getFloatValue() );

		}
		//        else
		//        {
		//            System.out.printf( "not for me %s \n",source.asSVGString() );
		//        }
		super.attributeUpdated( source );
	}

	//    protected void build() throws SVGException
	//    {
	//        super.build();
	//
	//        StyleAttribute sty = new StyleAttribute();
	//
	////        SVGElement parent = this.getParent();
	////        if (parent instanceof RenderableElement)
	////        {
	////            RenderableElement re = (RenderableElement)parent;
	////            Rectangle2D bounds = re.getBoundingBox();
	////            bounds = null;
	////        }
	//
	//
	//        if ( getPres( sty.setName( "x" ) ) )
	//        {
	//            x = sty.getFloatValueWithUnits();
	//        }
	//
	//        if ( getPres( sty.setName( "y" ) ) )
	//        {
	//            y = sty.getFloatValueWithUnits();
	//        }
	//
	//        if ( getPres( sty.setName( "width" ) ) )
	//        {
	//            width = sty.getFloatValueWithUnits();
	//        }
	//
	//        if ( getPres( sty.setName( "height" ) ) )
	//        {
	//            height = sty.getFloatValueWithUnits();
	//        }
	//
	//        boolean rxSet = false;
	//        if ( getPres( sty.setName( "rx" ) ) )
	//        {
	//            rx = sty.getFloatValueWithUnits();
	//            rxSet = true;
	//        }
	//
	//        boolean rySet = false;
	//        if ( getPres( sty.setName( "ry" ) ) )
	//        {
	//            ry = sty.getFloatValueWithUnits();
	//            rySet = true;
	//        }
	//
	//        if ( !rxSet )
	//        {
	//            rx = ry;
	//        }
	//        if ( !rySet )
	//        {
	//            ry = rx;
	//        }
	//
	//
	//        if ( rx == 0f && ry == 0f )
	//        {
	//            rect = new Rectangle2D.Float( x, y, width, height );
	//        }
	//        else
	//        {
	//            rect = new RoundRectangle2D.Float( x, y, width, height, rx * 2, ry * 2 );
	//        }
	//    }
	public void render( Graphics2D g ) throws SVGException
	{
		Rectangle2D r = getRect().getBounds2D();
		//System.out.printf( "render %g %g %g %g\n", r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight() );
		beginLayer( g );
		renderShape(
			g,
			getRect() );
		finishLayer( g );
	}

	public Shape getShape()
	{
		return shapeToParent( getRect() );
	}

	public Rectangle2D getBoundingBox() throws SVGException
	{
		return boundsToParent( includeStrokeInBounds( getRect().getBounds2D() ) );
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		////        if (trackManager.getNumTracks() == 0) return false;
		//        boolean changeState = super.updateTime( curTime );
		//
		//        //Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//
		//        if ( getPres( sty.setName( "x" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != x )
		//            {
		//                x = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "y" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != y )
		//            {
		//                y = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "width" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != width )
		//            {
		//                width = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "height" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != height )
		//            {
		//                height = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "rx" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != rx )
		//            {
		//                rx = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "ry" ) ) )
		//        {
		//            FloatUnitAttribute newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != ry )
		//            {
		//                ry = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( shapeChange )
		//        {
		//            build();
		////            if (rx == 0f && ry == 0f)
		////            {
		////                rect = new Rectangle2D.Float(x, y, width, height);
		////            }
		////            else
		////            {
		////                rect = new RoundRectangle2D.Float(x, y, width, height, rx * 2, ry * 2);
		////            }
		////            return true;
		//        }
		//
		//        return changeState || shapeChange;
		return false;
	}

}
