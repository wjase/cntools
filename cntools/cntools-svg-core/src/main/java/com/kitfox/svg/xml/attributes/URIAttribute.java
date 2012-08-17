/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author jasonw
 */
public class URIAttribute extends StringAttribute
{

	private URI valueB;

	public URIAttribute( String name )
	{
		super( name );
	}

	public URI getURIValue()
	{
		return valueB;
	}

	public void setValue( URI value )
	{
		this.valueB = value;
	}

	@Override
	public void setStringValue( String value )
	{
		try
		{
			setValue( new URI( value ) );
		}
		catch (URISyntaxException ex)
		{

		}
	}

}
