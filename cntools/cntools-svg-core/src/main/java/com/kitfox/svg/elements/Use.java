/*
 * LinearGradient.java
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
 * Created on January 26, 2004, 1:54 AM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.ElementReferenceAttribute;
import com.kitfox.svg.xml.attributes.HasBounds;
import com.kitfox.svg.xml.attributes.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Use extends ShapeElement implements HasBounds
{

	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );

	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	private ElementReferenceAttribute referencedElement = new ElementReferenceAttribute( "xlink:href",
		this );

	private AffineTransform refXform;

	public CSSQuantityAttribute getWidth()
	{
		return width;
	}

	public CSSQuantityAttribute getHeight()
	{
		return height;
	}

	public CSSQuantityAttribute getX()
	{
		return x;
	}

	public CSSQuantityAttribute getY()
	{
		return y;
	}

	public ElementReferenceAttribute getXRef()
	{
		return referencedElement;
	}

	/**
	 * Creates a new instance of LinearGradient
	 */
	public Use()
	{
		addPresentationAttributes(
			x,
			y,
			width,
			height,
			referencedElement );
	}

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
	 * String href = attrs.getValue("xlink:href");
	 *
	 * if (x != null) this.x = (float)XMLParseUtil.parseRatio(x);
	 * if (y != null) this.y = (float)XMLParseUtil.parseRatio(y);
	 * if (width != null) this.width = (float)XMLParseUtil.parseRatio(width);
	 * if (height != null) this.height = (float)XMLParseUtil.parseRatio(height);
	 *
	 *
	 * if (href != null)
	 * {
	 * try {
	 * URI src = getXMLBase().resolve(href);
	 * this.href = helper.universe.getElement(src);
	 * }
	 * catch (Exception e)
	 * {
	 * e.printStackTrace();
	 * }
	 * }
	 *
	 * //Determine use offset/scale
	 * refXform = new AffineTransform();
	 * refXform.translate(this.x, this.y);
	 * refXform.scale(this.width, this.height);
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
					x,
					referencedElement ))
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

		}
		return super.initAtttributeValue(
			name,
			value );
	}

	@Override
	protected void build() throws SVGException
	{
		super.build();

		//        StyleAttribute sty = new StyleAttribute();

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

		//        if ( getPres( sty.setName( "xlink:href" ) ) )
		//        {
		//            URI src = sty.getURIValue( getXMLBase() );
		//            referencedElement = diagram.getUniverse().getElement( src );
		//        }

		//        if ( getPres( sty.setName( "transform" ) ) )
		//        {
		//            if ( getPres( sty.setName( "transform" ) ) )
		//            {
		//                refXform = parseTransform( sty.getStringValue() );
		//            }
		//        }
		//        else
		//        {
		//Determine use offset/scale
		refXform = new AffineTransform();
		refXform.translate(
			this.x.getNormalisedValue(),
			this.y.getNormalisedValue() );

		//        }
	}

	public void render( Graphics2D g ) throws SVGException
	{
		beginLayer( g );

		AffineTransform oldXform = g.getTransform();
		g.transform( refXform );

		if (referencedElement.isSet())
		{
			SVGElement el = referencedElement.getElement();
			if (el == null || !( el instanceof RenderableElement ))
			{
				return;
			}

			RenderableElement rendEle = (RenderableElement) el;
			rendEle.pushParentContext( this );
			rendEle.render( g );
			rendEle.popParentContext();

		}
		else
		{
			// TODO: bad link render some missing icon
		}

		g.setTransform( oldXform );

		finishLayer( g );
	}

	public Shape getShape()
	{
		if (referencedElement.getElement() instanceof ShapeElement)
		{
			Shape shape = ( (ShapeElement) referencedElement.getElement() ).getShape();
			shape = refXform.createTransformedShape( shape );
			shape = shapeToParent( shape );
			return shape;
		}

		return null;
	}

	@Override
	public Rectangle2D getBoundingBox() throws SVGException
	{
		if (referencedElement.getElement() instanceof ShapeElement)
		{
			Rectangle2D bounds = null;
			ShapeElement shapeEle = (ShapeElement) referencedElement.getElement();
			shapeEle.pushParentContext( this );
			bounds = shapeEle.getBoundingBox();
			shapeEle.popParentContext();
			if (bounds != null)
			{
				if (refXform != null)
				{
					bounds = refXform.createTransformedShape(
						bounds )
						.getBounds2D();
					bounds = boundsToParent( bounds );
				}

			}

			return bounds;
		}

		return null;
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
		//        if (trackManager.getNumTracks() == 0) return false;
		boolean changeState = super.updateTime( curTime );

		//Get current values for parameters
		StyleAttribute sty = new StyleAttribute();
		boolean shapeChange = false;

		//        if ( getPres( sty.setName( "x" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != x )
		//            {
		//                x = newVal;
		//                shapeChange = true;
		//            }
		//        }

		//        if ( getPres( sty.setName( "y" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != y )
		//            {
		//                y = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "width" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != width )
		//            {
		//                width = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "height" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != height )
		//            {
		//                height = newVal;
		//                shapeChange = true;
		//            }
		//        }

		//        if ( getPres( sty.setName( "xlink:href" ) ) )
		//        {
		//            URI src = sty.getURIValue( getXMLBase() );
		//            SVGElement newVal = diagram.getUniverse().getElement( src );
		//            if ( newVal != referencedElement )
		//            {
		//                referencedElement = newVal;
		//                shapeChange = true;
		//            }
		//        }
		/*
		 * if (getPres(sty.setName("xlink:href")))
		 * {
		 * URI src = sty.getURIValue(getXMLBase());
		 * href = diagram.getUniverse().getElement(src);
		 * }
		 *
		 * //Determine use offset/scale
		 * refXform = new AffineTransform();
		 * refXform.translate(this.x, this.y);
		 * refXform.scale(this.width, this.height);
		 */
		if (shapeChange)
		{
			build();
			//Determine use offset/scale
			//            refXform.setToTranslation(this.x, this.y);
			//            refXform.scale(this.width, this.height);
			//            return true;
		}

		return changeState || shapeChange;
	}

}
