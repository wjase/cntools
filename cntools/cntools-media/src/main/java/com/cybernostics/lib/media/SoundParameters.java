package com.cybernostics.lib.media;

import com.cybernostics.lib.patterns.singleton.DefaultConstructorSingleton;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;

/**
 *
 * @author jasonw
 */
public class SoundParameters
{

	private float volume = 0.7f;
	private int loopCount = 1;

	private static final SoundParameters looper = new SoundParameters( SoundEffect.LOOP,
		1.0f );

	public static SoundParameters loop()
	{
		return looper;
	}

	public SoundParameters()
	{
	}

	public SoundParameters( int count )
	{
		loopCount = count;
	}

	public SoundParameters( float vol )
	{
		volume = vol;
	}

	public SoundParameters( int count, float vol )
	{
		loopCount = count;
		volume = vol;
	}

	private static SingletonInstance< SoundParameters > defParams = new DefaultConstructorSingleton< SoundParameters >(
		SoundParameters.class );

	public static SoundParameters getDefault()
	{
		return defParams.get();
	}

	public int getLoopCount()
	{
		return loopCount;
	}

	public void setLoopCount( int loopCount )
	{
		this.loopCount = loopCount;
	}

	public float getVolume()
	{
		return volume;
	}

	public void setVolume( float volume )
	{
		this.volume = volume;
	}
}
