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
import com.kitfox.svg.xml.attributes.CSSQuantity;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.ColorAttribute;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Stop extends SVGElement
{

	CSSQuantityAttribute offset = new CSSQuantityAttribute( "offset" )
	{

		@Override
		public void setValue( CSSQuantity value )
		{
			float quantity = value.getValue();
			if (quantity > 1.0f)
			{
				value.setValue( 1.0f );
			}
			if (quantity < 0f)
			{
				value.setValue( 0f );
			}
			super.setValue( value );
		}

	};
	CSSQuantityAttribute opacity = new CSSQuantityAttribute( "stop-opacity" );// 1f;
	ColorAttribute color = new ColorAttribute( "stop-color" );

	//Color  = Color.black;

	/** Creates a new instance of Stop */
	public Stop()
	{
		opacity.setDefault( 1f );
		bindStyleAttributes(
			color,
			opacity );
	}

	/*
	 public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent)
	 {
	 //Load style string
	 super.loaderStartElement(helper, attrs, parent);

	 String offset = attrs.getValue("offset");
	 this.offset = (float)XMLParseUtil.parseRatio(offset);

	 buildStop();
	 }
	 */

	@Override
	public boolean initStyleAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 's':
				if (setStyleAttribute(
					name,
					value,
					opacity,
					color ))
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
			case 'o':
				if (addPresentationAttribute(
					name,
					value,
					offset ))
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
	//        if (getPres(sty.setName("offset")))
	//        {
	//            offset = sty.getFloatValue();
	//            String units = sty.getUnits();
	//            if (units != null && units.equals("%")) offset /= 100f;
	//            if (offset > 1) offset = 1;
	//            if (offset < 0) offset = 0;
	//        }
	//        
	//        if (getStyle(sty.setName("stop-color"))) color = sty.getColorValue();
	//
	//        if (getStyle(sty.setName("stop-opacity"))) opacity = sty.getRatioValue();
	//    }

	/**
	 * Updates all attributes in this diagram associated with a time event.
	 * Ie, all attributes with track information.
	 * @return - true if this node has changed state as a result of the time
	 * update
	 */
	public boolean updateTime( double curTime ) throws SVGException
	{
		//        if (trackManager.getNumTracks() == 0) return false;

		//Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        
		//        if (getPres(sty.setName("offset")))
		//        {
		//            float newVal = sty.getFloatValue();
		//            if (newVal != offset)
		//            {
		//                offset = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("stop-color")))
		//        {
		//            Color newVal = sty.getColorValue();
		//            if (newVal != color)
		//            {
		//                color = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("stop-opacity")))
		//        {
		//            float newVal = sty.getFloatValue();
		//            if (newVal != opacity)
		//            {
		//                opacity = newVal;
		//                shapeChange = true;
		//            }
		//        }
		//        
		//        return shapeChange;
		return false;
	}
}
