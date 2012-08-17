package com.cybernostics.lib.persist.xml.test;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ABase
{

	private String aName;

	public ABase()
	{

	}

	public ABase( String name )
	{
		aName = name;
	}

	public String getaName()
	{
		return aName;
	}

	public void setaName( String someName )
	{
		this.aName = someName;
	}

}
