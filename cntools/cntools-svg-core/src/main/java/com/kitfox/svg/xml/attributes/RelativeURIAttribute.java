/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.elements.SVGElement;
import java.io.File;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jasonw
 */
public class RelativeURIAttribute extends StringAttribute
{

	public RelativeURIAttribute( String name )
	{
		super( name );
	}

	private URI href = null;

	public static final Pattern matchUrlPattern = Pattern.compile( "\\s*url\\((.*)\\)\\s*" );

	protected SVGElement parent = null;

	public RelativeURIAttribute( String name, SVGElement parent )
	{
		super( name );
		this.parent = parent;
	}

	public URI getURI()
	{
		return href;
	}

	public static URI parseURIValue( URI base, String stringValue )
	{
		try
		{
			String fragment = parseURLFn( stringValue );
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
			return null;
		}
	}

	public static String parseURLFn( String stringValue ) throws Exception
	{
		Matcher matchUrl = ElementReferenceAttribute.matchUrlPattern.matcher( stringValue );
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
			throw e;
		}
	}

	public void setParent( SVGElement parent )
	{
		this.parent = parent;
	}

	@Override
	public void setStringValue( String value )
	{
		href = parseURIValue(
			parent.getXMLBase(),
			value );
		super.setStringValue( value );

	}

	public void setValue( URI value )
	{
		href = value;
	}
}
