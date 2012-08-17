package com.cybernostics.lib.gui.shapeeffects;

import java.awt.Color;
import java.awt.color.ColorSpace;

/**
 *
 * @author jasonw
 */
public class MutableColor extends Color
{

	int mutableValue = 0;

	public MutableColor()
	{
		super( 0 );
	}

	public MutableColor( ColorSpace cspace, float[] components, float alpha )
	{
		super( cspace, components, alpha );

	}

	public MutableColor( float r, float g, float b, float a )
	{
		this( (int) ( r * 255 + 0.5 ),
			(int) ( g * 255 + 0.5 ),
			(int) ( b * 255 + 0.5 ),
			(int) ( a * 255 + 0.5 ) );
	}

	public MutableColor( float r, float g, float b )
	{
		this( r, g, b, 1.0f );
	}

	public MutableColor( int rgba, boolean hasalpha )
	{
		super( 0 );
		if (hasalpha)
		{
			mutableValue = rgba;
		}
		else
		{
			mutableValue = 0xff000000 | rgba;
		}
	}

	public MutableColor( int rgb )
	{
		super( rgb );
		mutableValue = 0xff000000 | rgb;
	}

	public MutableColor( int r, int g, int b, int a )
	{
		super( 0 );
		mutableValue = ( ( a & 0xFF ) << 24 ) | ( ( r & 0xFF ) << 16 )
			| ( ( g & 0xFF ) << 8 ) | ( ( b & 0xFF ) << 0 );
	}

	public MutableColor( int r, int g, int b )
	{
		this( r, g, b, 255 );
	}

	@Override
	public int getRGB()
	{
		return mutableValue;
	}

	public void setAlpha( int alpha )
	{
		mutableValue &= 0x00FFFFFF;
		mutableValue |= ( ( alpha & 0xFF ) << 24 );
	}

	public void setRed( int red )
	{
		mutableValue &= 0xFF00FFFF;
		mutableValue |= ( ( red & 0xFF ) << 16 );
	}

	public void setGreen( int green )
	{
		mutableValue &= 0xFFFF00FF;
		mutableValue |= ( ( green & 0xFF ) << 8 );
	}

	public void setBlue( int blue )
	{
		mutableValue &= 0xFFFFFF00;
		mutableValue |= ( blue & 0xFF );
	}
}
