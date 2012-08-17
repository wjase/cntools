/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.kitfox.svg.xml.attributes;

import com.kitfox.svg.xml.NumberWithUnits;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public interface IStyleAttribute extends Serializable
{
	double[] getDoubleList();

	float[] getFloatList();

	float getFloatValueWithUnits();

	int[] getIntList();

	int getIntValue();

	String getName();

	NumberWithUnits getNumberWithUnits();

	float getRatioValue();

	String[] getStringList();

	String getStringValue();

	URI getURIValue();

	/**
	 * Parse this style attribute as a URL and return it in URI form resolved
	 * against the passed base.
	 *
	 * @param base - URI to resolve against. If null, will return value without
	 * attempting to resolve it.
	 */
	URI getURIValue( URI base );

	URL getURLValue( URL docRoot );

	URL getURLValue( URI docRoot );

	String getUnits();

	String parseURLFn() throws Exception;

	void setStringValue( String value );

}
