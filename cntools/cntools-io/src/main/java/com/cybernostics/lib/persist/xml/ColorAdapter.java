package com.cybernostics.lib.persist.xml;

import java.awt.Color;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ColorAdapter extends XmlAdapter< Argb, Color >
{

	@Override
	public Argb marshal( Color c ) throws Exception
	{
		return new Argb( c );
	}

	@Override
	public Color unmarshal( Argb c ) throws Exception
	{
		return Argb.getColor( c );
	}
}
