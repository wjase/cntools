/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

/**
 *
 * @author jasonw
 */
public class StringAttribute extends NamedAttribute
{

	private String value = null;

	protected boolean set = false;

	public StringAttribute( String name )
	{
		super( name );
	}

	public StringAttribute( String name, String value )
	{
		this( name );
		this.value = value;
	}

	@Override
	public String getStringValue()
	{
		return this.value;
	}

	@Override
	public void setStringValue( String value )
	{
		this.value = value;
		isSet( value != null );
	}

	@Override
	public boolean isSet()
	{
		return set;
	}

	public void isSet( boolean flag )
	{
		set = flag;
	}

	public final void setOriginalString( String value )
	{
		this.value = value;
	}

	public StringAttribute withName( String name )
	{
		setName( name );
		isSet( false );
		return this;
	}

	//    public static final String PRESENTATION_STYLE_FORMAT = "%s=\"%s\"";
	//    public static final String CSS_STYLE_FORMAT = "%s:%s;";
	//    
	//    private String format = CSS_STYLE_FORMAT;
	//    @Override
	//    public String asSVGString( )
	//    {
	//        return String.format( format, getName(), getStringValue() );
	//    }
}
