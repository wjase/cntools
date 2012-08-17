/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.urlfactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use this to register a new protocol handler to be used when creating a
 * URL from a string. Unfortunately (as at JDK 1.7) there is no way to hook this automagically
 * to the regular URL constructor when in WebStart... so we could just use new URL(String).
 * 
 * Instead, any places in code where you would have called new URL(String) with a custom protocol,
 * you now need to call URLFactory.newURL(String); .
 * 
 * You also need your own URLStreamhandler subclass which you wrap in an instance of 
 * ProtocolURLFactory and pump that into the static URLFactory.register("protocolname", ProtocolURLFactory);
 *
 * @author jasonw
 */
public class URLFactory implements CustomUrlFactory
{

	private static URLFactory inst = null;

	private static URLFactory get()
	{
		if (inst == null)
		{
			inst = new URLFactory();
		}
		return inst;
	}

	public static URL newURL( String s ) throws MalformedURLException
	{
		return get().create(
			s );
	}

	/**
	 * Static access only thanks.
	 */
	private URLFactory()
	{
	}

	/**
	 * Register a new URL creator class for a given protocol.
	 * For non-webstart apps you can also call CustomURLScheme.register. See resloader
	 * implementation in cntools-resourcefinder.
	 *
	 * @param protocol - name of the protocol (without any colon)
	 * @param toRegister - a class which will spit out a URL with the right URLStreamHandler attached
	 */
	public static void registerURLFactory( String protocol,
		CustomUrlFactory toRegister )
	{
		schemes.put(
			protocol,
			toRegister );
	}

	private static Map< String, CustomUrlFactory > schemes = new HashMap< String, CustomUrlFactory >();

	private static final Pattern protocolPattern = Pattern.compile(
		"^\\s*([a-z]+):",
		Pattern.CASE_INSENSITIVE );

	@Override
	public URL create( String spec ) throws MalformedURLException
	{
		Matcher m = protocolPattern.matcher( spec );

		if (m.find())
		{
			String protocol = m.group( 1 );
			if (!isValidProtocol( protocol ))
			{
				throw new MalformedURLException( spec );
			}
			CustomUrlFactory protocolFactory = schemes.get( protocol );
			if (protocolFactory != null)
			{
				return protocolFactory.create( spec );
			}
		}
		return new URL( spec );
	}

	/*
	 * Returns true if specified string is a valid protocol name.
	 */
	private boolean isValidProtocol( String protocol )
	{
		int len = protocol.length();
		if (len < 1)
		{
			return false;
		}
		char c = protocol.charAt( 0 );
		if (!Character.isLetter( c ))
		{
			return false;
		}
		for (int i = 1; i < len; i++)
		{
			c = protocol.charAt( i );
			if (!Character.isLetterOrDigit( c ) && c != '.' && c != '+' &&
					c != '-')
			{
				return false;
			}
		}
		return true;
	}

}
