/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class CSSStyleAttribute extends NamedAttribute
	implements
	Map< String, IStringAttribute >
{

	@Override
	public boolean isSet()
	{
		for (IStringAttribute eachAttribute : values.values())
		{
			if (eachAttribute.isSet())
			{
				return true;
			}
		}

		return false;
	}

	private Map< String, IStringAttribute > values = new TreeMap< String, IStringAttribute >();

	private String cachedValue = null;

	public CSSStyleAttribute( String name )
	{
		super( name );
	}

	private static final String cssAttFormat = "%s:%s;";

	@Override
	public String getStringValue()
	{
		StringBuilder sb = new StringBuilder();
		for (IStringAttribute eachAtt : values.values())
		{
			if (eachAtt.isSet())
			{
				sb.append( String.format(
					cssAttFormat,
					eachAtt.getName(),
					eachAtt.getStringValue() ) );
			}
		}
		return sb.toString();

	}

	@Override
	public void setStringValue( String value )
	{
		parseStyle(
			value,
			values );

		if (cachedValue == null)
		{
			cachedValue = value;
		}
	}

	@Override
	public Collection< IStringAttribute > values()
	{
		return values.values();
	}

	@Override
	public int size()
	{
		return values.size();
	}

	@Override
	public IStringAttribute remove( Object key )
	{
		return values.remove( key );
	}

	@Override
	public void putAll( Map< ? extends String, ? extends IStringAttribute > m )
	{
		values.putAll( m );
	}

	@Override
	public IStringAttribute put( String key, IStringAttribute value )
	{
		return values.put(
			key,
			value );
	}

	@Override
	public Set< String > keySet()
	{
		return values.keySet();
	}

	@Override
	public boolean isEmpty()
	{
		return values.isEmpty();
	}

	@Override
	public int hashCode()
	{
		return values.hashCode();
	}

	@Override
	public IStringAttribute get( Object key )
	{
		return values.get( key );
	}

	@Override
	public boolean equals( Object o )
	{
		return values.equals( o );
	}

	@Override
	public Set< Entry< String, IStringAttribute >> entrySet()
	{
		return values.entrySet();
	}

	@Override
	public boolean containsValue( Object value )
	{
		return values.containsValue( value );
	}

	@Override
	public boolean containsKey( Object key )
	{
		return values.containsKey( key );
	}

	@Override
	public void clear()
	{
		values.clear();
	}

	private static final Pattern patSemi = Pattern.compile( ";" );

	private static final Pattern patColonSpace = Pattern.compile( ":" );
	//Strips left and right whitespace

	private static Matcher matcherContent = Pattern.compile(
		"\\s*([^\\s](.*[^\\s])?)\\s*" )
		.matcher(
			"" );

	/*
	 * * Takes a CSS style string and returns a hash of them.
	 * @param styleString - A CSS formatted string of styles. Eg,
	 * "font-size:12;fill:#d32c27;fill-rule:evenodd;stroke-width:1pt;"
	 * @param map - A map to which these styles will be added
	 */
	public static void parseStyle( String styleString,
		Map< String, IStringAttribute > map )
	{

		String[] styles = patSemi.split( styleString );

		for (int i = 0; i < styles.length; ++i)
		{
			if (styles[ i ].length() == 0)
			{
				continue;
			}

			String[] vals = patColonSpace.split( styles[ i ] );

			matcherContent.reset( vals[ 0 ] );
			matcherContent.matches();
			vals[ 0 ] = matcherContent.group( 1 );

			matcherContent.reset( vals[ 1 ] );
			matcherContent.matches();
			vals[ 1 ] = matcherContent.group( 1 );

			IStringAttribute att = map.get( vals[ 0 ] );
			if (att != null)
			{
				att.setStringValue( vals[ 1 ] );
			}
			else
			{
				att = createAttribute( vals[ 0 ] );
				att.setStringValue( vals[ 1 ] );
				map.put(
					vals[ 0 ],
					att );
			}
		}

		//return map;
	}

	public static IStringAttribute createAttribute( String name )
	{
		if (name.indexOf( "olor" ) != -1)
		{
			return new ColorAttribute( name );
		}

		return new StringAttribute( name );
	}

	public void put( IStringAttribute eachAttribute )
	{
		put(
			eachAttribute.getName(),
			eachAttribute );
	}

}
