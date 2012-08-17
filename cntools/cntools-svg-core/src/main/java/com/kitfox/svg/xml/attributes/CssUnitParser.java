/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.CSSUnits;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class CssUnitParser
{

	static public float convertUnitsToFloat( CSSUnits unitType, float value )
	{
		if (unitType == CSSUnits.UT_UNITLESS || unitType == CSSUnits.UT_PERCENT)
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
			case UT_IN:
				return value * pixPerInch;
			case UT_CM:
				return value * inchesPerCm * pixPerInch;
			case UT_MM:
				return value * .1f * inchesPerCm * pixPerInch;
			case UT_PT:
				return value * ( 1f / 72f ) * pixPerInch;
			case UT_PC:
				return value * ( 1f / 6f ) * pixPerInch;
			case UT_PERCENT:
				return value / 100f;
		}

		return value;
	}

	private static final Pattern matchFpNumUnitsPattern = Pattern.compile(
			"\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(px|cm|mm|in|pc|pt|em|ex|%)\\s*" );

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
			unitStr = "PERCENT";
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

}
