/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.cybernostics.lib.resourcefinder.protocols.resloader;

import com.cybernostics.lib.patterns.singleton.DefaultConstructorSingleton;
import com.cybernostics.lib.patterns.singleton.SingletonInstance;
import com.cybernostics.lib.urlfactory.CustomUrlFactory;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author jasonw
 */
public class ResloaderURLFactory implements CustomUrlFactory
{

	private static SingletonInstance< ResloaderURLFactory > inst = new SingletonInstance< ResloaderURLFactory >()
	{

		@Override
		protected ResloaderURLFactory createInstance()
		{
			return new ResloaderURLFactory();
		}
	};

	public static ResloaderURLFactory get()
	{
		return inst.get();
	}

	@Override
	public URL create( String spec ) throws MalformedURLException
	{
		return new URL( null, spec, Handler.get() );
	}

}
