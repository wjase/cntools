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

import com.kitfox.svg.xml.attributes.IntAttribute;
import com.kitfox.svg.xml.attributes.IntDefaultConstant;
import com.kitfox.svg.xml.attributes.IntDefaultValue;
import com.kitfox.svg.xml.attributes.StringAttribute;

/**
 * Implements an embedded font.
 *
 * SVG specification: http://www.w3.org/TR/SVG/fonts.html
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class FontFace extends SVGElement
{

	StringAttribute fontFamily = new StringAttribute( "font-family", null );

	/**
	 * Em size of coordinate system font is defined in
	 */
	IntAttribute unitsPerEm = new IntAttribute( "units-per-em" );

	IntAttribute ascent = new IntAttribute( "ascent" );

	IntAttribute descent = new IntAttribute( "descent" );

	IntAttribute ascentHeight = new IntAttribute( "ascent-height" );

	IntAttribute underlinePosition = new IntAttribute( "underline-position" );

	IntAttribute underlineThickness = new IntAttribute( "underline-thickness" );

	IntAttribute strikethroughPosition = new IntAttribute( "strikethrough-position" );

	IntAttribute strikethroughThickness = new IntAttribute( "strikethrough-thickenss" );

	IntAttribute overlinePosition = new IntAttribute( "overline-position" );

	IntAttribute overlineThickness = new IntAttribute( "overline-thickness" );

	/**
	 * Creates a new instance of Font
	 */
	public FontFace()
	{

		addPresentationAttributes(
			ascent,
									ascentHeight,
									descent,
									fontFamily,
									overlinePosition,
									overlineThickness,
									strikethroughPosition,
									strikethroughThickness,
									underlinePosition,
									underlineThickness,
									unitsPerEm );
		unitsPerEm.setDefaultSource( new IntDefaultConstant( 1000 ) );
		ascent.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue()
					- ( (Font) parent ).getVertOriginY();
			}

		} );

		descent.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return ( (Font) parent ).getVertOriginY();
			}

		} );

		ascentHeight.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return getAscent();
			}

		} );

		underlinePosition.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() * 5 / 6;
			}

		} );

		underlineThickness.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() / 20;
			}

		} );

		strikethroughPosition.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() * 3 / 6;
			}

		} );

		strikethroughThickness.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() / 20;
			}

		} );

		overlinePosition.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() * 5 / 6;
			}

		} );

		overlineThickness.setDefaultSource( new IntDefaultValue()
		{

			@Override
			public int get()
			{
				return unitsPerEm.getIntValue() / 20;
			}

		} );
	}

	/*
	 * public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 * {
	 * //Load style string
	 * super.loaderStartElement(helper, attrs, parent);
	 *
	 * fontFamily = attrs.getValue("font-family");
	 *
	 * String unitsPerEm = attrs.getValue("units-per-em");
	 * String ascent = attrs.getValue("ascent");
	 * String descent = attrs.getValue("descent");
	 * String accentHeight = attrs.getValue("accent-height");
	 *
	 * String underlinePosition = attrs.getValue("underline-position");
	 * String underlineThickness = attrs.getValue("underline-thickness");
	 * String strikethroughPosition = attrs.getValue("strikethrough-position");
	 * String strikethroughThickness = attrs.getValue("strikethrough-thickness");
	 * String overlinePosition = attrs.getValue("overline-position");
	 * String overlineThickness = attrs.getValue("overline-thickness");
	 *
	 *
	 * if (unitsPerEm != null) this.unitsPerEm = XMLParseUtil.parseInt(unitsPerEm);
	 * if (ascent != null) this.ascent = XMLParseUtil.parseInt(ascent);
	 * if (descent != null) this.descent = XMLParseUtil.parseInt(descent);
	 * if (accentHeight != null) this.accentHeight = XMLParseUtil.parseInt(accentHeight);
	 *
	 * if (underlinePosition != null) this.underlinePosition = XMLParseUtil.parseInt(underlinePosition);
	 * if (underlineThickness != null) this.underlineThickness = XMLParseUtil.parseInt(underlineThickness);
	 * if (strikethroughPosition != null) this.strikethroughPosition = XMLParseUtil.parseInt(strikethroughPosition);
	 * if (strikethroughThickness != null) this.strikethroughThickness = XMLParseUtil.parseInt(strikethroughThickness);
	 * if (overlinePosition != null) this.overlinePosition = XMLParseUtil.parseInt(overlinePosition);
	 * if (overlineThickness != null) this.overlineThickness = XMLParseUtil.parseInt(overlineThickness);
	 *
	 * // unitFontXform.setToScale(1.0 / (double)unitsPerEm, 1.0 / (double)unitsPerEm);
	 * }
	 */
	/*
	 * public void loaderEndElement(SVGLoaderHelper helper)
	 * {
	 * super.loaderEndElement(helper);
	 *
	 * build();
	 *
	 * // unitFontXform.setToScale(1.0 / (double)unitsPerEm, 1.0 / (double)unitsPerEm);
	 * }
	 */

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (value.charAt( 0 ))
		{
			case 'a':
				if (addPresentationAttribute(
					name,
					value,
					ascent,
					ascentHeight ))
				{
					return true;
				}
			case 'd':
				if (addPresentationAttribute(
					name,
					value,
					descent ))
				{
					return true;
				}
			case 'f':
				if (addPresentationAttribute(
					name,
					value,
					fontFamily ))
				{
					return true;
				}
			case 'o':
				if (addPresentationAttribute(
					name,
					value,
					overlinePosition,
					overlineThickness ))
				{
					return true;
				}
			case 's':
				if (addPresentationAttribute(
					name,
					value,
					strikethroughPosition,
					strikethroughThickness ))
				{
					return true;
				}
			case 'u':
				if (addPresentationAttribute(
					name,
					value,
					unitsPerEm,
					underlinePosition,
					underlineThickness ))
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
	//        if ( getPres( sty.setName( "font-family" ) ) )
	//        {
	//            fontFamily = sty.getStringValue();
	//        }
	//
	//        if ( getPres( sty.setName( "units-per-em" ) ) )
	//        {
	//            unitsPerEm = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "ascent" ) ) )
	//        {
	//            ascent = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "descent" ) ) )
	//        {
	//            descent = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "accent-height" ) ) )
	//        {
	//            accentHeight = sty.getIntValue();
	//        }
	//
	//        if ( getPres( sty.setName( "underline-position" ) ) )
	//        {
	//            underlinePosition = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "underline-thickness" ) ) )
	//        {
	//            underlineThickness = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "strikethrough-position" ) ) )
	//        {
	//            strikethroughPosition = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "strikethrough-thickenss" ) ) )
	//        {
	//            strikethroughThickness = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "overline-position" ) ) )
	//        {
	//            overlinePosition = sty.getIntValue();
	//        }
	//        if ( getPres( sty.setName( "overline-thickness" ) ) )
	//        {
	//            overlineThickness = sty.getIntValue();
	//        }
	//    }
	public String getFontFamily()
	{
		return fontFamily.getStringValue();
	}

	public int getUnitsPerEm()
	{
		return unitsPerEm.getIntValue();
	}

	public int getAscent()
	{
		return ascent.getIntValue();
	}

	public int getDescent()
	{
		return descent.getIntValue();
	}

	public int getAccentHeight()
	{
		return ascentHeight.getIntValue();
	}

	public int getUnderlinePosition()
	{
		return underlinePosition.getIntValue();
	}

	public int getUnderlineThickness()
	{
		return underlineThickness.getIntValue();
	}

	public int getStrikethroughPosition()
	{
		return strikethroughPosition.getIntValue();
	}

	public int getStrikethroughThickness()
	{
		return strikethroughThickness.getIntValue();
	}

	public int getOverlinePosition()
	{
		return overlinePosition.getIntValue();
	}

	public int getOverlineThickness()
	{
		return overlineThickness.getIntValue();
	}

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 *
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	@Override
	public boolean updateTime( double curTime )
	{
		//Fonts can't change
		return false;
	}

}
