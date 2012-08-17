/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.elements.ElementHierachyListener;
import com.kitfox.svg.elements.SVGElement;
import java.net.URI;

/**
 *
 * @author jasonw
 */
public class ElementReferenceAttribute extends RelativeURIAttribute
{

	private SVGElement referencedElement;

	public ElementReferenceAttribute( String name, String value )
	{
		super( name );
		setStringValue( value );
	}

	public ElementReferenceAttribute( String name, SVGElement parent )
	{
		super( name );
		this.parent = parent;
	}

	public SVGElement getElement()
	{
		if (isSet() && referencedElement == null)
		{
			String val = getStringValue();
			setStringValue( val );
		}
		return referencedElement;
	}

	@Override
	public void setStringValue( String value )
	{
		if (value == null)
		{
			throw new NullPointerException( "Null setValue() for " + getName() );
		}
		if (value.isEmpty())
		{
			isSet( false );
			return; // nothing to set
		}
		super.setStringValue( value );
		isSet( true );
		if (parent.getParent() == null)
		{
			parent.addHierachyListener( new ElementHierachyListener()
			{

				@Override
				public void addedToParent( SVGElement parent )
				{
					setValue( parent.getDiagram()
						.getUniverse()
						.getElement(
							getURI() ) );
				}
			} );
		}
		else
		{
			URI toGet = getURI();
			setValue( parent.getDiagram()
				.getUniverse()
				.getElement(
					toGet ) );
		}

	}

	public void setValue( SVGElement value )
	{
		this.referencedElement = value;
		isSet( true );
		fireUpdate();

	}

}
