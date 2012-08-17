package com.cybernostics.lib.resourcefinder.protocols.resloader;

import java.net.URL;

/**
 *
 * @author jasonw
 */
public class ResloaderURLBits
{

	private URL url = null;
	private String classComponent = null;

	public String getClassComponent()
	{
		return classComponent;
	}

	public String getPathComponent()
	{
		return pathComponent;
	}

	private String pathComponent = null;

	private ResloaderURLBits( URL toParse )
	{
		this.url = toParse;
	}

	private void parse()
	{
		String combinedPathPart = url.getPath();

		classComponent = url.getHost();
		pathComponent = url.getPath();

	}

	public static ResloaderURLBits create( URL toParse )
	{
		ResloaderURLBits rl = new ResloaderURLBits( toParse );
		rl.parse();
		return rl;
	}

}
