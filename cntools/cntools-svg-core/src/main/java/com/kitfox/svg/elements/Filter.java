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

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.xml.attributes.CSSQuantityAttribute;
import com.kitfox.svg.xml.attributes.IntAttribute;
import com.kitfox.svg.xml.attributes.IntDefaultConstant;
import com.kitfox.svg.xml.attributes.RelativeURIAttribute;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class Filter extends SVGElement
{

	public static final int FU_OBJECT_BOUNDING_BOX = 0;

	public static final int FU_USER_SPACE_ON_USE = 1;

	protected IntAttribute filterUnits = new IntAttribute( "filterUnits" )
	{

		@Override
		public void setStringValue( String value )
		{
			setValue( value.equals( "userspaceonuse" ) ?
					FU_OBJECT_BOUNDING_BOX :
					FU_USER_SPACE_ON_USE );
		}

	};

	public static final int PU_OBJECT_BOUNDING_BOX = 0;

	public static final int PU_USER_SPACE_ON_USE = 1;

	//    protected int primitiveUnits = PU_OBJECT_BOUNDING_BOX;
	protected IntAttribute primitiveUnits = new IntAttribute( "primitiveUnits" )
	{

		@Override
		public void setStringValue( String value )
		{
			setValue( value.equals( "userspaceonuse" ) ?
					PU_USER_SPACE_ON_USE :
					PU_OBJECT_BOUNDING_BOX );
		}

	};

	private CSSQuantityAttribute x = new CSSQuantityAttribute( "x" );

	private CSSQuantityAttribute y = new CSSQuantityAttribute( "y" );

	private CSSQuantityAttribute width = new CSSQuantityAttribute( "width" );

	private CSSQuantityAttribute height = new CSSQuantityAttribute( "height" );

	Point2D filterRes = new Point2D.Double();

	RelativeURIAttribute xlinkHref = new RelativeURIAttribute( "xlink:href",
		this );

	final ArrayList filterEffects = new ArrayList();

	/**
	 * Creates a new instance of FillElement
	 */
	public Filter()
	{
		filterUnits.setDefaultSource( new IntDefaultConstant( FU_OBJECT_BOUNDING_BOX ) );
		primitiveUnits.setDefaultSource( new IntDefaultConstant( PU_OBJECT_BOUNDING_BOX ) );

		addPresentationAttributes(
			x,
			y,
			width,
			height,
			xlinkHref,
			primitiveUnits,
			filterUnits );

	}

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

		if (child instanceof FilterEffects)
		{
			filterEffects.add( child );
		}
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 'f':
				if (addPresentationAttribute(
					name,
					value,
					filterUnits ))
				{
					return true;
				}
			case 'x':
				if (addPresentationAttribute(
					name,
					value,
					x,
					xlinkHref ))
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
			case 'p':
				if (addPresentationAttribute(
					name,
					value,
					primitiveUnits ))
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

	//        if ( getPres( sty.setName( "filterUnits" ) ) )
	//        {
	//            strn = sty.getStringValue().toLowerCase();
	//            if ( strn.equals( "userspaceonuse" ) )
	//            {
	//                filterUnits = FU_USER_SPACE_ON_USE;
	//            }
	//            else
	//            {
	//                filterUnits = FU_OBJECT_BOUNDING_BOX;
	//            }
	//        }
	//
	//        if ( getPres( sty.setName( "primitiveUnits" ) ) )
	//        {
	//            strn = sty.getStringValue().toLowerCase();
	//            if ( strn.equals( "userspaceonuse" ) )
	//            {
	//                primitiveUnits = PU_USER_SPACE_ON_USE;
	//            }
	//            else
	//            {
	//                primitiveUnits = PU_OBJECT_BOUNDING_BOX;
	//            }
	//        }
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

	//        try
	//        {
	//            if ( getPres( sty.setName( "xlink:href" ) ) )
	//            {
	//                URI src = sty.getURIValue( getXMLBase() );
	//                xlinkHref = src.toURL();
	//            }
	//        }
	//        catch ( Exception e )
	//        {
	//            throw new SVGException( e );
	//        }
	//
	//    }

	public float getX()
	{
		return x.getNormalisedValue();
	}

	public float getY()
	{
		return y.getNormalisedValue();
	}

	public float getWidth()
	{
		return width.getNormalisedValue();
	}

	public float getHeight()
	{
		return height.getNormalisedValue();
	}

	@Override
	public boolean updateTime( double curTime ) throws SVGException
	{
		////        if (trackManager.getNumTracks() == 0) return false;
		//
		//        //Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean stateChange = false;
		//
		//        if ( getPres( sty.setName( "x" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != x )
		//            {
		//                x = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "y" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != y )
		//            {
		//                y = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "width" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != width )
		//            {
		//                width = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "height" ) ) )
		//        {
		//            float newVal = sty.getFloatValueWithUnits();
		//            if ( newVal != height )
		//            {
		//                height = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        try
		//        {
		//            if ( getPres( sty.setName( "xlink:href" ) ) )
		//            {
		//                URI src = sty.getURIValue( getXMLBase() );
		//                URL newVal = src.toURL();
		//
		//                if ( !newVal.equals( xlinkHref ) )
		//                {
		//                    xlinkHref = newVal;
		//                    stateChange = true;
		//                }
		//            }
		//        }
		//        catch ( Exception e )
		//        {
		//            throw new SVGException( e );
		//        }
		//
		//        if ( getPres( sty.setName( "filterUnits" ) ) )
		//        {
		//            int newVal;
		//            String strn = sty.getStringValue().toLowerCase();
		//            if ( strn.equals( "userspaceonuse" ) )
		//            {
		//                newVal = FU_USER_SPACE_ON_USE;
		//            }
		//            else
		//            {
		//                newVal = FU_OBJECT_BOUNDING_BOX;
		//            }
		//            if ( newVal != filterUnits )
		//            {
		//                filterUnits = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        if ( getPres( sty.setName( "primitiveUnits" ) ) )
		//        {
		//            int newVal;
		//            String strn = sty.getStringValue().toLowerCase();
		//            if ( strn.equals( "userspaceonuse" ) )
		//            {
		//                newVal = PU_USER_SPACE_ON_USE;
		//            }
		//            else
		//            {
		//                newVal = PU_OBJECT_BOUNDING_BOX;
		//            }
		//            if ( newVal != filterUnits )
		//            {
		//                primitiveUnits = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//
		//
		//        return stateChange;
		return true;
	}

}
