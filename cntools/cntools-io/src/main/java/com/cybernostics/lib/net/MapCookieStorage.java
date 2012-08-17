package com.cybernostics.lib.net;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jasonw
 *
 */
public class MapCookieStorage implements CookieStore
{
	Map< URI, List< HttpCookie >> cookieStore = new HashMap< URI, List< HttpCookie >>();

	@Override
	public boolean removeAll()
	{
		cookieStore.clear();
		return true;
	}

	@Override
	public boolean remove( URI uri, HttpCookie cookie )
	{
		cookieStore.remove( uri );
		return true;
	}

	@Override
	public List< URI > getURIs()
	{
		List< URI > uris = new ArrayList< URI >();
		uris.addAll( cookieStore.keySet() );

		return uris;
	}

	@Override
	public List< HttpCookie > getCookies()
	{
		ArrayList< HttpCookie > cookies = new ArrayList< HttpCookie >();
		for (List< HttpCookie > eachList : cookieStore.values())
		{
			cookies.addAll( eachList );
		}

		return cookies;
	}

	@Override
	public List< HttpCookie > get( URI uri )
	{
		if (!cookieStore.containsKey( uri ))
		{
			cookieStore.put(
				uri,
				new ArrayList< HttpCookie >() );
		}
		return cookieStore.get( uri );
	}

	@Override
	public void add( URI uri, HttpCookie cookie )
	{
		if (!cookieStore.containsKey( uri ))
		{
			cookieStore.put(
				uri,
				new ArrayList< HttpCookie >() );
		}
		cookieStore.get(
			uri )
			.add(
				cookie );
	}

	public MapCookieStorage()
	{
	}

}
