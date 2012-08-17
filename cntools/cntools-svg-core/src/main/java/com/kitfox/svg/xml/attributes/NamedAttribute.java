/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
abstract public class NamedAttribute
	implements
	IStringAttribute,
	MutableAttribute
{

	private String name = null;

	public NamedAttribute( String name )
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setName( String name )
	{
		this.name = name;
	}

	// standard presentation attribute format (css must do its own thing
	private static final String SVGFormat = " %s=\"%s\"";

	@Override
	public String asSVGString()
	{
		if (!isSet())
		{
			return "";
		}

		return String.format(
			SVGFormat,
			getName(),
			getStringValue() );
	}

	private AttributeListener listener = doNothing;

	public void fireUpdate()
	{
		listener.attributeUpdated( this );
	}

	@Override
	public void setAttributeListener( AttributeListener listener )
	{
		this.listener = listener;
	}

	private static final AttributeListener doNothing = new AttributeListener()
	{

		@Override
		public void attributeUpdated( IStringAttribute source )
		{
			// NOP! uncomment the following for diagnostics - then remove it again!
			//            Exception e = new Exception();
			//            System.out.printf( "No listener for %s:\n",source.asSVGString() );
			//            e.printStackTrace();
		}
	};

}
