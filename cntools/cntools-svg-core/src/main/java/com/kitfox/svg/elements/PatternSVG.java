/*
 * Gradient.java
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
 * Created on January 26, 2004, 3:25 AM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.pattern.PatternPaint;
import com.kitfox.svg.xml.attributes.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class PatternSVG extends FillElement
{

	public static final int GU_OBJECT_BOUNDING_BOX = 0;

	public static final int GU_USER_SPACE_ON_USE = 1;

	private IntAttribute gradientUnits = new IntAttribute( "gradientUnits" )
	{

		@Override
		public void setStringValue( String value )
		{
			setValue( value.equals( "userspaceonuse" ) ?
					GU_USER_SPACE_ON_USE :
					GU_OBJECT_BOUNDING_BOX );
		}

	};

	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );

	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	private TransformAttribute patternXform = new TransformAttribute( "patternTransform" );

	private ElementReferenceAttribute xlinkhref = new ElementReferenceAttribute( "xlink:href",
		this );

	//Rectangle2D viewBox = new Rectangle2D.Float( );
	private RectangleAttribute viewBox = new RectangleAttribute( "viewbox" );

	private Paint texPaint;
	// if the pattern is loaded before the xref it points to

	private URI forwardRef = null;

	@Override
	public void attributeUpdated( IStringAttribute source )
	{
		isDirty = true;
		super.attributeUpdated( source );
	}

	/**
	 * Creates a new instance of Gradient
	 */
	public PatternSVG()
	{
		addPresentationAttributes(
			x,
			y,
			width,
			height,
			viewBox,
			patternXform,
			xlinkhref );
	}

	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * String href = attrs.getValue("xlink:href");
	 * //If we have a link to another pattern, initialize ourselves with it's values
	 * if (href != null)
	 * {
	 * //System.err.println("Gradient.loaderStartElement() href '" + href + "'");
	 * try {
	 * URI src = getXMLBase().resolve(href);
	 * // URL url = srcUrl.toURL();
	 * // URL url = new URL(helper.docRoot, href);
	 * PatternSVG patSrc = (PatternSVG)helper.universe.getElement(src);
	 *
	 * gradientUnits = patSrc.gradientUnits;
	 * x = patSrc.x;
	 * y = patSrc.y;
	 * width = patSrc.width;
	 * height = patSrc.height;
	 * viewBox = patSrc.viewBox;
	 * patternXform.setTransform(patSrc.patternXform);
	 * members.addAll(patSrc.members);
	 * }
	 * catch (Exception e)
	 * {
	 * e.printStackTrace();
	 * }
	 * }
	 *
	 *
	 * String gradientUnits = attrs.getValue("gradientUnits");
	 *
	 * if (gradientUnits != null)
	 * {
	 * if (gradientUnits.toLowerCase().equals("userspaceonuse")) this.gradientUnits = GU_USER_SPACE_ON_USE;
	 * else this.gradientUnits = GU_OBJECT_BOUNDING_BOX;
	 * }
	 *
	 * String patternTransform = attrs.getValue("patternTransform");
	 * if (patternTransform != null)
	 * {
	 * patternXform = parseTransform(patternTransform);
	 * }
	 *
	 * String x = attrs.getValue("x");
	 * String y = attrs.getValue("y");
	 * String width = attrs.getValue("width");
	 * String height = attrs.getValue("height");
	 *
	 * if (x != null) this.x = XMLParseUtil.parseFloat(x);
	 * if (y != null) this.y = XMLParseUtil.parseFloat(y);
	 * if (width != null) this.width = XMLParseUtil.parseFloat(width);
	 * if (height != null) this.height = XMLParseUtil.parseFloat(height);
	 *
	 * String viewBoxStrn = attrs.getValue("viewBox");
	 * if (viewBoxStrn != null)
	 * {
	 * float[] dim = XMLParseUtil.parseFloatList(viewBoxStrn);
	 * viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
	 * }
	 * }
	 */

	/**
	 * Called after the start element but before the end element to indicate
	 * each child tag that has been processed
	 */
	public void loaderAddChild( SVGLoaderHelper helper, SVGElement child )
		throws SVGElementException
	{
		super.loaderAddChild(
			helper,
			child );

		//        members.add(child);
	}

	public int getGradientUnits()
	{
		return gradientUnits.getIntValue();
	}

	public float getX()
	{
		if (!xlinkhref.isSet())
		{
			return x.getNormalisedValue();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getX();
	}

	public float getY()
	{
		if (!xlinkhref.isSet())
		{
			return y.getNormalisedValue();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getY();
	}

	public float getWidth()
	{
		if (!xlinkhref.isSet())
		{
			return width.getNormalisedValue();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getWidth();
	}

	public float getHeight()
	{
		if (!xlinkhref.isSet())
		{
			return height.getNormalisedValue();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getHeight();
	}

	public Rectangle2D getViewBox()
	{
		if (!xlinkhref.isSet())
		{
			return viewBox.getRect();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getViewBox();
	}

	public AffineTransform getPatternXForm()
	{
		if (!xlinkhref.isSet())
		{
			return patternXform.getXform();
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getPatternXForm();
	}

	public ArrayList getChildren()
	{
		if (!xlinkhref.isSet())
		{
			return children;
		}
		return ( (PatternSVG) xlinkhref.getElement() ).getChildren();
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
					x,
					xlinkhref ))
				{
					return true;
				}
			case 'g':
				if (addPresentationAttribute(
					name,
					value,
					gradientUnits ))
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
			case 'p':
				if (addPresentationAttribute(
					name,
					value,
					patternXform ))
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

	//    protected void build() throws SVGException
	//    {
	//        super.build();
	//
	////        StyleAttribute sty = new StyleAttribute();
	////
	////        //Load style string
	////        String href = null;
	////        if ( getPres( sty.setName( "xlink:href" ) ) )
	////        {
	////            href = sty.getStringValue();
	////        }
	////        //String href = attrs.getValue("xlink:href");
	////        //If we have a link to another pattern, initialize ourselves with it's values
	////        if ( href != null )
	////        {
	//////System.err.println("Gradient.loaderStartElement() href '" + href + "'");
	////            try
	////            {
	////                URI base = getXMLBase();
	////                URI src = SVGUniverse.resolve( base, href );
	////                PatternSVG patSrc = null;
	////                try
	////                {
	////                    patSrc = ( PatternSVG ) diagram.getUniverse().getElement( src );
	////                }
	////                catch ( Exception e )
	////                {
	////                }
	////
	////
	////                if ( patSrc != null )
	////                {
	////                    gradientUnits = patSrc.gradientUnits;
	////                    x = patSrc.x;
	////                    y = patSrc.y;
	////                    width = patSrc.width;
	////                    height = patSrc.height;
	////                    viewBox = patSrc.viewBox;
	////                    patternXform.setTransform( patSrc.patternXform );
	////                    children.addAll( patSrc.children );
	////
	////                }
	////                else
	////                {
	////                    forwardRef = src;
	////                }
	////            }
	////            catch ( Exception e )
	////            {
	////                e.printStackTrace();
	////            }
	////        }
	////
	////        String gradientUnits = "";
	////        if ( getPres( sty.setName( "gradientUnits" ) ) )
	////        {
	////            gradientUnits = sty.getStringValue().toLowerCase();
	////        }
	////        if ( gradientUnits.equals( "userspaceonuse" ) )
	////        {
	////            this.gradientUnits = GU_USER_SPACE_ON_USE;
	////        }
	////        else
	////        {
	////            this.gradientUnits = GU_OBJECT_BOUNDING_BOX;
	////        }
	////
	////        String patternTransform = "";
	////        if ( getPres( sty.setName( "patternTransform" ) ) )
	////        {
	////            patternTransform = sty.getStringValue();
	////        }
	////        patternXform = parseTransform( patternTransform );
	////
	////
	////        if ( getPres( sty.setName( "x" ) ) )
	////        {
	////            x = sty.getFloatValueWithUnits();
	////        }
	////
	////        if ( getPres( sty.setName( "y" ) ) )
	////        {
	////            y = sty.getFloatValueWithUnits();
	////        }
	////
	////        if ( getPres( sty.setName( "width" ) ) )
	////        {
	////            width = sty.getFloatValueWithUnits();
	////        }
	////
	////        if ( getPres( sty.setName( "height" ) ) )
	////        {
	////            height = sty.getFloatValueWithUnits();
	////        }
	////
	////        if ( getPres( sty.setName( "viewBox" ) ) )
	////        {
	////            float[] dim = sty.getFloatList();
	////            viewBox = new Rectangle2D.Float( dim[0], dim[1], dim[2], dim[3] );
	////        }
	//
	//        preparePattern();
	//    }

	/*
	 * public void loaderEndElement(SVGLoaderHelper helper)
	 * {
	 * build();
	 * }
	 */
	protected void preparePattern() throws SVGException
	{
		//For now, treat all fills as UserSpaceOnUse.  Otherwise, we'll need
		// a different paint for every object.
		int tileWidth = (int) getWidth();
		int tileHeight = (int) getHeight();

		float stretchX = 1f, stretchY = 1f;
		AffineTransform xform = new AffineTransform();// getPatternXForm();
		if (!xform.isIdentity())
		{
			//Scale our source tile so that we can have nice sampling from it.
			float xlateX = (float) xform.getTranslateX();
			float xlateY = (float) xform.getTranslateY();

			Point2D.Float pt = new Point2D.Float(), pt2 = new Point2D.Float();

			pt.setLocation(
				tileWidth,
				0 );
			xform.transform(
				pt,
				pt2 );
			pt2.x -= xlateX;
			pt2.y -= xlateY;
			stretchX = (float) Math.sqrt( pt2.x * pt2.x + pt2.y * pt2.y )
				* 1.5f / tileWidth;

			pt.setLocation(
				0,
				tileHeight );
			xform.transform(
				pt,
				pt2 );
			pt2.x -= xlateX;
			pt2.y -= xlateY;
			stretchY = (float) Math.sqrt( pt2.x * pt2.x + pt2.y * pt2.y )
				* 1.5f / tileHeight;

			tileWidth *= stretchX;
			tileHeight *= stretchY;
		}

		if (tileWidth == 0 || tileHeight == 0)
		{
			//Use defaults if tile has degenerate size
			return;
		}

		BufferedImage buf = new BufferedImage( tileWidth,
			tileHeight,
			BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = buf.createGraphics();
		g.setClip(
			0,
			0,
			tileWidth,
			tileHeight );
		g.setRenderingHint(
			RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON );

		float xVal = getX();
		float yVal = getY();

		for (Iterator it = getChildren().iterator(); it.hasNext();)
		{
			SVGElement ele = (SVGElement) it.next();
			if (ele instanceof RenderableElement)
			{
				//AffineTransform xform2 = new AffineTransform();

				if (!viewBox.isSet())
				{
					xform.translate(
						-xVal,
						-yVal );
				}
				else
				{
					Rectangle2D.Float rect = (Rectangle2D.Float) getViewBox();
					xform.scale(
						tileWidth / rect.width,
						tileHeight / rect.height );
					xform.translate(
						-rect.x,
						-rect.y );
				}

				g.setTransform( xform );
				( (RenderableElement) ele ).render( g );
			}
		}

		g.dispose();

		//try {
		//javax.imageio.ImageIO.write(buf, "png", new java.io.File("c:\\tmp\\texPaint.png"));
		//} catch (Exception e ) {}

		if (xform.isIdentity())
		{
			texPaint = new TexturePaint( buf, new Rectangle2D.Float( xVal,
				yVal,
				tileWidth,
				tileHeight ) );
		}
		else
		{

			xform.scale(
				1 / stretchX,
				1 / stretchY );
			texPaint = new PatternPaint( buf, xform );
		}
	}

	private boolean isDirty = true;

	@Override
	public Paint getPaint( Rectangle2D bounds, AffineTransform xform )
	{
		if (isDirty)
		{
			try
			{
				preparePattern();
				isDirty = false;
			}
			catch (SVGException ex)
			{
			}

		}
		if (texPaint == null && forwardRef != null)
		{
			try
			{
				build();
				forwardRef = null;

			}
			catch (SVGException ex)
			{
				Logger.getLogger(
					PatternSVG.class.getName() )
					.log(
						Level.SEVERE,
						null,
						ex );
			}
		}
		return texPaint;
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	@Override
	public boolean updateTime( double curTime ) throws SVGException
	{
		//Patterns don't change state
		return false;
	}

}
