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
import com.kitfox.svg.xml.attributes.ElementReferenceAttribute;
import com.kitfox.svg.xml.attributes.IntAttribute;
import com.kitfox.svg.xml.attributes.IntDefaultConstant;
import com.kitfox.svg.xml.attributes.TransformAttribute;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
abstract public class Gradient extends FillElement
{

	public static final int SM_PAD = 0;

	public static final int SM_REPEAT = 1;

	public static final int SM_REFLECT = 2;

	//int spreadMethod = SM_PAD;
	IntAttribute spreadMethod = new IntAttribute( "spreadMethod" )
	{

		@Override
		public void setStringValue( String value )
		{
			super.setOriginalString( value );
			int val = value.equals( "repeat" ) ? SM_REPEAT :
					( value.equals( "reflect" ) ? SM_REFLECT : SM_PAD );
			setValue( val );
		}

	};

	public int getSpreadMethod()
	{
		if (spreadMethod.isSet())
		{
			return spreadMethod.getIntValue();
		}
		if (stopRef.isSet())
		{
			Gradient grad = (Gradient) stopRef.getElement();//diagram.getUniverse().getElement(stopRef);                
			return grad.getSpreadMethod();
		}
		return spreadMethod.getIntValue();
	}

	public static final int GU_OBJECT_BOUNDING_BOX = 0;

	public static final int GU_USER_SPACE_ON_USE = 1;

	//protected int gradientUnits = GU_OBJECT_BOUNDING_BOX;
	IntAttribute gradientUnits = new IntAttribute( "gradientUnits" )
	{

		@Override
		public void setStringValue( String value )
		{
			super.setOriginalString( value );
			setValue( value.equalsIgnoreCase( "userspaceonuse" ) ?
					GU_USER_SPACE_ON_USE :
					GU_OBJECT_BOUNDING_BOX );

		}

	};

	public int getGradientUnits()
	{
		if (gradientUnits.isSet())
		{
			return gradientUnits.getIntValue();
		}
		if (stopRef.isSet())
		{
			Gradient grad = (Gradient) stopRef.getElement();//diagram.getUniverse().getElement(stopRef);                
			return grad.getGradientUnits();
		}
		return gradientUnits.getIntValue();
	}

	//Either this gradient contains a list of stops, or it will take it's
	// stops from the referenced gradient
	protected ArrayList stops = new ArrayList();

	ElementReferenceAttribute stopRef = new ElementReferenceAttribute( "xlink:href",
		this );

	//    Gradient stopRef = null;
	protected TransformAttribute gradientTransform = new TransformAttribute( "gradientTransform" );

	//protected AffineTransform  = new AffineTransform();

	public AffineTransform getTransform()
	{
		if (stopRef.isSet())
		{
			Gradient grad = (Gradient) stopRef.getElement();//diagram.getUniverse().getElement(stopRef);                
			return grad.getTransform();
		}
		return gradientTransform.getXform();

	}

	//Cache arrays of stop values here

	protected float[] stopFractions;

	protected Color[] stopColors;

	/**
	 * Creates a new instance of Gradient
	 */
	public Gradient()
	{
		spreadMethod.setDefaultSource( new IntDefaultConstant( SM_PAD ) );
		gradientUnits.setDefaultSource( new IntDefaultConstant( GU_OBJECT_BOUNDING_BOX ) );

	}

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

