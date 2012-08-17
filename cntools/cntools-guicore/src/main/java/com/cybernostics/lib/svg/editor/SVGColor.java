package com.cybernostics.lib.svg.editor;

import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.kitfox.svg.xml.ColorTable;
import java.awt.Color;
import java.util.Map.Entry;
import java.util.*;

/**
 *
 * @author jasonw
 */
public class SVGColor extends Color implements Comparable< SVGColor >
{

	public static void main( String[] args )
	{
		Collection< SVGColor > cols = svgColors.get()
			.values();

		for (SVGColor each : cols)
		{
			System.out.printf(
				"%s\n",
				each.getSVGText() );
		}

		System.out.println( fromName( "black" ) );
	}

	private String SVGtext = "";

	public SVGColor( String name, Color hue )
	{
		super( hue.getRGB() );
		this.SVGtext = name;
	}

	/**
	 * created either with a color name or css color code
	 *
	 * @param s
	 */
	public SVGColor( String s )
	{
		super( parseColor(
			s ).getRGB() );
		this.SVGtext = s;
	}

	public static Color parseColor( String s )
	{
		getColors();
		SVGColor svgC = svgColors.get()
			.get(
				s );

		if (svgC != null)
		{
			return svgC;
		}

		if (s.equals( "black" ))
		{
			throw new RuntimeException( "Whats up with black??? should have been found" );
		}
		//        if ( !s.startsWith( "#" ) )
		//        {
		//
		//            Color hue = svgColors.get( s );
		//            if ( hue != null )
		//            {
		//                return hue;
		//            }
		//
		//        }
		return Color.decode( s );
	}

	public static final String CSS_COLOUR = "#%02x%02x%02x";

	public static String getSVGColorSpec( Color c )
	{
		if (c instanceof SVGColor)
		{
			return ( (SVGColor) c ).getSVGText();
		}
		return String.format(
			CSS_COLOUR,
			c.getRed(),
			c.getGreen(),
			c.getBlue() );
	}

	public static SVGColor fromName( String colorName )
	{
		return svgColors.get()
			.get(
				colorName );
	}

	public String getSVGText()
	{
		return SVGtext;
	}

	private static SingletonInstance< Map< String, SVGColor >> svgColors = new SingletonInstance< Map< String, SVGColor >>()
	{

		@Override
		protected Map< String, SVGColor > createInstance()
		{
			Map< String, SVGColor > aMap = new TreeMap< String, SVGColor >();
			Map colors = ColorTable.getAllColors();
			Set< Entry< String, Color >> colSet = colors.entrySet();
			for (Entry< String, Color > eachColor : colSet)
			{
				if (eachColor.getKey()
					.startsWith(
						"current" ))
				{
					continue;
				}
				SVGColor newColor = new SVGColor( eachColor.getKey(),
					eachColor.getValue() );
				aMap.put(
					eachColor.getKey(),
					newColor );

			}
			return aMap;
		}

	};

	//private static Map<String, SVGColor> svgColors = null;
	public static Collection< SVGColor > getColors()
	{
		return svgColors.get()
			.values();
	}

	private static List< String > basicColors = Arrays.asList(
		"magenta",
		"cyan",
		"orange",
		"yellow",
		"pink",
		"darkGray",
		"gray",
		"lightGray",
		"green",
		"blue",
		"red",
		"white",
		"black" );

	private Integer myIntObj = null;

	private Integer getMyIntRGB()
	{
		if (myIntObj == null)
		{
			myIntObj = new Integer( getRGB() );
		}
		return myIntObj;
	}

	/**
	 * if the color is in the basic list then it comes first, otherwise it is
	 * sorted by rgb value
	 *
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo( SVGColor o )
	{
		int myIndex = ( !SVGtext.startsWith( "#" ) ) ? basicColors.indexOf( SVGtext )
			: -1;
		int otherIndex = ( !o.SVGtext.startsWith( "#" ) ) ? basicColors.indexOf( o.getSVGText() )
			: -1;
		if (myIndex >= 0)
		{
			if (otherIndex == -1 || myIndex > otherIndex)
			{
				return -1;
			}
			if (myIndex == otherIndex)
			{
				return 0;
			}
			if (myIndex < otherIndex)
			{
				return 1;
			}
		}
		if (otherIndex >= 0)
		{
			return 1;
		}

		return getMyIntRGB().compareTo(
			o.getMyIntRGB() );

	}

}
