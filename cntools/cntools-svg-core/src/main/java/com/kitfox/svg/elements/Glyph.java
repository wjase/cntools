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

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.attributes.StringAttribute;

// import org.apache.batik.ext.awt.geom.ExtendedGeneralPath;

/**
 * Implements an embedded font.
 *
 * SVG specification: http://www.w3.org/TR/SVG/fonts.html
 *
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Glyph extends MissingGlyph
{
	/**
	 * One or more characters indicating the unicode sequence that denotes
	 * this glyph.
	 */
	StringAttribute unicode = new StringAttribute( "unicode" );

	/** Creates a new instance of Font */
	public Glyph()
	{
		addPresentationAttribute( unicode );
	}

	/*
	 public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 {
	 //Load style string
	 super.loaderStartElement(helper, attrs, parent);

	 //Get unicode sequence that maps to this glyph
	 unicode = attrs.getValue("unicode");
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
			case 'u':
				if (addPresentationAttribute(
					name,
					value,
					unicode ))
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
	//        if (getPres(sty.setName("unicode"))) unicode = sty.getStringValue();
	//    }

	public String getUnicode()
	{
		return unicode.getStringValue();
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
		//Fonts can't change
		return false;
	}
}
