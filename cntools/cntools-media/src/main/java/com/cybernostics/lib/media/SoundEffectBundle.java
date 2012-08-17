package com.cybernostics.lib.media;

import com.cybernostics.lib.concurrent.CallableWorkerTask;
import com.cybernostics.lib.resourcefinder.Finder;
import com.cybernostics.lib.resourcefinder.ResourceFinderException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages a collection of Sound Effects
 * @author jasonw
 */
public class SoundEffectBundle
{

	private Finder loader = null;

	public Finder getLoader()
	{
		return loader;
	}

	public void setLoader( Finder loader )
	{
		this.loader = loader;
	}

	ConcurrentMap< String, SoftReference< SoundEffect >> effects = new ConcurrentHashMap< String, SoftReference< SoundEffect >>();

	public SoundEffect get( String path ) throws ResourceFinderException
	{
		return this.get(
			path,
			loader );
	}

	private static final String MP3Ext = ".mp3";

	public SoundEffect get( String path, Finder loader )
		throws ResourceFinderException
	{
		if (effects.containsKey( path ))
		{
			SoundEffect fromCache = effects.get(
				path )
				.get();
			if (fromCache != null)
			{
				return fromCache;
			}
		}

		URL soundURL = loader.getResource( path.endsWith( MP3Ext ) ? path
			: path + MP3Ext );
		if (soundURL == null)
		{
			throw new RuntimeException( "Resource not found: " + path );
		}
		SoundEffect newEffect = new SoundEffect( soundURL, 1.0f );
		effects.put(
			path,
			new SoftReference< SoundEffect >( newEffect ) );

		return newEffect;
	}

	public void preload( final String... resources )
	{
		CallableWorkerTask loadTask = new CallableWorkerTask(
			"loading sound effects" )
		{

			@Override
			protected Object doTask() throws Exception
			{
				for (String toLoad : resources)
				{
					SoundEffectBundle.this.get( toLoad );
				}
				return null;
			}
		};

		loadTask.start();
	}
}
