package com.cybernostics.lib.persist.xml.test;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DerivedClass extends ABase
{

	@XmlElement
	public int aNewIntValue;

	@XmlElement
	String name = "test";

	public DerivedClass()
	{

	}

}