		if (!( child instanceof Stop ))
		{
			return;
		}
		appendStop( (Stop) child );
	}

	@Override
	public boolean initAtttributeValue( String name, String value )
	{
		switch (name.charAt( 0 ))
		{
			case 's':
				if (addPresentationAttribute(
					name,
					value,
					spreadMethod ))
				{
					return true;
				}
			case 'x': //xlink-href
				if (addPresentationAttribute(
					name,
					value,
					stopRef ))
				{
					return true;
				}
			case 'g':
				if (addPresentationAttribute(
					name,
					value,
					gradientUnits,
					gradientTransform ))
				{
					return true;
				}
		}
		return super.initAtttributeValue(
			name,
			value );
	}

	//    @Override
	//    protected void build() throws SVGException
	//    {
	//        super.build();
	//        
	//        StyleAttribute sty = new StyleAttribute();
	//        String strn;
	//        
	//        if (getPres(sty.setName("spreadMethod")))
	//        {
	//            strn = sty.getStringValue().toLowerCase();
	//            if (strn.equals("repeat")) spreadMethod = SM_REPEAT;
	//            else if (strn.equals("reflect")) spreadMethod = SM_REFLECT;
	//            else spreadMethod = SM_PAD;
	//        }
	//
	//        if (getPres(sty.setName("gradientUnits")))
	//        {
	//            strn = sty.getStringValue().toLowerCase();
	//            if (strn.equals("userspaceonuse")) gradientUnits = GU_USER_SPACE_ON_USE;
	//            else gradientUnits = GU_OBJECT_BOUNDING_BOX;
	//        }
	//
	//        if (getPres(sty.setName("gradientTransform"))) gradientTransform = parseTransform(sty.getStringValue());
	//        //If we still don't have one, set it to identity
	//        if (gradientTransform == null) gradientTransform = new AffineTransform();
	//
	//        
	//        //Check to see if we're using our own stops or referencing someone else's
	//        if (getPres(sty.setName("xlink:href")))
	//        {
	//            try {
	//                stopRef = sty.getURIValue(getXMLBase());
	////System.err.println("Gradient: " + sty.getStringValue() + ", " + getXMLBase() + ", " + src);
	////                URI src = getXMLBase().resolve(href);
	////                stopRef = (Gradient)diagram.getUniverse().getElement(src);
	//            }
	//            catch (Exception e)
	//            {
	//                throw new SVGException("Could not resolve relative URL in Gradient: " + sty.getStringValue() + ", " + getXMLBase(), e);
	//            }
	//        }
	//    }
	public float[] getStopFractions()
	{
		if (stopRef.isSet())
		{
			Gradient grad = (Gradient) stopRef.getElement();//diagram.getUniverse().getElement(stopRef);    
			if (grad != null)
			{
				return grad.getStopFractions();
			}
		}

		if (stopFractions != null)
		{
			return stopFractions;
		}

		stopFractions = new float[ stops.size() ];
		int idx = 0;
		for (Iterator it = stops.iterator(); it.hasNext();)
		{
			Stop stop = (Stop) it.next();
			float val = stop.offset.getNormalisedValue();
			if (idx != 0 && val < stopFractions[ idx - 1 ])
			{
				val = stopFractions[ idx - 1 ];
			}
			stopFractions[ idx++ ] = val;
		}

		return stopFractions;
	}

	public Color[] getStopColors()
	{
		if (stopRef.isSet())
		{
			Gradient grad = (Gradient) stopRef.getElement();//diagram.getUniverse().getElement(stopRef);    
			if (grad != null)
			{
				return grad.getStopColors();
			}
		}

		if (stops.isEmpty())
		{
			throw new IllegalArgumentException( "no stops for " + getId() );
		}
		if (stopColors != null)
		{
			return stopColors;
		}

		stopColors = new Color[ stops.size() ];
		int idx = 0;
		for (Iterator it = stops.iterator(); it.hasNext();)
		{
			Stop stop = (Stop) it.next();
			int stopColorVal = stop.color.getColorValue()
				.getRGB();
			Color stopColor = new Color( ( stopColorVal >> 16 ) & 0xff,
				( stopColorVal >> 8 ) & 0xff,
											stopColorVal & 0xff,
				clamp(
					(int) ( stop.opacity.getNormalisedValue() * 255 ),
																		0,
					255 ) );
			stopColors[ idx++ ] = stopColor;
		}

		return stopColors;
	}

	public void setStops( Color[] colors, float[] fractions )
	{
		if (colors.length != fractions.length)
		{
			throw new IllegalArgumentException();
		}

		this.stopColors = colors;
		this.stopFractions = fractions;
		stopRef = null;
	}

	private int clamp( int val, int min, int max )
	{
		if (val < min)
		{
			return min;
		}
		if (val > max)
		{
			return max;
		}
		return val;
	}

	public void setStopRef( URI grad )
	{
		stopRef.setValue( grad );
	}

	public void appendStop( Stop stop )
	{
		stops.add( stop );
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
		//        boolean stateChange = false;
		//
		//        //Get current values for parameters
		//        StyleAttribute sty = new StyleAttribute();
		//        boolean shapeChange = false;
		//        String strn;
		//        
		//
		//        if (getPres(sty.setName("spreadMethod")))
		//        {
		//            int newVal;
		//            strn = sty.getStringValue().toLowerCase();
		//            if (strn.equals("repeat")) newVal = SM_REPEAT;
		//            else if (strn.equals("reflect")) newVal = SM_REFLECT;
		//            else newVal = SM_PAD;
		//            if (spreadMethod != newVal)
		//            {
		//                spreadMethod = newVal;
		//                stateChange = true;
		//            }
		//        }
		//        
		//        if (getPres(sty.setName("gradientUnits")))
		//        {
		//            int newVal;
		//            strn = sty.getStringValue().toLowerCase();
		//            if (strn.equals("userspaceonuse")) newVal = GU_USER_SPACE_ON_USE;
		//            else newVal = GU_OBJECT_BOUNDING_BOX;
		//            if (newVal != gradientUnits)
		//            {
		//                gradientUnits = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        if (getPres(sty.setName("gradientTransform")))
		//        {
		//            AffineTransform newVal = parseTransform(sty.getStringValue());
		//            if (newVal != null && newVal.equals(gradientTransform))
		//            {
		//                gradientTransform = newVal;
		//                stateChange = true;
		//            }
		//        }
		//
		//        
		//        //Check to see if we're using our own stops or referencing someone else's
		//        if (getPres(sty.setName("xlink:href")))
		//        {
		//            try {
		//                URI newVal = sty.getURIValue(getXMLBase());
		//                if ((newVal == null && stopRef != null) || !newVal.equals(stopRef))
		//                {
		//                    stopRef = newVal;
		//                    stateChange = true;
		//                }
		//            }
		//            catch (Exception e)
		//            {
		//                e.printStackTrace();
		//            }
		//        }
		//        
		//        //Check stops, if any
		//        for (Iterator it = stops.iterator(); it.hasNext();)
		//        {
		//            Stop stop = (Stop)it.next();
		//            if (stop.updateTime(curTime))
		//            {
		//                stateChange = true;
		//                stopFractions = null;
		//                stopColors = null;
		//            }
		//        }
		//        
		//        return stateChange;
		return false;
	}

}
