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
import com.kitfox.svg.SVGParseException;
import com.kitfox.svg.xml.attributes.IntAttribute;
import com.kitfox.svg.xml.attributes.IntDefaultConstant;
import java.util.HashMap;

/**
 * Implements an embedded font.
 *
 * SVG specification: http://www.w3.org/TR/SVG/fonts.html
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Font extends SVGElement
{

	public int getVertOriginX()
	{
		return vertOriginX.getIntValue();
	}

	public int getVertOriginY()
	{
		return vertOriginY.getIntValue();
	}

	public int getVertAdvY()
	{
		return vertAdvY.getIntValue();
	}

	IntAttribute horizOriginX = new IntAttribute( "horiz-origin-x" );

	IntAttribute horizOriginY = new IntAttribute( "horiz-origin-y" );

	IntAttribute horizAdvX = new IntAttribute( "horiz-adv-x" ); ////, -1 Must be specified

	IntAttribute vertOriginX = new IntAttribute( "vert-origin-x" )//, -1
	{

		@Override
		public int getIntValue()
		{
			int val = super.getIntValue();
			if (val == -1)
			{
				val = getHorizAdvX() / 2;
			}
			return val;
		}

	};
	//Defaults to horizAdvX / 2

	IntAttribute vertOriginY = new IntAttribute( "vert-origin-y" )//, -1
	{

		@Override
		public int getIntValue()
		{
			int val = super.getIntValue();
			if (val == -1)
			{
				val = fontFace.getAscent();
			}
			return val;
		}

	};

	; //Defaults to font's ascent
	IntAttribute vertAdvY = new IntAttribute( "vert-adv-y" )//, -1
	{

		@Override
		public int getIntValue()
		{
			int val = super.getIntValue();
			if (val == -1)
			{
				val = fontFace.getUnitsPerEm();
			}
			return val;
		}

		;

	}; //Defaults to one 'em'.  See font-face

	FontFace fontFace = null;

	MissingGlyph missingGlyph = null;

	final HashMap glyphs = new HashMap();

	/**
	 * Creates a new instance of Font
	 */
	public Font()
	{
		IntDefaultConstant negOne = new IntDefaultConstant( -1 );
		horizAdvX.setDefaultSource( negOne );
		vertOriginX.setDefaultSource( negOne );
		vertOriginY.setDefaultSource( negOne );
		vertAdvY.setDefaultSource( negOne );

	}

	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 *
	 * if (horizOriginX != null) this.horizOriginX = XMLParseUtil.parseInt(horizOriginX);
	 * if (horizOriginY != null) this.horizOriginY = XMLParseUtil.parseInt(horizOriginY);
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

		if (child instanceof Glyph)
		{
			glyphs.put(
				( (Glyph) child ).getUnicode(),
				child );
		}
		else
			if (child instanceof MissingGlyph)
			{
				missingGlyph = (MissingGlyph) child;
			}
			else
				if (child instanceof FontFace)
				{
					fontFace = (FontFace) child;
				}
	}

	@Override
	public void loaderEndElement( SVGLoaderHelper helper )
		throws SVGParseException
	{
		super.loaderEndElement( helper );

		//build();

		helper.universe.registerFont( this );
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (value.charAt( 0 ))
		{
			case 'h':
				if (addPresentationAttribute(
					name,
					value,
					horizOriginX,
					horizOriginY,
					horizAdvX ))
				{
					return true;
				}
			case 'v':
				if (addPresentationAttribute(
					name,
					value,
					vertAdvY,
					vertOriginX,
					vertOriginY ))
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
	//        if ( getPres( sty.setName( "horiz-origin-x" ) ) )
	//        {
	//            horizOriginX = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "horiz-origin-y" ) ) )
	//        {
	//            horizOriginY = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "horiz-adv-x" ) ) )
	//        {
	//            horizAdvX = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "vert-origin-x" ) ) )
	//        {
	//            vertOriginX = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "vert-origin-y" ) ) )
	//        {
	//            vertOriginY = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "vert-adv-y" ) ) )
	//        {
	//            vertAdvY = sty.getIntValue();
	//        }
	//    }

	public FontFace getFontFace()
	{
		return fontFace;
	}

	public MissingGlyph getGlyph( String unicode )
	{
		Glyph retVal = (Glyph) glyphs.get( unicode );
		if (retVal == null)
		{
			return missingGlyph;
		}
		return retVal;
	}

	public int getHorizOriginX()
	{
		return horizOriginX.getIntValue();
	}

	public int getHorizOriginY()
	{
		return horizOriginY.getIntValue();
	}

	public int getHorizAdvX()
	{
		return horizAdvX.getIntValue();
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
		/*
		 * if (trackManager.getNumTracks() == 0) return false;
		 *
		 * //Get current values for parameters
		 * StyleAttribute sty = new StyleAttribute();
		 * boolean stateChange = false;
		 *
		 * if (getPres(sty.setName("horiz-origin-x")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != horizOriginX)
		 * {
		 * horizOriginX = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * if (getPres(sty.setName("horiz-origin-y")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != horizOriginY)
		 * {
		 * horizOriginY = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * if (getPres(sty.setName("horiz-adv-x")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != horizAdvX)
		 * {
		 * horizAdvX = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * if (getPres(sty.setName("vert-origin-x")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != vertOriginX)
		 * {
		 * vertOriginX = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * if (getPres(sty.setName("vert-origin-y")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != vertOriginY)
		 * {
		 * vertOriginY = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * if (getPres(sty.setName("vert-adv-y")))
		 * {
		 * int newVal = sty.getIntValue();
		 * if (newVal != vertAdvY)
		 * {
		 * vertAdvY = newVal;
		 * stateChange = true;
		 * }
		 * }
		 *
		 * return shapeChange;
		 */
	}

}
