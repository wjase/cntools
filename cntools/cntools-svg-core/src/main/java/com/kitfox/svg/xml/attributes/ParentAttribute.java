/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.elements.SVGElement;

/**
 * Represents a value returned for a style attribute
 * @author jasonw
 */
public class ParentAttribute implements IStringAttribute
{

	private IStringAttribute parentValue = null;

	public boolean isSet()
	{
		return parentValue.isSet();
	}

	public IStringAttribute getParentValue()
	{
		return parentValue;
	}

	private SVGElement owner = null;

	public ParentAttribute( SVGElement e, IStringAttribute parentValue )
	{
		this.parentValue = parentValue;
		this.owner = e;
	}

	@Override
	public String getStringValue()
	{
		return parentValue.getStringValue();
	}

	@Override
	public void setStringValue( String value )
	{
		if (owner.initStyleAtttributeValue(
			parentValue.getName(),
			value ))
		{
			return;
		}
		StringAttribute sa = new StringAttribute( parentValue.getName(), value );
		owner.setStyleAttribute(
			parentValue.getName(),
			value,
			sa );
	}

	@Override
	public void setName( String name )
	{

	}

	@Override
	public String getName()
	{
		return parentValue.getName();
	}

	@Override
	public String asSVGString()
	{
		return parentValue.asSVGString();
	}

}
