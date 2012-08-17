package com.cybernostics.lib.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jasonw
 *
 */
public class CommandArguments
{
	private static List< String > args = new ArrayList< String >();

	public static List< String > get()
	{
		return args;
	}

	public static void set( String[] args )
	{
		CommandArguments.args.addAll( Arrays.asList( args ) );
	}

}
