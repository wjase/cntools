/*
 * Font.java
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
 * Created on February 20, 2004, 10:00 PM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.xml.attributes.IntAttribute;
import com.kitfox.svg.xml.attributes.IntDefaultConstant;
import com.kitfox.svg.xml.attributes.Path2DAttribute;
import com.kitfox.svg.xml.attributes.WindingRuleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

// import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;

/**
 * Implements an embedded font.
 *
 * SVG specification: http://www.w3.org/TR/SVG/fonts.html
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class MissingGlyph extends ShapeElement
{
	//We may define a path
	//    ExtendedGeneralPath path = null;
	//Shape path = null;

	private Path2DAttribute path = new Path2DAttribute( "d" );

	private WindingRuleAttribute fillRule = new WindingRuleAttribute( "fill-rule" );

	//Alternately, we may have child graphical elements
	private IntAttribute horizAdvX = new IntAttribute( "horiz-adv-x" ); //Inherits font's value if not set

	private IntAttribute vertOriginX = new IntAttribute( "vert-origin-x" ); //Inherits font's value if not set

	private IntAttribute vertOriginY = new IntAttribute( "vert-origin-y" ); //Inherits font's value if not set

	private IntAttribute vertAdvY = new IntAttribute( "vert-adv-y" ); //Inherits font's value if not set

	/**
	 * Creates a new instance of Font
	 */
	public MissingGlyph()
	{
		IntDefaultConstant negOne = new IntDefaultConstant( -1 );
		horizAdvX.setDefaultSource( negOne );
		vertOriginX.setDefaultSource( negOne );
		vertOriginY.setDefaultSource( negOne );
		vertAdvY.setDefaultSource( negOne );

		addPresentationAttributes(
			fillRule,
			horizAdvX,
			path,
			vertAdvY,
			vectorEffect,
			vertOriginX,
			vertOriginY,
			xmlBase );
	}

	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * //If glyph path was specified, calculate it
	 * String commandList = attrs.getValue("d");
	 * if (commandList != null)
	 * {
	 * StyleAttribute atyleAttrib = getStyle("fill-rule");
	 * String fillRule = (atyleAttrib == null) ? "nonzero" : atyleAttrib.getStringValue();
	 *
	 * PathCommand[] commands = parsePathList(commandList);
	 *
	 * // ExtendedGeneralPath buildPath = new ExtendedGeneralPath(
	 * GeneralPath buildPath = new GeneralPath(
	 * fillRule.equals("evenodd") ? GeneralPath.WIND_EVEN_ODD : GeneralPath.WIND_NON_ZERO,
	 * commands.length);
	 *
	 * BuildHistory hist = new BuildHistory();
	 *
	 * for (int i = 0; i < commands.length; ++i)
	 * {
	 * PathCommand cmd = commands[i];
	 * cmd.appendPath(buildPath, hist);
	 * }
	 *
	 * //Reflect glyph path to put it in user coordinate system
	 * AffineTransform at = new AffineTransform();
	 * at.scale(1, -1);
	 * path = at.createTransformedShape(buildPath);
	 * }
	 *
	 *
	 * //Read glyph spacing info
	 * String horizAdvX = attrs.getValue("horiz-adv-x");
	 * String vertOriginX = attrs.getValue("vert-origin-x");
	 * String vertOriginY = attrs.getValue("vert-origin-y");
	 * String vertAdvY = attrs.getValue("vert-adv-y");
	 *
	 * if (horizAdvX != null) this.horizAdvX = XMLParseUtil.parseInt(horizAdvX);
	 * if (vertOriginX != null) this.vertOriginX = XMLParseUtil.parseInt(vertOriginX);
	 * if (vertOriginY != null) this.vertOriginY = XMLParseUtil.parseInt(vertOriginY);
	 * if (vertAdvY != null) this.vertAdvY = XMLParseUtil.parseInt(vertAdvY);
	 *
	 * }
	 */

	/**
	 * Called after the start element but before the end element to indicate
	 * each child tag that has been processed
	 */
	@Override
	public void loaderAddChild( SVGLoaderHelper helper, SVGElement child )
		throws SVGElementException
	{
		super.loaderAddChild(
			helper,
			child );
	}

	@Override
	public boolean initStyleAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'f':
				if (setStyleAttribute(
					name,
					value,
					fillRule ))
				{
					return true;
				}

		}
		return super.initStyleAtttributeValue(
			name,
			value );
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'd':
				if (addPresentationAttribute(
					name,
					value,
					path ))
				{
					return true;
				}
			case 'h':
				if (addPresentationAttribute(
					name,
					value,
					horizAdvX ))
				{
					return true;
				}
			case 'v':
				if (addPresentationAttribute(
					name,
					value,
												vertOriginX,
												vertOriginY,
												vertAdvY ))
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
	//        String commandList = "";
	//        if (getPres(sty.setName("d"))) commandList = sty.getStringValue();
	//
	//    
	//        //If glyph path was specified, calculate it
	//        if (commandList != null)
	//        {
	////            StyleAttribute atyleAttrib = getStyle("fill-rule");
	//            String fillRule = getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero";
	//
	//            PathCommand[] commands = parsePathList(commandList);
	//
	////            ExtendedGeneralPath buildPath = new ExtendedGeneralPath(
	//            GeneralPath buildPath = new GeneralPath(
	//                fillRule.equals("evenodd") ? GeneralPath.WIND_EVEN_ODD : GeneralPath.WIND_NON_ZERO,
	//                commands.length);
	//
	//            BuildHistory hist = new BuildHistory();
	//
	//            MutableGeneralPath mgp = new MutableGeneralPath( buildPath );
	//            for (int i = 0; i < commands.length; ++i)
	//            {
	//                PathCommand cmd = commands[i];
	//                cmd.appendPath( mgp, hist);
	//            }
	//
	//            //Reflect glyph path to put it in user coordinate system
	//            AffineTransform at = new AffineTransform();
	//            at.scale(1, -1);
	//            path = at.createTransformedShape(buildPath);
	//        }
	//
	//
	//        //Read glyph spacing info
	//        if (getPres(sty.setName("horiz-adv-x"))) horizAdvX = sty.getIntValue();
	//
	//        if (getPres(sty.setName("vert-origin-x"))) vertOriginX = sty.getIntValue();
	//
	//        if (getPres(sty.setName("vert-origin-y"))) vertOriginY = sty.getIntValue();
	//
	//        if (getPres(sty.setName("vert-adv-y"))) vertAdvY = sty.getIntValue();
	//    }

	public Shape getPath()
	{
		return path.getShape();
	}

	public void render( Graphics2D g ) throws SVGException
	{
		//Do not push or pop stack

		if (path != null)
		{
			renderShape(
				g,
				path.getShape() );
		}

		Iterator it = children.iterator();
		while (it.hasNext())
		{
			SVGElement ele = (SVGElement) it.next();
			if (ele instanceof RenderableElement)
			{
				( (RenderableElement) ele ).render( g );
			}
		}

		//Do not push or pop stack
	}

	public int getHorizAdvX()
	{
		int ret = this.horizAdvX.getIntValue();
		if (ret == -1)
		{
			ret = ( (Font) parent ).getHorizAdvX();
		}
		return ret;
	}

	public int getVertOriginX()
	{
		int ret = this.vertOriginX.getIntValue();
		if (ret == -1)
		{
			ret = getHorizAdvX() / 2;
		}
		return ret;
	}

	public int getVertOriginY()
	{
		int ret = this.vertOriginY.getIntValue();
		if (ret == -1)
		{
			ret = ( (Font) parent ).getFontFace()
				.getAscent();
		}
		return ret;
	}

	public int getVertAdvY()
	{
		int ret = this.vertAdvY.getIntValue();
		if (ret == -1)
		{
			ret = ( (Font) parent ).getFontFace()
				.getUnitsPerEm();
		}
		return ret;

	}

	public Shape getShape()
	{
		return shapeToParent( path.getShape() );

	}

	public Rectangle2D getBoundingBox() throws SVGException
	{
		if (path != null)
		{
			return boundsToParent( includeStrokeInBounds( path.getShape()
				.getBounds2D() ) );
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
		//Fonts can't change
		return false;
	}

}
