package com.cybernostics.lib.net;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author jasonw
 * 
 */
public class CustomCookieManager extends CookieManager
{
	private static CookieManager singleton = null;

	public static CookieManager get()
	{
		if (singleton == null)
		{
			singleton = new CustomCookieManager();
		}
		return singleton;
	}

	CustomCookieManager()
	{
		super( new MapCookieStorage(), CookiePolicy.ACCEPT_ALL );
	}

	@Override
	public Map< String, List< String >> get( URI uri,
		Map< String, List< String >> requestHeaders ) throws IOException
	{

		return super.get(
			uri.resolve( "/" ),
			requestHeaders );
	}

	@Override
	public CookieStore getCookieStore()
	{
		return super.getCookieStore();
	}

	@Override
	public void put( URI uri, Map< String, List< String >> responseHeaders )
		throws IOException
	{

		List< String > newCookies = responseHeaders.get( "Set-Cookie" );

		if (newCookies != null)
		{
			for (String eachCookie : newCookies)
			{
				Pattern p = Pattern.compile( "(=|; )" );
				String[] tokens = p.split( eachCookie );

				HttpCookie cook = new HttpCookie( tokens[ 0 ], tokens[ 1 ] );

				// "XDEBUG_SESSION=netbeans-xdebug; expires=Thu, 10-Jun-2010 22:05:37 GMT; path=/";
				for (int index = 0; index < tokens.length; index += 2)
				{
					if (tokens[ index ].equalsIgnoreCase( "path" ))
					{
						cook.setPath( tokens[ index + 1 ] );
					}
					if (tokens[ index ].equalsIgnoreCase( "expires" ))
					{
						try
						{
							Date expiresOn = DateFormat.getDateInstance()
								.parse(
									tokens[ index + 1 ] );
							Date now = Calendar.getInstance()
								.getTime();
							cook.setMaxAge( expiresOn.getTime() - now.getTime() );
						}
						catch (ParseException e)
						{
						}
					}
				}
				getCookieStore().add(
					uri.resolve( "/" ),
					cook );
				// HttpCookie(m.group( 1 ), m.group( 2 )) );
				// Pattern p = Pattern.compile( "^(?:([^=]+)=(.+)(?:$| ;))+$" );
				// Matcher m = p.matcher( eachCookie );
				// if(m.matches())
				// {
				// getCookieStore().add( uri.resolve( "/" ), new
				// HttpCookie(m.group( 1 ), m.group( 2 )) );
				// }
			}

		}

	}

	@Override
	public void setCookiePolicy( CookiePolicy cookiePolicy )
	{
		// TODO Auto-generated method stub
		super.setCookiePolicy( cookiePolicy );
	}

}
