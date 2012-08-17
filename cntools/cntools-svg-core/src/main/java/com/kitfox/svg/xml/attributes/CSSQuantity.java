/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.CSSUnits;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class CSSQuantity
{

	private CSSUnits units = CSSUnits.UT_UNITLESS;

	private float value;

	private float calculatedValue = Float.NaN;

	boolean needsCalc = true; // to save recalculation

	private static final Pattern matchFpNumUnitsPattern = Pattern.compile(
			"^\\s*([-+]?(?:[\\d\\.]+)([eE][-+]?\\d+)?)\\s*(px|cm|mm|in|pc|pt|em|ex|%)?\\s*$" );

	public CSSQuantity()
	{
	}

	public CSSQuantity( float value )
	{
		this.value = value;
	}

	public CSSQuantity( float value, CSSUnits units )
	{
		this.value = value;
		this.units = units;
	}

	public CSSUnits getUnits()
	{
		return units;
	}

	public void setUnits( CSSUnits units )
	{
		this.units = units;
	}

	public float getValue()
	{
		return value;
	}

	public void setValue( float value )
	{
		this.value = value;
		needsCalc = true;
	}

	public float getNormalisedValue()
	{
		if (needsCalc)
		{
			needsCalc = false;
			calculatedValue = calculateValueUsingUnits(
				units,
				value );
		}
		return calculatedValue;
	}

	public float getNormalisedValue( float parentValue )
	{
		if (units == CSSUnits.UT_PERCENT)
		{
			return getNormalisedValue() * parentValue;
		}
		return getNormalisedValue();
	}

	// conversion constants
	//======================
	//default to 72 dpi
	private static final int DEFAULT_PIXELS_PER_INCH = 72;

	private static final float INCHES_PER_CM = .3936f;

	private static final float PTS_PER_INCH = 1f / 72f;

	private static final float PICAS_PER_INCH = 1f / 6f;

	private static float pixPerInch = DEFAULT_PIXELS_PER_INCH;

	private static boolean calcDpi = true;

	public static float getPixelsPerInch()
	{
		if (calcDpi)
		{
			calcDpi = false;
			try
			{
				pixPerInch = (float) Toolkit.getDefaultToolkit()
					.getScreenResolution();
			}
			catch (HeadlessException ex)
			{
			}

		}
		return pixPerInch;
	}

	static public float calculateValueUsingUnits( CSSUnits unitType, float value )
	{

		switch (unitType)
		{
			case UT_UNITLESS:
				return value;
			case UT_IN:
				return value * pixPerInch;
			case UT_CM:
				return value * INCHES_PER_CM * getPixelsPerInch();
			case UT_MM:
				return value * .1f * INCHES_PER_CM * getPixelsPerInch();
			case UT_PT:
				return value * PTS_PER_INCH * getPixelsPerInch();
			case UT_PC:
				return value * PICAS_PER_INCH * getPixelsPerInch();
			case UT_PERCENT:
				return value / 100f;
		}

		return value;
	}

	public boolean parse( String input )
	{
		//CSSUnits.
		Matcher matchFpNumUnits = matchFpNumUnitsPattern.matcher( input );

		if (!matchFpNumUnits.matches())
		{
			return false;
		}

		String numberStr = matchFpNumUnits.group( 1 );
		this.value = Float.parseFloat( numberStr );

		String unitStr = matchFpNumUnits.group( 3 );

		if (unitStr != null)
		{

			unitStr = unitStr.toUpperCase();

			CSSUnits parsedUnits;
			if (unitStr.equals( "%" ))
			{
				parsedUnits = CSSUnits.UT_PERCENT;
			}
			else
			{
				parsedUnits = CSSUnits.valueOf( "UT_" + unitStr );
			}

			this.units = parsedUnits;
		}
		needsCalc = true;
		return true;
	}

	public CSSUnits parseUnits( String input )
	{
		//CSSUnits.
		Matcher matchFpNumUnits = matchFpNumUnitsPattern.matcher( input );
		if (!matchFpNumUnits.matches())
		{
			return null;
		}

		String unitStr = matchFpNumUnits.group(
			6 )
			.toUpperCase();
		if (unitStr.equals( "%" ))
		{
			return CSSUnits.UT_PERCENT;
		}
		return CSSUnits.valueOf( "UT_" + unitStr );
	}

	public static String unitsAsString( CSSUnits unitIdx )
	{
		switch (unitIdx)
		{
			default:
				return "";
			case UT_PX:
				return "px";
			case UT_CM:
				return "cm";
			case UT_MM:
				return "mm";
			case UT_IN:
				return "in";
			case UT_EM:
				return "em";
			case UT_EX:
				return "ex";
			case UT_PT:
				return "pt";
			case UT_PC:
				return "pc";
			case UT_PERCENT:
				return "%";
		}
	}

	void setValue( float value, CSSUnits units )
	{
		this.value = value;
		this.units = units;
		needsCalc = true;
	}

	public String asValueWithUnits()
	{
		StringBuilder sb = new StringBuilder();
		append(
			sb,
			value );
		sb.append( unitsAsString( units ) );
		return sb.toString();
	}

	private static DecimalFormat defaultFormat = new DecimalFormat( "#.#####" );

	private static void append( StringBuilder sb, double d )
	{
		sb.append( defaultFormat.format( d ) );
	}

}
