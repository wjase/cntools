/*
 * FillElement.java
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
 * Created on March 18, 2004, 6:52 AM
 */
package com.kitfox.svg.elements;

import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class FePointLight extends FeLight
{

	CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	CSSQuantityAttribute z = new CSSQuantityAttribute( "z" );

	/**
	 * Creates a new instance of FillElement
	 */
	public FePointLight()
	{
		addPresentationAttributes(
			x,
			y,
			z );
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
			case 'z':
				if (addPresentationAttribute(
					name,
					value,
					z ))
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
	//        String strn;
	//        
	//        if (getPres(sty.setName("x"))) x = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("y"))) y = sty.getFloatValueWithUnits();
	//        
	//        if (getPres(sty.setName("z"))) z = sty.getFloatValueWithUnits();
	//    }
	public float getX()
	{
		return x.getNormalisedValue();
	}

	public float getY()
	{
		return y.getNormalisedValue();
	}

	public float getZ()
	{
		return z.getNormalisedValue();
	}

	//    public boolean updateTime(double curTime) throws SVGException
	//    {
	////        if (trackManager.getNumTracks() == 0) return false;
	//
	//        //Get current values for parameters
	//        StyleAttribute sty = new StyleAttribute();
	//        boolean stateChange = false;
	//        
	//        if (getPres(sty.setName("x")))
	//        {
	//            float newVal = sty.getFloatValueWithUnits();
	//            if (newVal != x)
	//            {
	//                x = newVal;
	//                stateChange = true;
	//            }
	//        }
	//        
	//        if (getPres(sty.setName("y")))
	//        {
	//            float newVal = sty.getFloatValueWithUnits();
	//            if (newVal != y)
	//            {
	//                y = newVal;
	//                stateChange = true;
	//            }
	//        }
	//        
	//        if (getPres(sty.setName("z")))
	//        {
	//            float newVal = sty.getFloatValueWithUnits();
	//            if (newVal != z)
	//            {
	//                z = newVal;
	//                stateChange = true;
	//            }
	//        }
	//        
	//        return stateChange;
	//    }
}
