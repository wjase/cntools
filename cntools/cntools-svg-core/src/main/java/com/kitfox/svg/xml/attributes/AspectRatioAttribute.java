/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class AspectRatioAttribute extends NamedAttribute
{

	public AspectRatioAttribute( String name )
	{
		super( name );
	}

	private boolean set = false;

	@Override
	public boolean isSet()
	{
		return set;
	}

	public static final int PA_X_NONE = 0;

	public static final int PA_X_MIN = 1;

	public static final int PA_X_MID = 2;

	public static final int PA_X_MAX = 3;

	public static final int PA_Y_NONE = 0;

	public static final int PA_Y_MIN = 1;

	public static final int PA_Y_MID = 2;

	public static final int PA_Y_MAX = 3;

	public static final int PS_MEET = 0;

	public static final int PS_SLICE = 1;

	private int parSpecifier = PS_MEET;

	public int getParAlignX()
	{
		return parAlignX;
	}

	public void setParAlignX( int parAlignX )
	{
		this.parAlignX = parAlignX;
		set = true;
	}

	public int getParAlignY()
	{
		return parAlignY;
	}

	public void setParAlignY( int parAlignY )
	{
		this.parAlignY = parAlignY;
		set = true;
	}

	public int getParSpecifier()
	{
		return parSpecifier;
	}

	public void setParSpecifier( int parSpecifier )
	{
		this.parSpecifier = parSpecifier;
		set = true;
	}

	private int parAlignX = PA_X_MID;

	private int parAlignY = PA_Y_MID;

	@Override
	public String getStringValue()
	{
		throw new UnsupportedOperationException( "Not supported yet." );
	}

	private boolean contains( String haystack, String needle )
	{
		return haystack.indexOf( needle ) != -1;
	}

	@Override
	public void setStringValue( String value )
	{

		if (contains(
			value,
			"none" ))
		{
			parAlignX = PA_X_NONE;
			parAlignY = PA_Y_NONE;
		}
		else
			if (contains(
				value,
				"xMinYMin" ))
			{
				parAlignX = PA_X_MIN;
				parAlignY = PA_Y_MIN;
			}
			else
				if (contains(
					value,
					"xMidYMin" ))
				{
					parAlignX = PA_X_MID;
					parAlignY = PA_Y_MIN;
				}
				else
					if (contains(
						value,
						"xMaxYMin" ))
					{
						parAlignX = PA_X_MAX;
						parAlignY = PA_Y_MIN;
					}
					else
						if (contains(
							value,
							"xMinYMid" ))
						{
							parAlignX = PA_X_MIN;
							parAlignY = PA_Y_MID;
						}
						else
							if (contains(
								value,
								"xMidYMid" ))
							{
								parAlignX = PA_X_MID;
								parAlignY = PA_Y_MID;
							}
							else
								if (contains(
									value,
									"xMaxYMid" ))
								{
									parAlignX = PA_X_MAX;
									parAlignY = PA_Y_MID;
								}
								else
									if (contains(
										value,
										"xMinYMax" ))
									{
										parAlignX = PA_X_MIN;
										parAlignY = PA_Y_MAX;
									}
									else
										if (contains(
											value,
											"xMidYMax" ))
										{
											parAlignX = PA_X_MID;
											parAlignY = PA_Y_MAX;
										}
										else
											if (contains(
												value,
												"xMaxYMax" ))
											{
												parAlignX = PA_X_MAX;
												parAlignY = PA_Y_MAX;
											}

		if (contains(
			value,
			"meet" ))
		{
			parSpecifier = PS_MEET;
		}
		else
			if (contains(
				value,
				"slice" ))
			{
				parSpecifier = PS_SLICE;
			}
		set = true;

	}

}
