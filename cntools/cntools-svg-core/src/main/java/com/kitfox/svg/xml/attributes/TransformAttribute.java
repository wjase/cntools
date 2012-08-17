/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.XMLParseUtil;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class TransformAttribute extends StringAttribute
{
	private AffineTransform xform = new AffineTransform();

	public AffineTransform getXform()
	{
		return xform;
	}

	public TransformAttribute( String name )
	{
		super( name );
	}

	@Override
	public void setStringValue( String value )
	{
		super.setStringValue( value );
		parseTransform(
			value,
			xform );

	}

	static protected AffineTransform parseTransform( String val,
		AffineTransform dest )
	{
		final Matcher matchExpression = Pattern.compile(
			"\\w+\\([^)]*\\)" )
			.matcher(
				"" );

		AffineTransform retXform = dest == null ? new AffineTransform() : dest;

		matchExpression.reset( val );
		while (matchExpression.find())
		{
			try
			{
				retXform.concatenate( parseSingleTransform( matchExpression.group() ) );
			}
			catch (SVGException ex)
			{
				throw new RuntimeException( ex );
			}
		}

		return retXform;
	}

	static public AffineTransform parseSingleTransform( String val )
		throws SVGException
	{
		final Matcher matchWord = Pattern.compile(
			"[-.\\w]+" )
			.matcher(
				"" );

		AffineTransform retXform = new AffineTransform();

		matchWord.reset( val );
		if (!matchWord.find())
		{
			//Return identity transformation if no data present (eg, empty string)
			return retXform;
		}

		String function = matchWord.group()
			.toLowerCase();

		LinkedList termList = new LinkedList();
		while (matchWord.find())
		{
			termList.add( matchWord.group() );
		}

		double[] terms = new double[ termList.size() ];
		Iterator it = termList.iterator();
		int count = 0;
		while (it.hasNext())
		{
			terms[ count++ ] = XMLParseUtil.parseDouble( (String) it.next() );
		}

		//Calculate transformation
		if (function.equals( "matrix" ))
		{
			retXform.setTransform(
				terms[ 0 ],
				terms[ 1 ],
				terms[ 2 ],
				terms[ 3 ],
				terms[ 4 ],
				terms[ 5 ] );
		}
		else
			if (function.equals( "translate" ))
			{
				retXform.setToTranslation(
					terms[ 0 ],
					terms[ 1 ] );
			}
			else
				if (function.equals( "scale" ))
				{
					if (terms.length > 1)
					{
						retXform.setToScale(
							terms[ 0 ],
							terms[ 1 ] );
					}
					else
					{
						retXform.setToScale(
							terms[ 0 ],
							terms[ 0 ] );
					}
				}
				else
					if (function.equals( "rotate" ))
					{
						if (terms.length > 2)
						{
							retXform.setToRotation(
								Math.toRadians( terms[ 0 ] ),
								terms[ 1 ],
								terms[ 2 ] );
						}
						else
						{
							retXform.setToRotation( Math.toRadians( terms[ 0 ] ) );
						}
					}
					else
						if (function.equals( "skewx" ))
						{
							retXform.setToShear(
								Math.toRadians( terms[ 0 ] ),
								0.0 );
						}
						else
							if (function.equals( "skewy" ))
							{
								retXform.setToShear(
									0.0,
									Math.toRadians( terms[ 0 ] ) );
							}
							else
							{
								throw new SVGException( "Unknown transform type" );
							}

		return retXform;
	}

}
