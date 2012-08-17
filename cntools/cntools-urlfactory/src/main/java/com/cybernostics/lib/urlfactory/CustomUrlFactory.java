/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.urlfactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public interface CustomUrlFactory
{
	public URL create( String spec ) throws MalformedURLException;

}
