package com.cybernostics.lib.resourcefinder.net.customurl;

import com.cybernostics.lib.urlfactory.ProtocolURLFactory;
import com.cybernostics.lib.urlfactory.URLFactory;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Allows you to add your own URL handler without running into problems
 * of race conditions with setURLStream handler.
 *
 * To add your custom protocol eg myprot://blahblah:
 *
 * 1) Create a new protocol package which ends in myprot eg com.myfirm.protocols.myprot
 * 2) Create a subclass of URLStreamHandler called Handler in this package
 * 3) Before you use the protocol, call CustomURLScheme.add(com.myfirm.protocols.myprot.Handler.class);
 *
 * @author jasonw
 */
public class CustomURLScheme
{

	private static Set< String > protocols = new HashSet< String >();
	// this is the package name required to implelent a Handler class

	private static Pattern packagePattern = Pattern.compile( "(.+\\.protocols)\\.([^\\.]+)" );

	private static Map< String, CustomURLResourceChildFinder > childFinders =
																new HashMap< String, CustomURLResourceChildFinder >();

	private static Map< String, URLStreamHandler > handlers = new HashMap< String, URLStreamHandler >();

	public static void registerCustomFinder( String protocolname,
		CustomURLResourceChildFinder finder )
	{
		childFinders.put(
			protocolname,
			finder );
	}

	public static CustomURLResourceChildFinder getFinder( String finder )
	{
		return childFinders.get( finder );
	}

	public static boolean isRegistered( String protocolName )
	{
		return protocols.contains( protocolName );
	}

	public static URLStreamHandler getHandler( String protocol )
	{
		return handlers.get( protocol );
	}

	/**
	 * Call this method with your handler class
	 *
	 * @param handlerClass
	 * @param finder class used to find child resources of a given custom URL
	 * (Some url schemes don't need this, others do, when they map to file systems or archives)
	 * @throws Exception
	 */
	public static void add( URLStreamHandler handler,
		CustomURLResourceChildFinder finder )
			throws Exception
	{
		Class< ? extends URLStreamHandler > handlerClass = handler.getClass();

		if (handlerClass.getSimpleName()
			.equals(
				"Handler" ))
		{
			String pkgName = handlerClass.getPackage()
				.getName();
			Matcher m = packagePattern.matcher( pkgName );

			if (m.matches())
			{
				String protocolPackage = m.group( 1 );
				String protocolName = m.group( 2 );
				if (!protocols.contains( protocolName ))
				{
					protocols.add( protocolName );
					handlers.put(
						protocolName,
						handler );
					URLFactory.registerURLFactory(
						protocolName,
						new ProtocolURLFactory( handler ) );
					add( protocolPackage );
					if (finder != null)
					{
						registerCustomFinder(
							protocolName,
							finder );
					}
				}

			}
			else
			{
				throw new CustomURLHandlerException(
						"Your Handler class package must end in 'protocols.yourprotocolname' eg com.somefirm.blah.protocols.yourprotocol" );
			}

		}
		else
		{
			throw new CustomURLHandlerException( "Your handler class must be called 'Handler'" );
		}
	}

	private static void add( String handlerPackage )
	{
		// this property controls where java looks for
		// stream handlers - always uses current value.
		final String key = "java.protocol.handler.pkgs";

		String newValue = handlerPackage;
		System.out.println( String.format(
			"Installing custom url:%s",
			handlerPackage ) );
		String previousValue = System.getProperty(
			key,
			"" );

		// if we haven't already installed it
		if (previousValue.indexOf( handlerPackage ) == -1)
		{
			newValue = String.format(
				"%s%s%s",
				newValue,
				previousValue.isEmpty() ? "" : "|",
				previousValue );
			;
			System.out.println( String.format(
				"Result:%s\n",
				newValue ) );
			System.setProperty(
				key,
				newValue );
		}

	}

}
