/*
 * Stop.java
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
 * Created on January 26, 2004, 1:56 AM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.xml.attributes.FloatListAttribute;
import com.kitfox.svg.xml.attributes.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

// import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;
/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Tspan extends ShapeElement
{

	private FloatListAttribute x = new FloatListAttribute( "x" );

	private FloatListAttribute y = new FloatListAttribute( "y" );

	private FloatListAttribute dx = new FloatListAttribute( "dx" );

	private FloatListAttribute dy = new FloatListAttribute( "dy" );

	private FloatListAttribute rotate = new FloatListAttribute( "rotate" )
	{

		@Override
		public void setStringValue( String value )
		{
			super.setStringValue( value );
			float[] values = getFloatListValue();
			for (int i = 0; i < values.length; ++i)
			{
				values[ i ] = (float) Math.toRadians( values[ i ] );
			}

		}

		@Override
		public String asSVGString()
		{
			throw new RuntimeException( "not implemented yet" );
			//return super.asSVGString();
		}

	};

	private String text = "";

	private float cursorX;

	private float cursorY;

	//    Shape tspanShape;
	/**
	 * Creates a new instance of Stop
	 */
	public Tspan()
	{
		addPresentationAttributes(
			x,
			y,
			dx,
			dy,
			rotate );
	}

	public float getCursorX()
	{
		return cursorX;
	}

	public float getCursorY()
	{
		return cursorY;
	}

	public void setCursorX( float cursorX )
	{
		this.cursorX = cursorX;
	}

	public void setCursorY( float cursorY )
	{
		this.cursorY = cursorY;
	}

	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * String x = attrs.getValue("x");
	 * String y = attrs.getValue("y");
	 * String dx = attrs.getValue("dx");
	 * String dy = attrs.getValue("dy");
	 * String rotate = attrs.getValue("rotate");
	 *
	 * if (x != null) this.x = XMLParseUtil.parseFloatList(x);
	 * if (y != null) this.y = XMLParseUtil.parseFloatList(y);
	 * if (dx != null) this.dx = XMLParseUtil.parseFloatList(dx);
	 * if (dy != null) this.dy = XMLParseUtil.parseFloatList(dy);
	 * if (rotate != null)
	 * {
	 * this.rotate = XMLParseUtil.parseFloatList(rotate);
	 * for (int i = 0; i < this.rotate.length; ++i)
	 * this.rotate[i] = (float)Math.toRadians(this.rotate[i]);
	 * }
	 * }
	 */

	/**
	 * Called during load process to add text scanned within a tag
	 */
	public void loaderAddText( SVGLoaderHelper helper, String text )
	{
		this.text += text;
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
			case 'd':
				if (addPresentationAttribute(
					name,
					value,
					dx,
					dy ))
				{
					return true;
				}

			case 'r':
				if (addPresentationAttribute(
					name,
					value,
					rotate ))
				{
					return true;
				}

		}
		return super.initAtttributeValue(
			name,
			value );
	}

	public void addShape( GeneralPath addShape ) throws SVGException
	{
		float[] xValues = null;
		float[] yValues = null;
		boolean useDelta = false;
		if (x.isSet())
		{
			xValues = x.getFloatListValue();
			yValues = y.getFloatListValue();
			cursorX = xValues[ 0 ];
			cursorY = yValues[ 0 ];
		}
		else
			if (dx.isSet())
			{
				useDelta = true;
				xValues = dx.getFloatListValue();
				yValues = dy.getFloatListValue();
				cursorX += xValues[ 0 ];
				cursorY += yValues[ 0 ];
			}

		StyleAttribute sty = new StyleAttribute();

		String fontFamily = null;
		if (getStyle( sty.withName( "font-family" ) ))
		{
			fontFamily = sty.getStringValue();
		}

		float fontSize = 12f;
		if (getStyle( sty.withName( "font-size" ) ))
		{
			fontSize = sty.getFloatValueWithUnits();
		}

		//Get font
		Font font = diagram.getUniverse()
			.getFont(
				fontFamily );
		if (font == null)
		{
			addShapeSysFont(
				addShape,
				font,
				fontFamily,
				fontSize );
			return;
		}

		FontFace fontFace = font.getFontFace();
		int ascent = fontFace.getAscent();
		float fontScale = fontSize / (float) ascent;

		AffineTransform xform = new AffineTransform();

		strokeWidthScalar = 1f / fontScale;

		int posPtr = 1;

		float[] rotateValues = rotate.getFloatListValue();
		for (int i = 0; i < text.length(); ++i)
		{
			xform.setToIdentity();
			xform.setToTranslation(
				cursorX,
				cursorY );
			xform.scale(
				fontScale,
				fontScale );
			if (rotate != null)
			{
				xform.rotate( rotateValues[ posPtr ] );
			}

			String unicode = text.substring(
				i,
				i + 1 );
			MissingGlyph glyph = font.getGlyph( unicode );

			Shape path = glyph.getPath();
			if (path != null)
			{
				path = xform.createTransformedShape( path );
				addShape.append(
					path,
					false );
			}

			if (posPtr < xValues.length)
			{
				if (!useDelta)
				{
					cursorX = xValues[ posPtr ];
					cursorY = yValues[ posPtr++ ];
				}
				else
				{
					cursorX += xValues[ posPtr ];
					cursorY += yValues[ ++posPtr ];
				}

			}

			cursorX += fontScale * glyph.getHorizAdvX();
		}

		strokeWidthScalar = 1f;
	}

	private void addShapeSysFont( GeneralPath addShape,
		Font font,
		String fontFamily,
		float fontSize )
	{
		java.awt.Font sysFont = new java.awt.Font( fontFamily,
			java.awt.Font.PLAIN,
			(int) fontSize );

		FontRenderContext frc = new FontRenderContext( null, true, true );
		GlyphVector textVector = sysFont.createGlyphVector(
			frc,
			text );

		AffineTransform xform = new AffineTransform();

		float[] xValues;
		float[] yValues;
		boolean relative = false;
		if (x.isSet())
		{
			xValues = x.getFloatListValue();
			yValues = y.getFloatListValue();
		}
		else
		{
			relative = true;
			xValues = dx.getFloatListValue();
			yValues = dy.getFloatListValue();
		}

		boolean useRotate = rotate.isSet();
		float[] angles = rotate.getFloatListValue();

		int posPtr = 1;
		for (int i = 0; i < text.length(); ++i)
		{
			xform.setToIdentity();
			xform.setToTranslation(
				cursorX,
				cursorY );
			if (useRotate)
			{
				xform.rotate( angles[ Math.min(
					i,
					angles.length - 1 ) ] );
			}

			//String unicode = text.substring( i, i + 1 );
			Shape glyphOutline = textVector.getGlyphOutline( i );
			//GlyphMetrics glyphMetrics = textVector.getGlyphMetrics( i );

			glyphOutline = xform.createTransformedShape( glyphOutline );
			addShape.append(
				glyphOutline,
				false );

			if (posPtr < xValues.length)
			{
				if (!relative)
				{
					cursorX = xValues[ posPtr ];
					cursorY = yValues[ posPtr++ ];
				}
				else
				{
					cursorX += xValues[ posPtr ];
					cursorY += yValues[ posPtr++ ];
				}

			}
		}
	}

	@Override
	public void render( Graphics2D g ) throws SVGException
	{
		float[] xValues;
		float[] yValues;
		boolean relative = false;
		if (x.isSet())
		{
			xValues = x.getFloatListValue();
			yValues = y.getFloatListValue();
		}
		else
		{
			relative = true;
			xValues = dx.getFloatListValue();
			yValues = dy.getFloatListValue();
		}

		if (!relative)
		{
			cursorX = xValues[ 0 ];
			cursorY = yValues[ 0 ];
		}
		else
		{
			cursorX += xValues[ 0 ];
			cursorY += yValues[ 0 ];
		}

		StyleAttribute sty = new StyleAttribute();

		String fontFamily = null;
		if (getPres( sty.withName( "font-family" ) ))
		{
			fontFamily = sty.getStringValue();
		}

		float fontSize = 12f;
		if (getPres( sty.withName( "font-size" ) ))
		{
			fontSize = sty.getFloatValueWithUnits();
		}

		//Get font
		Font font = diagram.getUniverse()
			.getFont(
				fontFamily );
		if (font == null)
		{
			System.err.println( "Could not load font" );
			java.awt.Font sysFont = new java.awt.Font( fontFamily,
				java.awt.Font.PLAIN,
				(int) fontSize );
			renderSysFont(
				g,
				sysFont );
			return;
		}

		FontFace fontFace = font.getFontFace();
		int ascent = fontFace.getAscent();
		float fontScale = fontSize / (float) ascent;

		AffineTransform oldXform = g.getTransform();
		AffineTransform xform = new AffineTransform();

		strokeWidthScalar = 1f / fontScale;

		int posPtr = 1;

		for (int i = 0; i < text.length(); ++i)
		{
			xform.setToTranslation(
				cursorX,
				cursorY );
			xform.scale(
				fontScale,
				fontScale );
			g.transform( xform );

			String unicode = text.substring(
				i,
				i + 1 );
			MissingGlyph glyph = font.getGlyph( unicode );

			Shape path = glyph.getPath();
			if (path != null)
			{
				renderShape(
					g,
					path );
			}
			else
			{
				glyph.render( g );
			}

			if (posPtr < xValues.length)
			{
				if (!relative)
				{
					cursorX = xValues[ posPtr ];
					cursorY = yValues[ posPtr++ ];
				}
				else
				{
					cursorX += xValues[ posPtr ];
					cursorY += yValues[ posPtr++ ];
				}

			}

			cursorX += fontScale * glyph.getHorizAdvX();

			g.setTransform( oldXform );
		}

		strokeWidthScalar = 1f;
	}

	protected void renderSysFont( Graphics2D g, java.awt.Font font )
		throws SVGException
	{
		int posPtr = 1;
		FontRenderContext frc = g.getFontRenderContext();

		Shape textShape = font.createGlyphVector(
			frc,
			text )
			.getOutline(
				cursorX,
				cursorY );
		renderShape(
			g,
			textShape );
		Rectangle2D rect = font.getStringBounds(
			text,
			frc );
		cursorX += (float) rect.getWidth();
	}

	public Shape getShape()
	{
		return null;
		//return shapeToParent(tspanShape);
	}

	public Rectangle2D getBoundingBox()
	{
		return null;
		//return boundsToParent(tspanShape.getBounds2D());
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
		//Tspan does not change
		return false;
	}

	public String getText()
	{
		return text;
	}

	public void setText( String text )
	{
		this.text = text;
	}

}
