/*
 * StyleAttribute.java
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
 * Created on January 27, 2004, 2:53 PM
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.ColorTable;
import com.kitfox.svg.xml.NumberWithUnits;
import com.kitfox.svg.xml.XMLParseUtil;
import com.kitfox.svg.xml.attributes.IStyleAttribute;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mark McKay
 * @author <a href="mailto:mark@kitfox.com">Mark McKay</a>
 */
public class StyleAttribute implements IStyleAttribute, IStringAttribute
{

	private boolean set = false;

	@Override
	public boolean isSet()
	{
		return set;
	}

	public static final long serialVersionUID = 0;

	public static final Pattern matchUrlPattern = Pattern.compile( "\\s*url\\((.*)\\)\\s*" );

	public static final Pattern matchFpNumUnitsPattern = Pattern.compile( "\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(px|cm|mm|in|pc|pt|em|ex)\\s*" );

	private String name;

	private String stringValue;

	private boolean colorCompatable = false;

	private boolean urlCompatable = false;

	/**
	 * Creates a new instance of StyleAttribute
	 */
	public StyleAttribute()
	{
		this( null, null );
	}

	public StyleAttribute( String name )
	{
		this.name = name;
		stringValue = null;
	}

	public StyleAttribute( String name, String stringValue )
	{
		this.name = name;
		this.stringValue = stringValue;
		set = true;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName( String name )
	{
		this.name = name;
	}

	public StyleAttribute withName( String name )
	{
		setName( name );
		set = false;

		return this;
	}

	@Override
	public String getStringValue()
	{
		return stringValue;
	}

	@Override
	public String[] getStringList()
	{
		return XMLParseUtil.parseStringList( stringValue );
	}

	@Override
	public void setStringValue( String value )
	{
		stringValue = value;
		set = true;

	}

	public boolean getBooleanValue()
	{
		return stringValue.toLowerCase()
			.equals(
				"true" );
	}

	@Override
	public int getIntValue()
	{
		return XMLParseUtil.findInt( stringValue );
	}

	@Override
	public int[] getIntList()
	{
		return XMLParseUtil.parseIntList( stringValue );
	}

	public double getDoubleValue()
	{
		return XMLParseUtil.findDouble( stringValue );
	}

	@Override
	public double[] getDoubleList()
	{
		return XMLParseUtil.parseDoubleList( stringValue );
	}

	public float getFloatValue()
	{
		return XMLParseUtil.findFloat( stringValue );
	}

	@Override
	public float[] getFloatList()
	{
		return XMLParseUtil.parseFloatList( stringValue );
	}

	@Override
	public float getRatioValue()
	{
		return (float) XMLParseUtil.parseRatio( stringValue );
		//        try { return Float.parseFloat(stringValue); }
		//        catch (Exception e) {}
		//        return 0f;
	}

	@Override
	public String getUnits()
	{
		Matcher matchFpNumUnits = matchFpNumUnitsPattern.matcher( stringValue );
		if (!matchFpNumUnits.matches())
		{
			return null;
		}
		return matchFpNumUnits.group( 6 );
	}

	@Override
	public NumberWithUnits getNumberWithUnits()
	{
		return XMLParseUtil.parseNumberWithUnits( stringValue );
	}

	@Override
	public float getFloatValueWithUnits()
	{
		NumberWithUnits number = getNumberWithUnits();
		return convertUnitsToPixels(
			number.getUnits(),
			number.getValue() );
	}

	static public float convertUnitsToPixels( int unitType, float value )
	{
		if (unitType == NumberWithUnits.UT_UNITLESS
			|| unitType == NumberWithUnits.UT_PERCENT)
		{
			return value;
		}

		float pixPerInch;
		try
		{
			pixPerInch = (float) Toolkit.getDefaultToolkit()
				.getScreenResolution();
		}
		catch (HeadlessException ex)
		{
			//default to 72 dpi
			pixPerInch = 72;
		}
		final float inchesPerCm = .3936f;

		switch (unitType)
		{
			case NumberWithUnits.UT_IN:
				return value * pixPerInch;
			case NumberWithUnits.UT_CM:
				return value * inchesPerCm * pixPerInch;
			case NumberWithUnits.UT_MM:
				return value * .1f * inchesPerCm * pixPerInch;
			case NumberWithUnits.UT_PT:
				return value * ( 1f / 72f ) * pixPerInch;
			case NumberWithUnits.UT_PC:
				return value * ( 1f / 6f ) * pixPerInch;
		}

		return value;
	}

	public Color getColorValue()
	{
		return ColorTable.parseColor( stringValue );
	}

	@Override
	public String parseURLFn() throws Exception
	{
		Matcher matchUrl = matchUrlPattern.matcher( stringValue );

		try
		{
			if (!matchUrl.matches())
			{
				return null;
			}
			return matchUrl.group( 1 );
		}
		catch (Exception e)
		{
			Logger.getAnonymousLogger()
				.log(
					Level.WARNING,
					"Unable to parse attribute as URL:" + stringValue );
			throw e;
		}
	}

	@Override
	public URL getURLValue( URL docRoot )
	{

		try
		{
			String fragment = parseURLFn();
			if (fragment == null)
			{
				return null;
			}

			return new URL( docRoot, fragment );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public URL getURLValue( URI docRoot )
	{
		try
		{
			String fragment = parseURLFn();
			if (fragment == null)
			{
				return null;
			}
			URI ref = docRoot.resolve( fragment );
			return ref.toURL();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public URI getURIValue()
	{
		return getURIValue( null );
	}

	/**
	 * Parse this sytle attribute as a URL and return it in URI form resolved
	 * against the passed base.
	 *
	 * @param base - URI to resolve against. If null, will return value without
	 * attempting to resolve it.
	 */
	@Override
	public URI getURIValue( URI base )
	{
		try
		{
			String fragment = parseURLFn();
			if (fragment == null)
			{
				fragment = stringValue.replaceAll(
					"\\s+",
					"" );
			}
			if (fragment == null)
			{
				return null;
			}

			//======================
			//This gets around a bug in the 1.5.0 JDK
			if (Pattern.matches(
				"[a-zA-Z]:!\\\\.*",
				fragment ))
			{
				File file = new File( fragment );
				return file.toURI();
			}
			//======================

			//[scheme:]scheme-specific-part[#fragment]

			URI uriFrag = new URI( fragment );
			if (uriFrag.isAbsolute())
			{
				//Has scheme
				return uriFrag;
			}

			if (base == null)
			{
				return uriFrag;
			}

			URI relBase = new URI( null, base.getSchemeSpecificPart(), null );
			URI relUri;
			if (relBase.isOpaque())
			{
				relUri = new URI( null,
					base.getSchemeSpecificPart(),
					uriFrag.getFragment() );
			}
			else
			{
				relUri = relBase.resolve( uriFrag );
			}
			return new URI( base.getScheme() + ":" + relUri );
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void main( String[] args )
	{
		try
		{
			URI uri = new URI( "jar:http://www.kitfox.com/jackal/jackal.jar!/res/doc/about.svg" );
			uri = uri.resolve( "#myFragment" );

			System.err.println( uri.toString() );

			uri = new URI( "http://www.kitfox.com/jackal/jackal.html" );
			uri = uri.resolve( "#myFragment" );

			System.err.println( uri.toString() );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String asSVGString()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

}
